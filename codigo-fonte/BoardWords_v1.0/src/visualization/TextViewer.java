/*
TextViewer

BoardWords v1.0.0

Copyright (C) 2012, 2013 BoardWords

This file is part of BoardWords.

BoardWords is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

BoardWords is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with BoardWords. If not, see <http://www.gnu.org/licenses/>.
*/

/*Agradecimentos à toda a comunidade Java, pelo suporte técnico prestado;
em especial aos fóruns de discussão, que ajudam a desvendar conceitos e 
dominar técnicas de forma muito produtiva.*/

package visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import mainMenu.MainProgress;
import mainMenu.Principal;
import data.Text;
import data.TextBase;
import frequency.Frequency;



public class TextViewer 
{
	
	
	private JFrame window = new JFrame();
	//Como o tamanho da visualização é variável, por depender de vários fatores de configuração e
	//do tamanho dos dados de entrada (textos), o campo de visualização depende de um scroll para
	//que o usuário possa visualizar toda as informações geradas. Por isso, resolvemos colocar
	//toda a parte gráfica dentro de um JPanel contido em um JScrollPane.
	private JPanel panelMenu = new JPanel();    //Painél de funcionalidades da janela (contém os botões).
	private ZoomPanel zoomPanel = new ZoomPanel(3);    //Painél de visualização.
	private JScrollPane scrollPaneDisplay = new JScrollPane(zoomPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JButton impButton = new JButton();
	private JButton expButton = new JButton();
	private JButton labColorButton = new JButton();
	private JButton configButton = new JButton();
	private JButton visButton = new JButton();
	private JButton colorButton = new JButton();
	private JButton selectionColorButton = new JButton();
	private JButton zoomOutButton = new JButton();
	private JButton zoomInButton = new JButton();
	private JButton selectTextsButton = new JButton();
	private JButton selectWordsButton = new JButton();
	private JButton newVisButton = new JButton();
	private JButton textsButton = new JButton();
	private JButton fontColorButton = new JButton();
	private JTextField dateLabel = new JTextField();    //Label de identificação do agrupamento de dias da visualização 'word'.
	private JTextField scheduleLabel = new JTextField();    //Label de identificção do agrupamento de horário da visualização 'word'.
	private String title = "Painel ";
	private Configuration config;
	private Text[] texts;    //Cópia dos dados processados no painel principal.
	private Frequency frequency;
	private Color selColor = new Color(58, 95, 205);    //Cor de seleção padrão.
	private Color labColor = Color.WHITE;    //Cor dos identificadores de agrupamento e do horários dos textos.
	private Integer visIndex;
	private TextBase[] textsBase;
	private Principal principal;
	private ArrayList<JTextField> labels;    //Componentes identificadores de agrupamentos e tempos.
	private boolean isColorByHour = true;
	private TextMultiple textsHighlight;    //Janela de exibição de palavras selecionadas.
	private ArrayList<Object[]> selectedWords;    //Palavras selecionadas (palavra, cor).
	private boolean selWords = false;    //Verifica se há palavras selecionadas.
	private boolean processCanceled = false;
	private TextViewer textViewer = this;
	private int id;
	
	
	public TextViewer(final Principal principal, TextBase[] texts, Frequency frequency) throws CloneNotSupportedException, IOException, BadLocationException    //Índice da janela a ser criada - referência de nome.
	{
		window.setVisible(false);
		this.principal = principal;
		this.textsBase = texts;    //Necessário para selecionar textos para outro painel.
		this.visIndex = this.principal.getIndex();    //Índice de identificação dos paineis.
		this.principal.incIndex();    //Incrementando o índice de identificação.
		this.frequency = frequency;    //Necessário para obter a lista de palavras selecionadas para serem incluídas na visualização por Palavra.
		title += this.visIndex;
		id = this.visIndex;    //ID de controle para remover esse objeto da classe principal 'Principal'.
		
		/* Criando uma cópia da estrutura principal de dados processados: */
		this.texts = new Text[texts.length];
		config = new Configuration(this.visIndex, this.texts, zoomPanel);
		
		window.setTitle(title);
		window.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		window.setMinimumSize(new Dimension(800, 600));
		window.setLocationRelativeTo(null);    //Abrir janela no meio da tela.
		window.setResizable(true);
		window.getContentPane().add(panelMenu, BorderLayout.NORTH);
		window.getContentPane().add(scrollPaneDisplay, BorderLayout.CENTER);
		panelMenu.setLayout(null);    //Necessário para forçar o posicionamento dos botões.
		panelMenu.setPreferredSize(new Dimension(800, 34));
		panelMenu.setBackground(new Color(58, 95, 205));
		scrollPaneDisplay.setPreferredSize(new Dimension(800, 566));
		zoomPanel.setBackground(Color.BLACK);
		zoomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		zoomPanel.setVisible(true);
		scrollPaneDisplay.setAutoscrolls(true);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);    //Evitando o fechamento da janela para fechar todos as instâncias ligadas à ela juntamente.
		window.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		});
		
		for (int idxData = 0; idxData < texts.length && !principal.isCanceled(); idxData++) {
			/* Clonando (real, c/ independência relacional) todos os objetos da estrutura: */
			this.texts[idxData] = texts[idxData].clone(idxData, this, config);    //Passando os JTextFields como parâmetro para serem atualizados pelas próprias palavras.
		}
		
		if (principal.isCanceled()) {
			processCanceled = true;
			return;
		}
		
		setPanelMenu();
		
		boolean leg = config.isLeg();    //Legenda visível?
		int height = 8, width = 8;    //Altura e largura, respectivamente, do painél de visualização (zoomPanel).
		ArrayList<Text> textGroup = new ArrayList<Text>();    //Armazena temporariamente o agrupamento atual de datas para organizá-lo em ordem crescente de horários.
		labels = new ArrayList<JTextField>();
		
		Text[] allTexts = new Text[texts.length];    //Necessário para mostrar os textos em ordem.
		
		for (int idxText = 0; idxText < texts.length && !principal.isCanceled(); idxText++) {
			/* Criando um agrupamento para ordenar crescentemente os horários: */
			textGroup.add(this.texts[idxText]);
			if (this.texts[idxText].getTextWidth()+90 > width) width = this.texts[idxText].getTextWidth()+90;    //Atualizando a largura do painél com base no tamanho dos textos.
		}
		
		if (principal.isCanceled()) {
			processCanceled = true;
			return;
		}
		
		sortTime(textGroup);
		for (int idxText = 0; idxText < textGroup.size() && !principal.isCanceled(); idxText++) {    //Lendo todos os textos.
			allTexts[idxText] = textGroup.get(idxText);
			textGroup.get(idxText).setPosition(12, idxText*24 + 12);
			
			height += 24;
			textGroup.get(idxText).setTimeColor(labColor, leg);
			zoomPanel.add(textGroup.get(idxText).getTime());    //Adicionando os horários de criação dos textos.
			zoomPanel.add(textGroup.get(idxText).getTextBoard());    //Adicionando os painéis de palavras ao painél de visualização.
			labels.add(textGroup.get(idxText).getTime());
		}
		
		if (principal.isCanceled()) {
			processCanceled = true;
			return;
		}
		
		try {
			textsHighlight.setTexts(allTexts);    //Atualizando a lista de textos em ordem.
			if (selectedWords != null) textsHighlight.selectWords(selectedWords);
		} 
		catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		textGroup = null;
		zoomPanel.setPreferredSize(new Dimension(width, height));    //Definindo o tamanho correto do painél 'zoomPanel' para poder realizar o scroll.
		zoomPanel.revalidate();
		zoomPanel.repaint();
		config.setLabels(labels);    //Atualizando os labels.
		window.setVisible(true);
	}

	
	public void setPanelMenu() throws IOException, BadLocationException
	{
		createVisButton();    //Gerar visualização.
		createConfigButton();    //Configurar visualização.
		createImportButton();    //Importar configurações.
		createExportButton();    //Exportar configurações.
		createLabelsButton();    //Alterar cor dos identificadores (datas e agrupamentos).
		createColorButton();    //Alterar cor de fundo do painel de visualização e da exibição dos textos com palavras selecionadas.
		createSelColorButton();    //Alterar cor da seleção de textos e palavras.
		createSelectWordsButton();    //Selecionar palavras para destacar.
		createFontColorButton();    //Alterar cor da fonte dos textos da janela de destaque de palavras.
		createTextsButton();    //Visualizar textos com palavras destacadas.
		createSelectTextsButton();    //Selecionar textos para gerar novo painel de visualização.
		createNewVisButton();    //Exportar textos selecionados para novo painel de visualização.
		createZoomOutButton();    //Diminuir imagem de visualização.
		createZoomInButton();    //Aumentar imagem de visualização.
		
		createDateLabel();    //Identificador do intervalo de datas do agrupamento.
		createScheduleLabel();    //Identificador do intervalo de horários do agrupamento.
		zoomPanel.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{		
			}
			
			@Override
			public void mousePressed(MouseEvent e) 
			{	
			}
			
			@Override
			public void mouseExited(MouseEvent e) 
			{
			}
			
			@Override
			public void mouseEntered(MouseEvent e) 
			{
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if (e.getClickCount() == 2) {    //Limpando a seleção de todos os textos ou de todas as palavras.
					removeAllSelections();
				}
			}
		});
	}

	
	public void createVisButton()
	{
		visButton.setToolTipText("Gerar visualização");
		visButton.setIcon(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&16.png")));
		visButton.setBounds(3, 3, 28, 28);
		visButton.setBackground(new Color(25, 25, 112));
		visButton.setVisible(true);
		visButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				processCanceled = false;
				
				if (config.isGroup()) {    //Visualização baseada em agrupamento.
					if (config.isValid()) {
						Thread thread = new Thread(new Runnable() 
						{
							@Override
							public void run() 
							{
								window.setVisible(false);
								int byHour = Integer.MAX_VALUE;    //Controle de limite de tempo para a coloração baseada em faixas de horas do dia; 4 faixas.
								Color colorByHour = null;    //Cores baseadas em faixas de horário do dia (4 faixas). 
								boolean leg = config.isLeg();    //Legenda visível?
								int horValue, vertValue;
								int height = 8, width = 8;    //Altura e largura, respectivamente, do painél de visualização (zoomPanel).
								VisualProgress pr = new VisualProgress(textViewer, true);
								
								zoomPanel.restore();    //Restaurando a escala de zoom original.
								zoomPanel.removeAll();    //Limpando visualização anterior.
								zoomPanel.revalidate();
								zoomPanel.repaint();
								
								ArrayList<Text> textGroup = new ArrayList<Text>();    //Armazena temporariamente o agrupamento atual de datas para organizá-lo em ordem crescente de horários.
								labels = new ArrayList<JTextField>();
								
								horValue = config.getHorValue();
								vertValue = config.getVertValue();
								java.util.Date lowerLimDate = null, upperLimDate = null;    //Limite inferior e superior do agrupamento das datas, respectivamente.
								int upperLimTime = 0, lowerLimTime = 0;    //Limite superior e inferiro do agrupamento dos horários.
								int timeConst = 1;    //Constante de conversão do tempo em minutos (1 - se o atributo for minuto; 60 - se atributor for hora).
								int idxDateInterval = -12;    //Índice do posicionamento vertical do JTextField de intervalo de datas.
								java.util.Date curDate = null;
								Object interval;    //Auxiliar para adicionar o componente no painel e no ArrayList de visibilidade.
								Text[] allTexts = new Text[texts.length];    //Necessário para mostrar os textos em ordem.
								int idxAllTexts = 0;
								
								if (config.getTypeSubGroup().equals("hour")) timeConst = 60;    //Transformar horas em minutos.
								if (360%(vertValue*timeConst) == 0) {    //Verificando se a coloração é baseada em faixas: deve ser divisível por 6h (1h, 2h, 3h ou 6h).
									isColorByHour = true;
									byHour = 360;
									colorByHour = getColorByHour(byHour);
								}
								else isColorByHour = false;
								
								switch (config.getTypeGroup()) {
									case "day": {
										try {
											lowerLimDate =  (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(texts[0].getDate());
										} 
										catch (ParseException e1) {
											pr.error();
											e1.printStackTrace();
										}
										upperLimDate = (java.util.Date) sumDays(lowerLimDate, horValue-1);    //Primeiro limite do intervalo de agrupamento.
										idxDateInterval += 24;
										height += 24;
										
										if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
										else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
										zoomPanel.add(((DateInterval) interval).getInterval());
										labels.add(((DateInterval) interval).getInterval());
										
										for (int idxText = 0; (idxText < texts.length || !textGroup.isEmpty()) && !processCanceled; idxText++) {    //Lendo todas as linhas do vetor de textos.
											if (idxText < texts.length) {
												if (texts[idxText].getTextWidth()+90 > width) width = texts[idxText].getTextWidth()+90;    //Atualizando a largura do painél com base no tamanho dos textos.
												height += 24;    //Para cada novo componente adicionado, atualizamos a altura do painél.
												
												try {
													curDate = (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(texts[idxText].getDate());
												} 
												catch (ParseException e1) {
													pr.error();
													e1.printStackTrace();
												}
											}
											
											if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
												if (byHour == 1440) byHour = 360;
												else byHour += 360;
												colorByHour = getColorByHour(byHour);
											}
											
											if (curDate.after(upperLimDate) || idxText == texts.length) {    //A data atual excedeu o limite superior ou o último agrupamento ainda não foi impresso.
												sortTime(textGroup);
												lowerLimTime = 0;    //Reiniciando limite inferior.
												if (((vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
												else upperLimTime = (vertValue*timeConst)-1;
												idxDateInterval += 24;
												height += 24;
												byHour = 360;    //Reiniciando a cor da faixa do dia.
												colorByHour = getColorByHour(byHour);
												
												if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
												else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());
												zoomPanel.add((JTextField) interval);
												labels.add((JTextField) interval);
												
												for (int idx = 0; idx < textGroup.size() && !processCanceled; idx++) {    //Imprimindo textos do conjunto de datas.
													if (textGroup.get(idx).getMinutes() < lowerLimTime || textGroup.get(idx).getMinutes() > upperLimTime) {    //O horário atual excedeu o limite superior.
														lowerLimTime = upperLimTime+1;
													    //Incrementando o limite de tempo:
														if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
														else upperLimTime += (vertValue*timeConst);
														idxDateInterval += 24;
														height += 24;
														if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
															if (byHour == 1440) byHour = 360;
															else byHour += 360;
															colorByHour = getColorByHour(byHour);
														}
														
														if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
														else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());
														zoomPanel.add((JTextField) interval);
														labels.add((JTextField) interval);
														
														while ((textGroup.get(idx).getMinutes() < lowerLimTime || textGroup.get(idx).getMinutes() > upperLimTime) && !processCanceled) {    //Procurando o grupo de horário correspondente ao texto atual.
															lowerLimTime = upperLimTime+1;
														    //Incrementando o limite de tempo:
															if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
															else upperLimTime += (vertValue*timeConst);
															idxDateInterval += 24;
															height += 24;
															if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
																if (byHour == 1440) byHour = 360;
																else byHour += 360;
																colorByHour = getColorByHour(byHour);
															}													
															
															if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
															else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());
															zoomPanel.add((JTextField) interval);
															labels.add((JTextField) interval);
														}
														
														if (processCanceled) {
															zoomPanel.restore();    //Restaurando a escala de zoom original.
															zoomPanel.removeAll();    //Limpando visualização anterior.
															zoomPanel.revalidate();
															zoomPanel.repaint();
															labels = new ArrayList<JTextField>();
															pr.close();
															window.setVisible(true);
															return;
														}
													}
													
													idxDateInterval += 24;
													textGroup.get(idx).setPosition(12, idxDateInterval);
													allTexts[idxAllTexts] = textGroup.get(idx);
													++idxAllTexts;
													if (isColorByHour) textGroup.get(idx).setTimeColor(colorByHour, leg);    //Colorindo o label de tempo.
													else textGroup.get(idx).setTimeColor(labColor, leg);
													zoomPanel.add(textGroup.get(idx).getTime());    //Adicionando o label de horário.
													zoomPanel.add(textGroup.get(idx).getTextBoard());    //Adicionando o texto.
													labels.add(textGroup.get(idx).getTime());
												}
												textGroup = new ArrayList<Text>();    //Excluindo o agrupamento temporário.
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
												
												
												while (upperLimTime < 1439 && !processCanceled) {    //Imprimindo o resto do grupo de horários, se houver (caso o agrupamento 'textGroup' tenha se encerado)..
													lowerLimTime = upperLimTime+1;
												    //Incrementando o limite de tempo:
													if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
													else upperLimTime += (vertValue*timeConst);
													idxDateInterval += 24;
													height += 24;
													if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
														if (byHour == 1440) byHour = 360;
														else byHour += 360;
														colorByHour = getColorByHour(byHour);
													}
													
													if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
													else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());
													zoomPanel.add((JTextField) interval);
													labels.add((JTextField) interval);
												}
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
												
												if (idxText == texts.length) break;
												
												byHour = 360;    //Reiniciando a cor da faixa do dia.
												colorByHour = getColorByHour(byHour);
												lowerLimDate = sumDays(upperLimDate, 1);
												upperLimDate = sumDays(upperLimDate, horValue);    //Atualizando o limite superior com base nele mesmo (para manter o agrupamento sequencial).
												idxDateInterval += 24;    //Incrementando o intervalo visual de datas.
												height += 24;
												
												if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
												else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
												zoomPanel.add(((DateInterval) interval).getInterval());
												labels.add(((DateInterval) interval).getInterval());
												
												while (curDate.after(upperLimDate) && !processCanceled) {    //Imprimindo os intervalos de datas inferiores à data do texto atual.
													lowerLimDate = sumDays(upperLimDate, 1);
													upperLimDate = sumDays(upperLimDate, horValue);    //Atualizando o limite superior com base nele mesmo (para manter o agrupamento sequencial).
													idxDateInterval += 24;    //Incrementando o intervalo visual de datas.
													height += 24;
													
													if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
													else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
													zoomPanel.add(((DateInterval) interval).getInterval());
													labels.add(((DateInterval) interval).getInterval());
												}
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
											}
											
											textGroup.add(texts[idxText]);    //Adicionando o texto atual à seu respectivo agrupamento.
										}
										
										if (processCanceled) {
											zoomPanel.restore();    //Restaurando a escala de zoom original.
											zoomPanel.removeAll();    //Limpando visualização anterior.
											zoomPanel.revalidate();
											zoomPanel.repaint();
											labels = new ArrayList<JTextField>();
											pr.close();
											window.setVisible(true);
											return;
										}
										
										try {
											textsHighlight.setTexts(allTexts);
											if (selectedWords != null) textsHighlight.selectWords(selectedWords);
										} 
										catch (BadLocationException e1) {
											pr.error();
											e1.printStackTrace();
										}
										pr.close();
										window.setVisible(true);
									} break;
									case "month": {
										try {
											lowerLimDate =  (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(texts[0].getDate());
											lowerLimDate = getMinDay(lowerLimDate);
										} 
										catch (ParseException e1) {
											pr.error();
											e1.printStackTrace();
										}
										upperLimDate = (java.util.Date) sumMonths(lowerLimDate, horValue-1);    //Primeiro limite do intervalo de agrupamento.
										upperLimDate = getMaxDay(upperLimDate);
										idxDateInterval += 24;
										height += 24;
										
										if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
										else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
										zoomPanel.add(((DateInterval) interval).getInterval());
										labels.add(((DateInterval) interval).getInterval());
										
										for (int idxText = 0; (idxText < texts.length || !textGroup.isEmpty()) && !processCanceled; idxText++) {    //Lendo todas as linhas do vetor de textos.
											if (idxText < texts.length) {
												if (texts[idxText].getTextWidth()+90 > width) width = texts[idxText].getTextWidth()+90;    //Atualizando a largura do painél com base no tamanho dos textos.
												height += 24;    //Para cada novo componente adicionado, atualizamos a altura do painél.
												try {
													curDate = (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(texts[idxText].getDate());
												} 
												catch (ParseException e1) {
													pr.error();
													e1.printStackTrace();
												}
											}
											
											if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
												if (byHour == 1440) byHour = 360;
												else byHour += 360;
												colorByHour = getColorByHour(byHour);
											}
											
											if (curDate.after(upperLimDate) || idxText == texts.length) {    //A data atual excedeu o limite superior ou o último agrupamento ainda não foi impresso.
												sortTime(textGroup);
												lowerLimTime = 0;    //Reiniciando limite inferior.
												if (((vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
												else upperLimTime = (vertValue*timeConst)-1;
												idxDateInterval += 24;
												height += 24;
												byHour = 360;    //Reiniciando a cor da faixa do dia.
												colorByHour = getColorByHour(byHour);
												
												if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
												else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());
												zoomPanel.add((JTextField) interval);
												labels.add((JTextField) interval);
												
												for (int idx = 0; idx < textGroup.size() && !processCanceled; idx++) {    //Imprimindo textos do conjunto de datas.
													if (textGroup.get(idx).getMinutes() < lowerLimTime || textGroup.get(idx).getMinutes() > upperLimTime) {    //O horário atual excedeu o limite superior.
														lowerLimTime = upperLimTime+1;
													    //Incrementando o limite de tempo:
														if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
														else upperLimTime += (vertValue*timeConst);
														idxDateInterval += 24;
														height += 24;
														if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
															if (byHour == 1440) byHour = 360;
															else byHour += 360;
															colorByHour = getColorByHour(byHour);
														}
														
														if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
														else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());	
														zoomPanel.add((JTextField) interval);
														labels.add((JTextField) interval);
														
														while ((textGroup.get(idx).getMinutes() < lowerLimTime || textGroup.get(idx).getMinutes() > upperLimTime) && !processCanceled) {    //Procurando o grupo de horário corresponde ao texto atual.
															lowerLimTime = upperLimTime+1;
														    //Incrementando o limite de tempo:
															if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
															else upperLimTime += (vertValue*timeConst);
															idxDateInterval += 24;
															height += 24;
															if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
																if (byHour == 1440) byHour = 360;
																else byHour += 360;
																colorByHour = getColorByHour(byHour);
															}													
															
															if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
															else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());	
															zoomPanel.add((JTextField) interval);
															labels.add((JTextField) interval);
														}
														
														if (processCanceled) {
															zoomPanel.restore();    //Restaurando a escala de zoom original.
															zoomPanel.removeAll();    //Limpando visualização anterior.
															zoomPanel.revalidate();
															zoomPanel.repaint();
															labels = new ArrayList<JTextField>();
															pr.close();
															window.setVisible(true);
															return;
														}
													}
													
													idxDateInterval += 24;
													textGroup.get(idx).setPosition(12, idxDateInterval);
													allTexts[idxAllTexts] = textGroup.get(idx);
													++idxAllTexts;
													if (isColorByHour) textGroup.get(idx).setTimeColor(colorByHour, leg);    //Colorindo o label de tempo.
													else textGroup.get(idx).setTimeColor(labColor, leg);
													zoomPanel.add(textGroup.get(idx).getTime());    //Adicionando o label de horário.
													zoomPanel.add(textGroup.get(idx).getTextBoard());    //Adicionando o texto.
													labels.add(textGroup.get(idx).getTime());
												}
												textGroup = new ArrayList<Text>();    //Excluindo o agrupamento temporário.
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
												
												while (upperLimTime < 1439 && !processCanceled) {    //Imprimindo o resto do grupo de horários, se houver (caso o agrupamento 'textGroup' tenha se encerado)..
													lowerLimTime = upperLimTime+1;
												    //Incrementando o limite de tempo:
													if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
													else upperLimTime += (vertValue*timeConst);
													idxDateInterval += 24;
													height += 24;
													if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
														if (byHour == 1440) byHour = 360;
														else byHour += 360;
														colorByHour = getColorByHour(byHour);
													}
													
													if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
													else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());
													zoomPanel.add((JTextField) interval);
													labels.add((JTextField) interval);
												}
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
												
												if (idxText == texts.length) break;
												
												byHour = 360;    //Reiniciando a cor da faixa do dia.
												colorByHour = getColorByHour(byHour);
												lowerLimDate = sumMonths(upperLimDate, 1);
												lowerLimDate = getMinDay(lowerLimDate);
												upperLimDate = sumMonths(upperLimDate, horValue);    //Atualizando o limite superior com base nele mesmo (para manter o agrupamento sequencial).
												upperLimDate = getMaxDay(upperLimDate);
												
												idxDateInterval += 24;    //Incrementando o intervalo visual de datas.
												height += 24;
												if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
												else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
												zoomPanel.add(((DateInterval) interval).getInterval());
												labels.add(((DateInterval) interval).getInterval());
												
												while (curDate.after(upperLimDate) && !processCanceled) {    //Imprimindo os intervalos de datas inferiores à data do texto atual.
													lowerLimDate = sumMonths(upperLimDate, 1);
													lowerLimDate = getMinDay(lowerLimDate);
													upperLimDate = sumMonths(upperLimDate, horValue);    //Atualizando o limite superior com base nele mesmo (para manter o agrupamento sequencial).
													upperLimDate = getMaxDay(upperLimDate);
													idxDateInterval += 24;    //Incrementando o intervalo visual de datas.
													height += 24;
													
													if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
													else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
													zoomPanel.add(((DateInterval) interval).getInterval());
													labels.add(((DateInterval) interval).getInterval());
												}
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
											}
											
											textGroup.add(texts[idxText]);    //Adicionando o texto atual à seu respectivo agrupamento.
										}	
										
										if (processCanceled) {
											zoomPanel.restore();    //Restaurando a escala de zoom original.
											zoomPanel.removeAll();    //Limpando visualização anterior.
											zoomPanel.revalidate();
											zoomPanel.repaint();
											labels = new ArrayList<JTextField>();
											pr.close();
											window.setVisible(true);
											return;
										}
										
										try {
											textsHighlight.setTexts(allTexts);
											if (selectedWords != null) textsHighlight.selectWords(selectedWords);
										} 
										catch (BadLocationException e1) {
											pr.error();
											e1.printStackTrace();
										}
										pr.close();
										window.setVisible(true);
									} break;
									case "year": {
										try {
											lowerLimDate =  (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(texts[0].getDate());
											lowerLimDate = getMinDay(lowerLimDate);
											lowerLimDate = getMinMonth(lowerLimDate);
										} 
										catch (ParseException e1) {
											pr.error();
											e1.printStackTrace();
										}
										upperLimDate = sumYears(lowerLimDate, horValue-1);    //Primeiro limite do intervalo de agrupamento.
										upperLimDate = getMaxMonth(upperLimDate);
										upperLimDate = getMaxDay(upperLimDate);
										idxDateInterval += 24;
										height += 24;
										
										if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
										else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
										zoomPanel.add(((DateInterval) interval).getInterval());
										labels.add(((DateInterval) interval).getInterval());
										for (int idxText = 0; (idxText < texts.length || !textGroup.isEmpty()) && !processCanceled; idxText++) {    //Lendo todas as linhas do vetor de textos.
											if (idxText < texts.length) {
												if (texts[idxText].getTextWidth()+90 > width) width = texts[idxText].getTextWidth()+90;    //Atualizando a largura do painél com base no tamanho dos textos.
												height += 24;    //Para cada novo componente adicionado, atualizamos a altura do painél.
												try {
													curDate = (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(texts[idxText].getDate());
												} 
												catch (ParseException e1) {
													pr.error();
													e1.printStackTrace();
												}
											}
											
											if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
												if (byHour == 1440) byHour = 360;
												else byHour += 360;
												colorByHour = getColorByHour(byHour);
											}
												
											if (curDate.after(upperLimDate) || idxText == texts.length) {    //A data atual excedeu o limite superior ou o último agrupamento ainda não foi impresso.
												sortTime(textGroup);
												lowerLimTime = 0;    //Reiniciando limite inferior.
												if (((vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
												else upperLimTime = (vertValue*timeConst)-1;
												idxDateInterval += 24;
												height += 24;
												byHour = 360;    //Reiniciando a cor da faixa do dia.
												colorByHour = getColorByHour(byHour);
												
												if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
												else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());										
												zoomPanel.add((JTextField) interval);
												labels.add((JTextField) interval);
												
												for (int idx = 0; idx < textGroup.size() && !processCanceled; idx++) {    //Imprimindo textos do conjunto de datas.
													if (textGroup.get(idx).getMinutes() < lowerLimTime || textGroup.get(idx).getMinutes() > upperLimTime) {    //O horário atual excedeu o limite superior.
														lowerLimTime = upperLimTime+1;
													    //Incrementando o limite de tempo:
														if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
														else upperLimTime += (vertValue*timeConst);
														idxDateInterval += 24;
														height += 24;
														if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
															if (byHour == 1440) byHour = 360;
															else byHour += 360;
															colorByHour = getColorByHour(byHour);
														}
														
														if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
														else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());													zoomPanel.add((JTextField) interval);
														labels.add((JTextField) interval);
														
														while ((textGroup.get(idx).getMinutes() < lowerLimTime || textGroup.get(idx).getMinutes() > upperLimTime) && !processCanceled) {    //Procurando o grupo de horário corresponde ao texto atual.
															lowerLimTime = upperLimTime+1;
														    //Incrementando o limite de tempo:
															if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
															else upperLimTime += (vertValue*timeConst);
															idxDateInterval += 24;
															height += 24;
															if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
																if (byHour == 1440) byHour = 360;
																else byHour += 360;
																colorByHour = getColorByHour(byHour);
															}													
															
															if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
															else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());														zoomPanel.add((JTextField) interval);
															labels.add((JTextField) interval);
														}
														
														if (processCanceled) {
															zoomPanel.restore();    //Restaurando a escala de zoom original.
															zoomPanel.removeAll();    //Limpando visualização anterior.
															zoomPanel.revalidate();
															zoomPanel.repaint();
															labels = new ArrayList<JTextField>();
															pr.close();
															window.setVisible(true);
															return;
														}
													}
													
													idxDateInterval += 24;
													textGroup.get(idx).setPosition(12, idxDateInterval);
													allTexts[idxAllTexts] = textGroup.get(idx);
													++idxAllTexts;
													if (isColorByHour) textGroup.get(idx).setTimeColor(colorByHour, leg);    //Colorindo o label de tempo.
													else textGroup.get(idx).setTimeColor(labColor, leg);
													zoomPanel.add(textGroup.get(idx).getTime());    //Adicionando o label de horário.
													zoomPanel.add(textGroup.get(idx).getTextBoard());    //Adicionando o texto.
													labels.add(textGroup.get(idx).getTime());
												}
												textGroup = new ArrayList<Text>();    //Excluindo o agrupamento temporário.
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
												
												while (upperLimTime < 1439 && !processCanceled) {    //Imprimindo o resto do grupo de horários, se houver (caso o agrupamento 'textGroup' tenha se encerado)..
													lowerLimTime = upperLimTime+1;
												    //Incrementando o limite de tempo:
													if ((upperLimTime+(vertValue*timeConst)-1) >= 1440) upperLimTime = 1439;
													else upperLimTime += (vertValue*timeConst);
													idxDateInterval += 24;
													height += 24;
													if (upperLimTime > byHour) {    //Atualizando a cor da faixa do dia.
														if (byHour == 1440) byHour = 360;
														else byHour += 360;
														colorByHour = getColorByHour(byHour);
													}
													
													if (isColorByHour) interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, colorByHour, config.isLeg());
													else interval = timeInterval(toTime(lowerLimTime)+" - "+toTime(upperLimTime), 12, idxDateInterval, labColor, config.isLeg());											
													zoomPanel.add((JTextField) interval);
													labels.add((JTextField) interval);
												}
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
												
												if (idxText == texts.length) break;
												
												byHour = 360;    //Reiniciando a cor da faixa do dia.
												colorByHour = getColorByHour(byHour);
												lowerLimDate = sumYears(upperLimDate, 1);
												lowerLimDate = getMinDay(lowerLimDate);
												lowerLimDate = getMinMonth(lowerLimDate);
												upperLimDate = sumYears(upperLimDate, horValue);    //Atualizando o limite superior com base nele mesmo (para manter o agrupamento sequencial).
												upperLimDate = getMaxMonth(upperLimDate);
												upperLimDate = getMaxDay(upperLimDate);
												
												idxDateInterval += 24;    //Incrementando o intervalo visual de datas.
												height += 24;
												if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
												else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
												zoomPanel.add(((DateInterval) interval).getInterval());
												labels.add(((DateInterval) interval).getInterval());
												
												while (curDate.after(upperLimDate) && !processCanceled) {    //Imprimindo os intervalos de datas inferiores à data do texto atual.
													lowerLimDate = sumYears(upperLimDate, 1);
													lowerLimDate = getMinDay(lowerLimDate);
													lowerLimDate = getMinMonth(lowerLimDate);
													upperLimDate = sumYears(upperLimDate, horValue);    //Atualizando o limite superior com base nele mesmo (para manter o agrupamento sequencial).
													upperLimDate = getMaxMonth(upperLimDate);
													upperLimDate = getMaxDay(upperLimDate);
													idxDateInterval += 24;    //Incrementando o intervalo visual de datas.
													height += 24;
													if (isColorByHour) interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, colorByHour, config.isLeg());
													else interval = new DateInterval(convertDate(lowerLimDate)+" - "+convertDate(upperLimDate), 12, idxDateInterval, labColor, config.isLeg());
													zoomPanel.add(((DateInterval) interval).getInterval());
													labels.add(((DateInterval) interval).getInterval());
												}
												
												if (processCanceled) {
													zoomPanel.restore();    //Restaurando a escala de zoom original.
													zoomPanel.removeAll();    //Limpando visualização anterior.
													zoomPanel.revalidate();
													zoomPanel.repaint();
													labels = new ArrayList<JTextField>();
													pr.close();
													window.setVisible(true);
													return;
												}
											}
											
											textGroup.add(texts[idxText]);    //Adicionando o texto atual à seu respectivo agrupamento.
										}
										
										if (processCanceled) {
											zoomPanel.restore();    //Restaurando a escala de zoom original.
											zoomPanel.removeAll();    //Limpando visualização anterior.
											zoomPanel.revalidate();
											zoomPanel.repaint();
											labels = new ArrayList<JTextField>();
											pr.close();
											window.setVisible(true);
											return;
										}
									}
									try {
										textsHighlight.setTexts(allTexts);
										if (selectedWords != null) textsHighlight.selectWords(selectedWords);
									} 
									catch (BadLocationException e1) {
										pr.error();
										e1.printStackTrace();
									}
									pr.close();
									window.setVisible(true);
								}
								zoomPanel.setPreferredSize(new Dimension(width, height));    //Definindo o tamanho correto do painél 'zoomPanel' para poder realizar o scroll.
								zoomPanel.revalidate();
								zoomPanel.repaint();
								config.setLabels(labels);    //Atualizando os labels.
								pr.close();
								window.setVisible(true);
							}
						});
						thread.start();
					}	
				}
				else {    //Visualização simples, sem agrupamento.
					Thread thread = new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							window.setVisible(false);
							boolean leg = config.isLeg();    //Legenda visível?
							int height = 8, width = 8;    //Altura e largura, respectivamente, do painél de visualização (zoomPanel).
							MainProgress pr = new MainProgress(null);
							
							zoomPanel.restore();    //Restaurando a escala de zoom original.
							zoomPanel.removeAll();    //Limpando visualização anterior.
							zoomPanel.revalidate();
							zoomPanel.repaint();
							
							ArrayList<Text> textGroup = new ArrayList<Text>();    //Armazena temporariamente o agrupamento atual de datas para organizá-lo em ordem crescente de horários.
							labels = new ArrayList<JTextField>();
							
							Text[] allTexts = new Text[texts.length];    //Necessário para mostrar os textos em ordem.
							
							for (int idxText = 0; idxText < texts.length && !processCanceled; idxText++) {
								/* Criando um agrupamento para ordenar crescentemente os horários: */
								textGroup.add(texts[idxText]);
								if (texts[idxText].getTextWidth()+90 > width) width = texts[idxText].getTextWidth()+90;    //Atualizando a largura do painél com base no tamanho dos textos.
							}
							
							if (processCanceled) {
								zoomPanel.restore();    //Restaurando a escala de zoom original.
								zoomPanel.removeAll();    //Limpando visualização anterior.
								zoomPanel.revalidate();
								zoomPanel.repaint();
								labels = new ArrayList<JTextField>();
								pr.close();
								window.setVisible(true);
								return;
							}
							
							sortTime(textGroup);
							for (int idxText = 0; idxText < textGroup.size() && !processCanceled; idxText++) {    //Lendo todos os textos.
								allTexts[idxText] = textGroup.get(idxText);
								textGroup.get(idxText).setPosition(12, idxText*24 + 12);
								
								height += 24;
								textGroup.get(idxText).setTimeColor(labColor, leg);
								zoomPanel.add(textGroup.get(idxText).getTime());    //Adicionando os horários de criação dos textos.
								zoomPanel.add(textGroup.get(idxText).getTextBoard());    //Adicionando os painéis de palavras ao painél de visualização.
								labels.add(textGroup.get(idxText).getTime());
							}
							
							if (processCanceled) {
								zoomPanel.restore();    //Restaurando a escala de zoom original.
								zoomPanel.removeAll();    //Limpando visualização anterior.
								zoomPanel.revalidate();
								zoomPanel.repaint();
								labels = new ArrayList<JTextField>();
								pr.close();
								window.setVisible(true);
								return;
							}
							
							try {
								textsHighlight.setTexts(allTexts);    //Atualizando a lista de textos em ordem.
								if (selectedWords != null) textsHighlight.selectWords(selectedWords);
							} 
							catch (BadLocationException e1) {
								pr.error();
								e1.printStackTrace();
							}
							
							textGroup = null;
							zoomPanel.setPreferredSize(new Dimension(width, height));    //Definindo o tamanho correto do painél 'zoomPanel' para poder realizar o scroll.
							zoomPanel.revalidate();
							zoomPanel.repaint();
							config.setLabels(labels);    //Atualizando os labels.
							pr.close();
							window.setVisible(true);
						}
					});
					thread.start();
				}
			}
		});
		panelMenu.add(visButton);
	}	
	
	
	public void createConfigButton()
	{
		configButton.setToolTipText("Configurar parâmetros da visualização");
		configButton.setIcon(new ImageIcon(getClass().getResource("/icons/cogs_icon&16.png")));
		configButton.setBounds(34, 3, 28, 28);
		configButton.setBackground(new Color(25, 25, 112));
		configButton.setVisible(true);
		configButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				config.setVisible(true);
			}
		});
		panelMenu.add(configButton);
	}

	
	public void createImportButton()
	{
		impButton.setToolTipText("Importar arquivo de configurações");
		impButton.setIcon(new ImageIcon(getClass().getResource("/icons/import_icon&16.png")));
		impButton.setBounds(65, 3, 28, 28);
		impButton.setBackground(new Color(25, 25, 112));
		impButton.setVisible(true);
		impButton.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Selecionar arquivo de configurações");
				path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
				path.setAcceptAllFileFilterUsed(false); 
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = new File(path.getSelectedFile().getAbsolutePath());
					if (!file.exists()) {
						JOptionPane.showMessageDialog(null, "O arquivo selecionado não existe.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					String configs[], temp;
					
					try {
						BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "ISO-8859-1"));
						/* Lendo o arquivo de configurações: */
						temp = buffer.readLine();    //Verificando se há agrupamento.
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if (!configs[0].equals("grouping")) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						else if (configs[1].equals("true")) config.doClickCheckGroup(true);
						else if (configs[1].equals("false")) config.doClickCheckGroup(false);
						else {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						temp = buffer.readLine();    //Verificando se há legenda.
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if (!configs[0].equals("legend")) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						else if (configs[1].equals("true")) config.doClickCheckLeg(true);
						else if (configs[1].equals("false")) config.doClickCheckLeg(false);
						else {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						temp = buffer.readLine();    //Verificando se há exibição de frequências.
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if (!configs[0].equals("frequencies")) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						else if (configs[1].equals("true")) config.doClickCheckFreq(true);
						else if (configs[1].equals("false")) config.doClickCheckFreq(false);
						else {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						temp = buffer.readLine();    //Verificando tipo do agrupamento de dias (dia, mês ou ano).
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						switch (configs[1]) {
							case "day": {
								config.doClickDayButton();
							} break;
							case "month": {
								config.doClickMonthButton();
							} break;
							case "year": {
								config.doClickYearButton();
							} break;
							default: {
								JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
						temp = buffer.readLine();    //Selecionando o valor do agrupamento de dias.
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if (!isNumber(configs[1])) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						} 
						else config.setVertValue(configs[1]);
						temp = buffer.readLine();    //Verificando tipo do agrupamento de horário (minuto ou hora).
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						switch (configs[1]) {
							case "minute": {
								config.doClickMinButton();
							} break;
							case "hour": {
								config.doClickHourButton();
							} break;
							default: {
								JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
						temp = buffer.readLine();    //Selecionando o valor do agrupamento de horário.
						if (temp == null || (configs = temp.split(":")).length != 2) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if (!isNumber(configs[1])) {
							JOptionPane.showMessageDialog(null, "        O arquivo selecionado não\né um arquivo válido de configuração.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						else config.setHorValue(configs[1]);
						
						buffer.close();
					} 
					catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "O arquivo de entrada está\ncorrompido ou é inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		panelMenu.add(impButton);
	}
	

	public void createExportButton()
	{
		expButton.setToolTipText("Exportar arquivo de configurações");
		expButton.setIcon(new ImageIcon(getClass().getResource("/icons/export_icon&16.png")));
		expButton.setBounds(96, 3, 28, 28);
		expButton.setBackground(new Color(25, 25, 112));
		expButton.setVisible(true);
		expButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Selecionar diretório para salvar o arquivo de configurações");
				path.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = new File(path.getSelectedFile().getAbsolutePath()+File.separatorChar+"configurations.txt");
					if (file.exists()) {
						JOptionPane.showMessageDialog(null, "O arquivo de configuração já existe.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					try {
						if (!config.isValid()) return;
						OutputStreamWriter buffer = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
						
						if (config.isGroup()) buffer.write("grouping:true\n");
						else buffer.write("grouping:false\n");
						if (config.isLeg()) buffer.write("legend:true\n");
						else buffer.write("legend:false\n");
						if (config.isFreq()) buffer.write("frequencies:true\n");
						else buffer.write("frequencies:false\n");
						buffer.write("grouping_days:"+config.getTypeGroup()+"\n");
						buffer.write("value_grouping_days:"+config.getHorValue()+"\n");
						buffer.write("grouping_minutes:"+config.getTypeSubGroup()+"\n");
						buffer.write("value_grouping_minutes:"+config.getVertValue()+"\n");
						buffer.close();
					} 
					catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panelMenu.add(expButton);
	}
	
	
	public void createLabelsButton()
	{
		labColorButton.setToolTipText("Cor dos identificadores");
		labColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/flag_2_icon&16.png")));
		labColorButton.setBounds(127, 3, 28, 28);
		labColorButton.setBackground(new Color(25, 25, 112));
		labColorButton.setVisible(true);
		labColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Seleção da cor dos identificadores", labColor);
				
				if (newColor != null && newColor != selColor) {
					labColor = newColor;
					for (JTextField j : labels) {
						j.setBackground(labColor);
					}
					
					zoomPanel.revalidate();
					zoomPanel.repaint();
				}
			}
		});
		panelMenu.add(labColorButton);
	}
	
	
	public void createColorButton()
	{
		colorButton.setToolTipText("Alterar cor do painel");
		colorButton.setIcon(new ImageIcon(getClass().getResource("/icons/fill_icon&16.png")));
		colorButton.setBounds(158, 3, 28, 28);
		colorButton.setBackground(new Color(25, 25, 112));
		colorButton.setVisible(true);
		colorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Seleção da cor do painel", zoomPanel.getBackground());
				
				textsHighlight.setBackground(newColor);
				zoomPanel.setBackground(newColor);
				zoomPanel.repaint();
				zoomPanel.revalidate();
			}
		});
		panelMenu.add(colorButton);
	}
	
	
	public void createSelColorButton()
	{
		selectionColorButton.setToolTipText("Alterar cor de seleção dos textos e palavras");
		selectionColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/hand_2_icon&16.png")));
		selectionColorButton.setBounds(189, 3, 28, 28);
		selectionColorButton.setBackground(new Color(25, 25, 112));
		selectionColorButton.setVisible(true);
		selectionColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Seleção da cor de seleção dos textos e palavras", selColor);
				
				if (newColor != null && newColor != selColor) {
					selColor = newColor;
					
					for (int i = 0; i < texts.length; i++) {    //Atualizando a cor de seleção.
						texts[i].setSelectionColor(selColor);
					}
					
					zoomPanel.repaint();
					zoomPanel.revalidate();
				}
			}
		});
		panelMenu.add(selectionColorButton);
	}
	
	
	public void createSelectWordsButton()
	{
		selectWordsButton.setToolTipText("Selecionar palavras");
		selectWordsButton.setIcon(new ImageIcon(getClass().getResource("/icons/list_bullets_icon&16.png")));
		selectWordsButton.setBounds(220, 3, 28, 28);
		selectWordsButton.setBackground(new Color(25, 25, 112));
		selectWordsButton.setVisible(true);
		selectWordsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Selecionar arquivo de configurações");
				path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
				path.setAcceptAllFileFilterUsed(false);
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = new File(path.getSelectedFile().getAbsolutePath());
					if (!file.exists()) {
						JOptionPane.showMessageDialog(null, "O arquivo selecionado não existe.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					selWords = true;
					BufferedReader buffer = null;
					try {
						try {
							buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
						} 
						catch (UnsupportedEncodingException e2) {
							e2.printStackTrace();
						}
						try {
							String curWord;
							int i, j;
							
							//Se a lista de palavras já existir, apenas acrescenta. 
							if (selectedWords == null) selectedWords = new ArrayList<Object[]>();
							while (buffer.ready()) {
								curWord = buffer.readLine().trim();
								for (i = 0; i < texts.length; i++) {
									if (texts[i].isSelected()) texts[i].setSelected(false);    //A seleção de textos deve ser desfeita antes de destacar as palavras.
									
									for (j = 0; j < texts[i].size(); j++) {    //Procurando pela palavra 'curWord' no texto 'i'.
										if (texts[i].getWord(j).equals(curWord)) {
											if (searchWord(curWord) == -1) {    //Evita a inserção de palavras repetidas.
												Object[] selected = new Object[2];
												selected[0] = texts[i].getWord(j);    //Palavra.
												selected[1] = texts[i].getPixel(j).getBackground();    //Cor.
												selectedWords.add(selected);
											}
											texts[i].setWordOpaque(j, false);    //Selecionando a palavra, deixando-a invisível.
										}
									}
								}
								
								//Destaque das palavra selecionadas nos textos:
								textsHighlight.selectWords(selectedWords);
							}
							
							buffer.close();
						} 
						catch (IOException e1) {
							e1.printStackTrace();
						} 
						catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					} 
					catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panelMenu.add(selectWordsButton);
	}
	
	
	public void createFontColorButton() throws IOException, BadLocationException
	{
		fontColorButton.setToolTipText("Cor da fonte dos textos");
		fontColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/font_size_icon&16.png")));
		fontColorButton.setBounds(251, 3, 28, 28);
		fontColorButton.setBackground(new Color(25, 25, 112));
		fontColorButton.setVisible(true);
		fontColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Cor da fonte dos textos", textsHighlight.getForeground());
				
				textsHighlight.setForeground(newColor);
			}
		});
		panelMenu.add(fontColorButton);
	}
	
	
	public void createTextsButton() throws IOException, BadLocationException
	{
		textsHighlight = new TextMultiple(principal.getIndex()-1, texts);    //No construtor, 'principal.getIndex()' é incrementado.
		textsButton.setToolTipText("Palavras selecionadas");
		textsButton.setIcon(new ImageIcon(getClass().getResource("/icons/text_letter_t_icon&16.png")));
		textsButton.setBounds(282, 3, 28, 28);
		textsButton.setBackground(new Color(25, 25, 112));
		textsButton.setVisible(true);
		textsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				textsHighlight.setVisible(true);
			}
		});
		panelMenu.add(textsButton);
	}
	
	
	public void createSelectTextsButton()
	{
		selectTextsButton.setToolTipText("Selecionar textos por termo (palavra)");
		selectTextsButton.setIcon(new ImageIcon(getClass().getResource("/icons/indent_increase_icon&16.png")));
		selectTextsButton.setBounds(313, 3, 28, 28);
		selectTextsButton.setBackground(new Color(25, 25, 112));
		selectTextsButton.setVisible(true);
		selectTextsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Selecionar arquivo de configurações");
				path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
				path.setAcceptAllFileFilterUsed(false);
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = new File(path.getSelectedFile().getAbsolutePath());
					if (!file.exists()) {
						JOptionPane.showMessageDialog(null, "O arquivo selecionado não existe.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					BufferedReader buffer = null;
					try {
						try {
							buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
						} 
						catch (UnsupportedEncodingException e2) {
							e2.printStackTrace();
						}
						try {
							String curWord;
							int i, j;
							
							while (buffer.ready()) {
								curWord = buffer.readLine().trim();
								for (i = 0; i < texts.length; i++) {
									if (!texts[i].isSelected()) {    //Seleciona apenas os textos ainda não selecionados.
										for (j = 0; j < texts[i].size(); j++) {    //Procurando pela palavra 'curWord' no texto 'i'.
											if (texts[i].getWord(j).equals(curWord)) {
												texts[i].setSelected(true);
												break;
											}
										}
									}
								}
							}
							
							buffer.close();
						} 
						catch (IOException e1) {
							e1.printStackTrace();
						}
					} 
					catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panelMenu.add(selectTextsButton);
	}
	
	
	public void createNewVisButton()
	{
		newVisButton.setToolTipText("Gerar visualização da seleção em uma nova janela");
		newVisButton.setIcon(new ImageIcon(getClass().getResource("/icons/share_icon&16.png")));
		newVisButton.setBounds(344, 3, 28, 28);
		newVisButton.setBackground(new Color(25, 25, 112));
		newVisButton.setVisible(true);
		newVisButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Thread thread = new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						processCanceled = false;
						VisualProgress pr = new VisualProgress(textViewer, false);    //false: não limpa essa tela se o processamento for cancelado.
						ArrayList<Integer> indexes = new ArrayList<Integer>();
						
						for (int i = 0; i < texts.length && !processCanceled; i++) {    //Identificando os textos selecionados.
							if (texts[i].isSelected()) indexes.add(i);
						}
						
						if (processCanceled) return;
						
						if (indexes.size() == 0) {
							JOptionPane.showMessageDialog(null, "Selecione no mínimo um texto\npara utilizar esse recurso.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						
						TextBase[] selectedTexts = new TextBase[indexes.size()];
						
						for (int i = 0; i < indexes.size() && !processCanceled; i++) {    //Isolando os textos selecionados.
							selectedTexts[i] = textsBase[indexes.get(i)];
						}
						
						if (processCanceled) return;
						
						try {
							TextViewer newTextViewer = new TextViewer(principal, selectedTexts, frequency);
							
							if (!newTextViewer.isCanceled()) principal.addPanel(newTextViewer);
						} 
						catch (CloneNotSupportedException | IOException e1) {
							e1.printStackTrace();
						} 
						catch (BadLocationException e1) {
							e1.printStackTrace();
						}
						pr.close();
					}
				});
				thread.start();
			}
		});
		panelMenu.add(newVisButton);
	}

	
	public void createZoomOutButton()
	{
		zoomOutButton.setToolTipText("Diminuir zoom");
		zoomOutButton.setIcon(new ImageIcon(getClass().getResource("/icons/round_minus_icon&16.png")));
		zoomOutButton.setBounds(375, 3, 28, 28);
		zoomOutButton.setBackground(new Color(25, 25, 112));
		zoomOutButton.setVisible(true);
		zoomOutButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				zoomPanel.zoomOut();
				SwingUtilities.invokeLater(new Runnable() 
				{  
					public void run()
					{  
						zoomPanel.repaint();  
	                }  
	            });  	
				window.revalidate();
				window.repaint();
			}
		});
		panelMenu.add(zoomOutButton);
	}
	
	
	public void createZoomInButton()
	{
		zoomInButton.setToolTipText("Aumentar zoom");
		zoomInButton.setIcon(new ImageIcon(getClass().getResource("/icons/round_plus_icon&16.png")));
		zoomInButton.setBounds(406, 3, 28, 28);
		zoomInButton.setBackground(new Color(25, 25, 112));
		zoomInButton.setVisible(true);
		zoomInButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				zoomPanel.zoomIn();
				SwingUtilities.invokeLater(new Runnable() 
				{  
					public void run()
					{  
						zoomPanel.repaint();  
	                }  
	            });
				window.revalidate();
				window.repaint();
			}
		});
		panelMenu.add(zoomInButton);
	}

	
	public void close()
	{
		textsHighlight.setVisible(false);
		config.setVisible(false);
		window.setVisible(false);
		principal.removePanel(id-1);
	}
	
	
	public Text[] getTexts()
	{
		return(texts);
	}
	
	
	//Calendar: classe abstrata da classe Date, que não pode ser instanciada diretamente.
	public java.util.Date sumDays(java.util.Date lowerLimDate, int days)
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(lowerLimDate);
		newDate.add(GregorianCalendar.DAY_OF_MONTH, days);
		return((java.util.Date) newDate.getTime());
	}
	
	
	public java.util.Date sumMonths(java.util.Date date, int months)
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.add(GregorianCalendar.MONTH, months);
		return((java.util.Date) newDate.getTime());
	}
	
	
	public java.util.Date sumYears(java.util.Date date, int years)
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.add(GregorianCalendar.YEAR, years);
		return((java.util.Date) newDate.getTime());

	}
	
	
	@SuppressWarnings("deprecation")
	public String convertDate(java.util.Date date)
	{
		return(date.getDate()+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900));
	}
	
	
	public String toTime(int minutes)
	{
		return(minutes/60+":"+minutes%60);
	}
	
	
	private void sortTime(ArrayList<Text> group)
	{
		boolean swap = true;
		int lim = group.size()-1;
		Text aux;
		
		while (swap) {
			swap = false;
			for (int idx = 0; idx < lim; idx++) {
				if (group.get(idx).getMinutes() > group.get(idx+1).getMinutes()) {
					aux = group.get(idx);
					group.set(idx, group.get(idx+1));
					group.set(idx+1, aux);
					swap = true;
				}
			}
		}
	}

	
	private JTextField timeInterval(String interval, int x, int y, Color color, boolean opaque)
	{
		JTextField timeInterval = new JTextField();
		
		timeInterval.setBorder(null);
		timeInterval.setBounds(x, y, 85, 12);
		timeInterval.setText(interval);
		timeInterval.setEditable(false);
		timeInterval.setHorizontalAlignment(JTextField.CENTER);
		timeInterval.setBackground(color);
		timeInterval.setForeground(Color.BLACK);
		timeInterval.setOpaque(opaque);
		return(timeInterval);
	}
	
	
	@SuppressWarnings("deprecation")
	private java.util.Date getMinDay(java.util.Date date)    //Obtendo o primeiro dia do mês: EU SEI, É DESNECESSÁRIO, MAS DEIXA O CÓDIGO PADRONIZADO!
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.set(date.getYear()+1900, date.getMonth(), newDate.getActualMinimum(Calendar.DAY_OF_MONTH));
		return((java.util.Date) newDate.getTime());
	}
	
	
	@SuppressWarnings("deprecation")
	private java.util.Date getMaxDay(java.util.Date date)    //Obtendo o último dia do mês.
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.set(date.getYear()+1900, date.getMonth(), newDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		return((java.util.Date) newDate.getTime());
	}
	
	
	@SuppressWarnings("deprecation")
	private java.util.Date getMinMonth(java.util.Date date)    //Obtendo o primeiro mês do ano: EU SEI, É DESNECESSÁRIO, MAS DEIXA O CÓDIGO PADRONIZADO!
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.set(date.getYear()+1900, newDate.getActualMinimum(Calendar.MONTH), date.getDate());
		return((java.util.Date) newDate.getTime());
	}
	
	
	@SuppressWarnings("deprecation")
	private java.util.Date getMaxMonth(java.util.Date date)    //Obtendo o último mês do ano: EU SEI, É DESNECESSÁRIO, MAS DEIXA O CÓDIGO PADRONIZADO!
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.set(date.getYear()+1900, newDate.getActualMaximum(Calendar.MONTH), date.getDate());
		return((java.util.Date) newDate.getTime());
	}

	
	private boolean isNumber(String str)
	{
		try {
			Integer.parseInt(str);
		}
		catch (NumberFormatException nfe) {
			return(false);
		}
		return(true);
	}
	
	
	private void createDateLabel()
	{
		dateLabel.setBounds(527, 3, 170, 28);
		dateLabel.setEditable(false);
		dateLabel.setToolTipText("Agrupamento de dias");
		dateLabel.setHorizontalAlignment(JTextField.CENTER);
		panelMenu.add(dateLabel);
	}
	
	
	private void createScheduleLabel()
	{
		scheduleLabel.setBounds(700, 3, 97, 28);
		scheduleLabel.setEditable(false);
		scheduleLabel.setToolTipText("Agrupamento de minutos");
		scheduleLabel.setHorizontalAlignment(JTextField.CENTER);
		panelMenu.add(scheduleLabel);
	}
	
	
	public void setDateLabel(String date)
	{
		dateLabel.setText(date);
	}
	
	
	public void setScheduleLabel(String schedule)
	{
		scheduleLabel.setText(schedule);
	}
	
	
	public void resetFrequencies(ArrayList<ArrayList<Object>> words)
	{
		for (int idxLin = 0; idxLin < words.size(); idxLin++) {    //Percorrendo as tuplas das palavras: lista (palavra, cor, frequencia).
			words.get(idxLin).set(2, 0);    //Zerando o campo de frequencia, de índice 2.
		}
	}
	
	
	public void increaseFreq(ArrayList<ArrayList<Object>> words, String word)
	{
		for (int idxLin = 0; idxLin < words.size(); idxLin++) {
			if ( word.equals(String.valueOf(words.get(idxLin).get(0))) ) {
				words.get(idxLin).set(2, (((Integer) words.get(idxLin).get(2))+1));    //Incrementando a frequência.
				return;
			}
		}
	}

	
	//Retorna uma cor baseada em um intervalo de horário:
	private Color getColorByHour(int minutes)
	{
		Color color = null;
		
		switch (minutes) {
			case 360: color = new Color(173, 234, 234); break;    //Amanhecendo, sol nascendo (0h - 6h).
			case 720: color = new Color(255, 255, 0); break;    //Intensidade máxima de sol (6h - 12h).
			case 1080: color = new Color(255, 150, 0); break;    //Entardecendo, intensidade de sol diminuindo (12h - 18h).
			case 1440: color = new Color(51, 50, 58);    //Noite, sem sol (18h - 24h).
		}
		
		return(color);
	}
	
	
	public Integer searchWord(String word)
	{
		if (selectedWords == null) return(-1);
		for (int i = 0; i < selectedWords.size(); i++) {
			if (((String) selectedWords.get(i)[0]).equals(word)) return(i);    //Palavra já existe.
		}
		return(-1);
	}
	
	
	public void updateWordColor(int idx, Color color) throws BadLocationException
	{
		Object[] updateWord = new Object[2];
			
		updateWord[0] = selectedWords.get(idx)[0];
		updateWord[1] = color;
		selectedWords.set(idx, updateWord);
		textsHighlight.updateWords(selectedWords);
	}
	
	
	public TextMultiple getTextMultiple()
	{
		return(textsHighlight);
	}

	
	public void removeAllSelections()
	{
		for (int i = 0; i < texts.length; i++) {
			texts[i].setSelected(false);
			selectedWords = null;    //Removendo a seleção.
		}
		selWords = false;	
	}
	
	
	public boolean isSelWords()
	{
		return(selWords);
	}
	
	
	public boolean showFrequencies()
	{
		return(config.isGroup());
	}
	
	
	public void cancelProcess(boolean clear)
	{
		processCanceled = true;
		if (clear) {
			zoomPanel.restore();    //Restaurando a escala de zoom original.
			zoomPanel.removeAll();    //Limpando visualização anterior.
			zoomPanel.revalidate();
			zoomPanel.repaint();
			labels = new ArrayList<JTextField>();
		}
	}
	
	
	public boolean isCanceled()
	{
		return(processCanceled);
	}

	
}