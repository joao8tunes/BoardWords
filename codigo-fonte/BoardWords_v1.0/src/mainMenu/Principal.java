/*
Principal

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

package mainMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import visualization.CalendarViewer;
import visualization.TextViewer;
import data.TextBase;
import frequency.Frequency;



public class Principal
{

	
	private JFrame mainframe = new JFrame("BoardWords");    //Janela principal que contém todos os elementos de UI.
	private JPanel panelMenu = new JPanel();    //Painel do menu principal de funcionalidades.
	private JButton dataButton = new JButton("Textos");
	private JButton stopwordsButton = new JButton("Stopwords");
	private JButton processButton = new JButton("Processar");
	private JButton colorButton = new JButton("Palavras");
	private JButton calButton = new JButton("Calendário");
	private JButton panelButton = new JButton("Painel");
	private JButton saveButton = new JButton("Salvar");
	private JButton editorButton = new JButton("Editor");
	private JButton helpButton = new JButton("Ajuda");
	private String filesPath = "";
	private String stopwordsPath = "";
	private Help helpWindow = new Help();
	private Integer visIndex = 1;    //Índice simbólico para diferenciar as janelas de visualização.
	private TextBase[] texts;    //Vetor de objetos TextBase (estrutura principal do modo Texto).
	private ArrayList<TextViewer> panels = new ArrayList<TextViewer>();
	private Frequency freqWindow = new Frequency();    //Janela de frequẽncias e alteração da cor das palavras.
	private CalendarViewer calWindow = new CalendarViewer(this);
	private Principal p = this;
	private ArrayList<TextEditor> textEditors = new ArrayList<TextEditor>();    //Necessário para evitar o fechamento de arquivos que ainda não foram salvos.
	private boolean processCanceled = false;
	private boolean complete = false;    //Para verificar se os dados já foram processados e habilitar as funcionalidades dependentes dos dados.
	
	
	public Principal()
	{
		mainframe.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		mainframe.setResizable(false);	
		mainframe.setMinimumSize(new Dimension(1060, 104));
		mainframe.setLocationRelativeTo(null);    //Abrir janela no meio da tela.
		
		//Adicionando as estruturas principais ao JFrame principal:
		mainframe.getContentPane().add(panelMenu, BorderLayout.WEST);
        mainframe.pack();
		mainframe.setVisible(true);	
		mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainframe.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				boolean close = true;
				
				for (TextEditor t : textEditors) {
					if (t != null) {
						close = false;
						break;
					}
				}
				
				if (!close) {
					Object[] options = {"Yes", "No"};
					
					if (JOptionPane.showOptionDialog(null, "Há dados em processamento. Deseja\nsair do programa mesmo assim?", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == JOptionPane.YES_NO_OPTION) {
						System.exit(0);
					}
				}
				else if (texts != null) {
					Object[] options = {"Yes", "No"};
					
					if (JOptionPane.showOptionDialog(null, "Há dados em processamento. Deseja\nsair do programa mesmo assim?", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == JOptionPane.YES_NO_OPTION) {
						System.exit(0);
					}
				}
				else System.exit(0);
			}
		});
		setPanel();
	}
	
	
	private void setPanel()
	{
		//Adicionando os componentes ao menu principal com suas respectivas funcionalidades:
		panelMenu.setPreferredSize(new Dimension(1058, 104));
		panelMenu.setBackground(new Color(58, 95, 205));
		panelMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));  
		panelMenu.setVisible(true);
		createDataButton();
		createStopwordsButton();
		createProcessButton();
		createColorButton();
		createCalButton();
		createPanelButton();
		createSaveButton();
		createEditorButton();
		createHelpButton();
		panelMenu.revalidate();
		panelMenu.repaint();
	}
	
	
	private void createDataButton()
	{
		dataButton.setToolTipText("Selecionar diretório dos arquivos de entrada");
		dataButton.setIcon(new ImageIcon(getClass().getResource("/icons/db_icon&32.png")));
		dataButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		dataButton.setHorizontalTextPosition(SwingConstants.CENTER);
		dataButton.setLocation(50, 50);
		dataButton.setPreferredSize(new Dimension(111, 60));
		dataButton.setBackground(new Color(25, 25, 112));
		dataButton.setForeground(Color.WHITE);
		dataButton.setVisible(true);
		dataButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Selecionar diretório dos arquivos de entrada");
				path.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					filesPath = path.getSelectedFile().getAbsolutePath();
				}
			}
		});
		panelMenu.add(dataButton);
	}
	
	
	private void createStopwordsButton()
	{
		stopwordsButton.setToolTipText("Selecionar arquivo de stopwords");
		stopwordsButton.setIcon(new ImageIcon(getClass().getResource("/icons/hand_contra_icon&32.png")));
		stopwordsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		stopwordsButton.setHorizontalTextPosition(SwingConstants.CENTER);
		stopwordsButton.setLocation(50, 100);
		stopwordsButton.setPreferredSize(new Dimension(111, 60));
		stopwordsButton.setBackground(new Color(25, 25, 112));
		stopwordsButton.setForeground(Color.WHITE);
		stopwordsButton.setVisible(true);
		stopwordsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Selecionar arquivo de stopwords");
				path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
				path.setAcceptAllFileFilterUsed(false); 
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					stopwordsPath = path.getSelectedFile().getAbsolutePath();
				}
			}
		});
		panelMenu.add(stopwordsButton);
	}
	
	
	private void createProcessButton()
	{
		processButton.setToolTipText("Processar arquivos de entrada");
		processButton.setIcon(new ImageIcon(getClass().getResource("/icons/fire_icon&32.png")));
		processButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		processButton.setHorizontalTextPosition(SwingConstants.CENTER);
		processButton.setLocation(50, 50);
		processButton.setPreferredSize(new Dimension(111, 60));
		processButton.setBackground(new Color(25, 25, 112));
		processButton.setForeground(Color.WHITE);
		processButton.setVisible(true);
		processButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!isValid()) return;
				
				boolean process = false;
				if (texts != null) {
					if (JOptionPane.showConfirmDialog(null, "Deseja realmente processar os dados;\nos dados atuais serão perdidos?") == JOptionPane.YES_OPTION) process = true;
				}
				else process = true;
				
				if (process) {
					Thread thread1 = new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							MainProgress pr = new MainProgress(p);
							
							complete = false;
							processCanceled = false;    //Restaurando estado de êxito?...
							freqWindow.setVisible(false);
							calWindow.setVisible(false);
							
							for (int idx = 0; idx < panels.size() && !processCanceled; idx++) {
								/* Removendo os paineis da antiga base de dados: */
								if (panels.get(idx) != null)
									try {
										panels.get(idx).close();
									} 
									catch (Throwable e) {
										e.printStackTrace();
									}
							}
							
							if (processCanceled) {
								pr.cancel();
								if (texts != null) complete = true;    //O processo foi cancelado, mas já existem dados processados.
								return;
							}
							
							panels = new ArrayList<TextViewer>();
							visIndex = 1;
							texts = null;    //Removendo a antiga estrutura de dados processados.
							
							final String filenames[] = new File(filesPath).list();    //Identificando e listando os arquivos de entrada.
									
							if (filenames.length == 0 || !(new File(filesPath).exists())) {
								pr.close();
								JOptionPane.showMessageDialog(null, "Certifique-se da existência dos\narquivos de entrada.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
											
							texts = new TextBase[filenames.length];    //Recriando a estrutura de dados processados.
							freqWindow.removeStartwords();    //Removendo a antiga lista de startwords.
							freqWindow.setNumTexts(filenames.length);    //Quantidade de textos processados.
									
							for (int idxFiles = 0; idxFiles < filenames.length && !processCanceled; idxFiles++) {    //Lendo todos os arquivos do diretório de entrada.
								//Filtrando os arquivos de entrada de acordo com as palavras do arquivo 'stopwordsPath':
								try {
									texts[idxFiles] = new TextBase(filenames[idxFiles], filesPath, stopwordsPath, freqWindow, idxFiles);
								} 
								catch (ParseException e1) {
									e1.printStackTrace();
								}
								
								if (!texts[idxFiles].exists()) {    //O arquivo não foi processado corretamente.
									pr.error();
									texts = null;
									freqWindow.removeStartwords();    //Removendo a antiga lista de startwords.
									break;
								}
							}
									
							if (texts != null) {    //Se não deu erro no processamento, continua o processo.
								if (processCanceled) {
									pr.cancel();
									texts = null;
									freqWindow.removeStartwords();    //Removendo a antiga lista de startwords.
									return;
								}
								
								freqWindow.listStartwords(texts);
								freqWindow.setPanels(panels);
								freqWindow.setTexts(texts);    //Controle da estrutura de base dos paineis atuais e dos que ainda serão criados; atualização das cores dos pontos.
								try {
									calWindow.setBounds(getYear(filenames[0]), getYear(filenames[filenames.length-1]));    //Definindo os limites da visualização baseada em calendário.
									for (int i = 0; i < texts.length && !processCanceled; i++) {
										calWindow.setFilesIndexes(texts[i].getDate(), i);    //Atribuindo os índices dos textos aos respectivos dias em que foram criados.
									}
									
									if (processCanceled) {
										pr.cancel();
										texts = null;
										freqWindow.removeStartwords();    //Removendo a antiga lista de startwords.
										return;
									}
									
									calWindow.paintPixels();    //Pintando os pixels do calendário
									complete = true;
								} 
								catch (Throwable e1) {
									e1.printStackTrace();
								}
							}
							else {    //Deu erro no processamento.
								pr.error();
								freqWindow.removeStartwords();    //Removendo a antiga lista de startwords.
							}
							pr.close();
							return;
						}
					});
					
					thread1.start();
					return;
				}
			}
		});
		panelMenu.add(processButton);
	}
	
	
	private void createColorButton()
	{
		colorButton.setToolTipText("Exibir frequências e cores das palavras");
		colorButton.setIcon(new ImageIcon(getClass().getResource("/icons/list_num_icon&32.png")));
		colorButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		colorButton.setHorizontalTextPosition(SwingConstants.CENTER);
		colorButton.setLocation(50, 50);
		colorButton.setPreferredSize(new Dimension(111, 60));
		colorButton.setBackground(new Color(25, 25, 112));
		colorButton.setForeground(Color.WHITE);
		colorButton.setVisible(true);
		colorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (complete) freqWindow.setVisible(true);
				else JOptionPane.showMessageDialog(null, "Processe primeiro os arquivos de entrada.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panelMenu.add(colorButton);
	}
	
	
	private void createCalButton()
	{
		calButton.setToolTipText("Visualizar arquivos baseado em calendário");
		calButton.setIcon(new ImageIcon(getClass().getResource("/icons/calendar_2_icon&32.png")));
		calButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		calButton.setHorizontalTextPosition(SwingConstants.CENTER);
		calButton.setLocation(50, 50);
		calButton.setPreferredSize(new Dimension(111, 60));
		calButton.setBackground(new Color(25, 25, 112));
		calButton.setForeground(Color.WHITE);
		calButton.setVisible(true);
		calButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (complete) calWindow.setVisible(true);
				else JOptionPane.showMessageDialog(null, "Processe primeiro os arquivos de entrada.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panelMenu.add(calButton);
	}
	
	
	public void createPanelButton()
	{
		panelButton.setToolTipText("Gerar visualização de todos os arquivos");
		panelButton.setIcon(new ImageIcon(getClass().getResource("/icons/app_window&32.png")));
		panelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		panelButton.setHorizontalTextPosition(SwingConstants.CENTER);
		panelButton.setLocation(50, 350);
		panelButton.setPreferredSize(new Dimension(111, 60));
		panelButton.setBackground(new Color(25, 25, 112));
		panelButton.setForeground(Color.WHITE);
		panelButton.setVisible(true);
		panelButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (complete) {
					Thread thread = new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							processCanceled = false;
							MainProgress pr = new MainProgress(p);
							
							try {
								TextViewer newTextViewer = new TextViewer(p, texts, freqWindow);
								
								if (!newTextViewer.isCanceled()) panels.add(newTextViewer);    //Adiciona o novo painel somente se ele foi criado sem erros.
							} 
							catch (CloneNotSupportedException | IOException e1) {
								pr.error();
								e1.printStackTrace();
							} 
							catch (BadLocationException e1) {
								pr.error();
								e1.printStackTrace();
							}
							
							pr.close();
						}
					});
					thread.start();
				}
				else JOptionPane.showMessageDialog(null, "Processe primeiro os arquivos de entrada.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panelMenu.add(panelButton);
	}
	
	
	private void createSaveButton()
	{
		saveButton.setToolTipText("Salvar arquivos processados");
		saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/save_icon&32.png")));
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setLocation(50, 300);
		saveButton.setPreferredSize(new Dimension(111, 60));
		saveButton.setBackground(new Color(25, 25, 112));
		saveButton.setForeground(Color.WHITE);
		saveButton.setVisible(true);
		saveButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!isValid()) return;
				
				Thread thread1 = new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						processCanceled = false;
						String dirName = "";
						JFileChooser path = new JFileChooser();
						path.setDialogTitle("Selecionar diretório para salvar os arquivos");
						path.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
							GregorianCalendar calendar = new GregorianCalendar();
							SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
							dirName = path.getSelectedFile().getAbsolutePath()+File.separatorChar+"arquivos_processados_"+fmt.format(calendar.getTime());
							File dir = new File(dirName);
							if (dir.exists()) {
								JOptionPane.showMessageDialog(null, "O diretório\n"+dirName+"\njá existe. Escolha outro diretório.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
							dir.mkdir();
							
							MainProgress pr = new MainProgress(p);
													
							//Salvando os textos filtrados:
							BufferedReader stopwordsFile;
							String filenames[] = new File(filesPath).list();    //Identificando e listando os arquivos de entrada.
							String line[] = null;
							
							if (filenames.length == 0) {
								pr.close();
								JOptionPane.showMessageDialog(null, "Certifique-se da existência dos\narquivos de entrada.", "Erro", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							//Filtrando os arquivos de entrada de acordo com as palavras do arquivo 'stopwordsPath';
							//Os arquivos de entrada são processados e, conforme a filtragem vai sendo feita, uma
							//cópia filtrada de cada arquivo vai sendo criada em um diretório temporário:
							for (int idxFiles = 0; idxFiles < filenames.length && !processCanceled; idxFiles++) {    //Lendo todos os arquivos do diretório de entrada.
								try {
									BufferedReader curFile = new BufferedReader(new InputStreamReader(new FileInputStream(filesPath+File.separatorChar+filenames[idxFiles]), "ISO-8859-1"));
									Writer newFile = new OutputStreamWriter(new FileOutputStream(dirName+File.separatorChar+filenames[idxFiles]));
									
									//Descartando as três primeiras linhas de cada arquivo:
									curFile.readLine();
									curFile.readLine();
									curFile.readLine();
									
									String stopword = null;    //String auxiliar para armazenar o stopword atual (evita outro readLine()).
									while (curFile.ready()) {    //Lendo todas as linhas do arquivo atual.
										line = curFile.readLine().toLowerCase().split(" ");    //Separando todas as palavras da linha.
										for (int idxLine = 0; idxLine < line.length; idxLine++) {    //Lendo cada palavra de cada linha.
											stopwordsFile = new BufferedReader(new FileReader(stopwordsPath));
											
											//Removendo caracteres especiais da string:
											String[] code = {"[", "]", "'", "<", ">", "\\", "|", "/", ".", ",", ";", "̣̣̣̣̣̣̣̣̣̣", "·", "!", "?", "#", "\\$", "%", "\"", "&", "*", "(", ")", "_", "+", "{", "}", "º", "ª", "´", "§", "¬", "¢", "£", "³", "²", "¹"};
											for (int idxCode = 0; idxCode < code.length; idxCode++) 
												line[idxLine] = line[idxLine].replace(code[idxCode], "");
											line[idxLine] = line[idxLine].replaceAll("^\\s+", "");    //Removendo espaços em branco (\\s) no início da string (^), independente da quantidade (+).
											line[idxLine] = line[idxLine].replaceAll("\\s+$", "");    //Removendo espaços em branco (\\s) no fim da string ($), independente da quantidade (+).
											
											while (stopwordsFile.ready()) {    //Verificando se a palavra atual é uma stopword.
												stopword = stopwordsFile.readLine().toLowerCase().trim();
												if (stopword.equals(line[idxLine])) break;
											}
											
											//Se a palavra não for nenhuma stopword, pode ser escrita no novo arquivo:
											if (!stopword.equals(line[idxLine])) newFile.write(line[idxLine]+" ");
											stopwordsFile.close();    //Finalizando leitura do arquivo para voltar ao início.
										}
									}
									curFile.close();    //Fechando o arquivo atual para leitura.
									newFile.close();    //Finalizando a escrita do novo arquivo.
								} 
								catch (IOException excp) {
									pr.error();
									JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+filenames[idxFiles]+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
									break;
								}
							}	
							
							pr.close();
						}
					}
				});
				
				thread1.setDaemon(true);    //Necessário para matar a thread quando o objeto 'thread1' perder referência, i.e., quando finalizar o método desse escopo.
				thread1.start();
			}
		});
		panelMenu.add(saveButton);
	}
		
	
	public void createEditorButton()
	{
		editorButton.setToolTipText("Abrir editor de textos");
		editorButton.setIcon(new ImageIcon(getClass().getResource("/icons/notepad_2_icon&32.png")));
		editorButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		editorButton.setHorizontalTextPosition(SwingConstants.CENTER);
		editorButton.setLocation(50, 350);
		editorButton.setPreferredSize(new Dimension(111, 60));
		editorButton.setBackground(new Color(25, 25, 112));
		editorButton.setForeground(Color.WHITE);
		editorButton.setVisible(true);
		editorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				textEditors.add(new TextEditor());
			}
		});
		panelMenu.add(editorButton);
	}
	

	private void createHelpButton()
	{
		helpButton.setToolTipText("Exibir ajuda e créditos");
		helpButton.setIcon(new ImageIcon(getClass().getResource("/icons/info_icon&32.png")));
		helpButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		helpButton.setHorizontalTextPosition(SwingConstants.CENTER);
		helpButton.setLocation(50, 350);
		helpButton.setPreferredSize(new Dimension(111, 60));
		helpButton.setBackground(new Color(25, 25, 112));
		helpButton.setForeground(Color.WHITE);
		helpButton.setVisible(true);
		helpButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				helpWindow.setVisible(true);
			}
		});
		panelMenu.add(helpButton);
	}
	
	
	public boolean isValid()
	{	
		File file = new File(stopwordsPath), dir = new File(filesPath);
		if (!file.exists() || !dir.exists()) {
			JOptionPane.showMessageDialog(null, "Selecione primeiro o diretório dos\ndados de entrada e o arquivo de stopwords.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			return(false);
		}
		return(true);

	}	
	
	
	private int getYear(String file) 
	{
		String[] elems;
		
		elems = file.split("-");
		elems = elems[2].split("_");
		return(Integer.parseInt(elems[0]));
	}



	public TextBase[] getTexts()
	{
		return(texts);
	}
	
	
	
	public void newPanel(TextBase[] texts)
	{
		try {
			panels.add(new TextViewer(this, texts, freqWindow));
		} catch (CloneNotSupportedException | IOException e1) {
			e1.printStackTrace();
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}


	public void incIndex()
	{
		++visIndex;
	}

	
	public Integer getIndex()
	{
		return(visIndex);
	}

	
	
	public void addPanel(TextViewer newPanel)
	{
		panels.add(newPanel);
	}
	
	
	public void cancelProcess()
	{
		processCanceled = true;
	}

	
	public void removePanel(int id)    //O objeto 'TextViewer' é o "responsável" por sua exclusão.
	{
		panels.set(id, null);    //Se remover com 'remove' tem que tratar os reposicionamentos do array, então só setamos como null. Dá na mesma filho, pelo menos pros fins dese projeto.
	}
	
	
	public boolean isCanceled()
	{
		return(processCanceled);
	}
	
	
	
	/************************************/
	public static void main(String args[])
	{
		/* Visual do Sistema Operacional: */
//		try {       
//			UIManager.setLookAndFeel(  
//	        UIManager.getSystemLookAndFeelClassName());  
//	    }
//		catch(Exception ex) {  
//	    	ex.printStackTrace(System.err);  
//	    }  
		new Principal();
	}
	/************************************/

	
}