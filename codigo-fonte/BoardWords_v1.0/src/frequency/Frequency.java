/*
Frequency

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

package frequency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultRowSorter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import visualization.TextViewer;
import data.Text;
import data.TextBase;
import data.Word;
import data.WordBase;



public class Frequency
{

	
	private JFrame frame = new JFrame("Frequência dos startwords");
	private JPanel searchPanel = new JPanel();
	private JPanel colorPanel = new JPanel();
	private JTextField searchText = new JTextField();
	private JButton searchButton = new JButton();
	private JButton colorStartwordsButton = new JButton();
	private JButton colorStopwordsButton = new JButton();
	private MyTableModel tableModel = new MyTableModel();
	private JTable wordsTable = new JTable(tableModel);
	private JScrollPane wordsScroll = new JScrollPane(wordsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private ArrayList<ArrayList<String>> startwords = null;	
	private Color colorStartwords = Color.GRAY;
	private Color colorStopwords = Color.GRAY;
	private ArrayList<TextViewer> panels;
	private TextBase[] basicTexts;
	private ColorEditor colorEditor;
	private int numTexts;
	
	
	public Frequency() 
	{
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(400, 600));
		frame.setLocationRelativeTo(null);    //Abrir janela no meio da tela.
		searchPanel.setBounds(0, 0, 400, 40);
		searchPanel.setBackground(new Color(58, 95, 205));
		searchPanel.setVisible(true);
		colorPanel.setBounds(0, 40, 400, 30);
		colorPanel.setBackground(new Color(58, 95, 205));
		colorPanel.setVisible(true);
		wordsTable.setBackground(Color.WHITE);
		wordsTable.setAutoCreateRowSorter(true);    //Ordenar ao clicar nos índices das colunas.
		wordsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);    //Redimensionamento da tabela (habilita scroll horizontal).
		wordsTable.setVisible(true);
		wordsTable.getTableHeader().setReorderingAllowed(false);    //Bloqueio do reposicionamento das colunas da tabela.
		wordsTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
		wordsScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		createSearchText();
		createSearchButton();
		createColorStartwordsButton();
		createColorStopwordsButton();
		frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
		frame.getContentPane().add(colorPanel, BorderLayout.CENTER);
		frame.getContentPane().add(wordsScroll, BorderLayout.SOUTH);
		frame.setVisible(false);
	}
	
	
	public void setVisible(boolean choice)
	{
		frame.setVisible(choice);
	}
	
	
	public void createSearchText()
	{
		searchText.setPreferredSize(new Dimension(150, 30));
		searchText.setVisible(true);
		searchPanel.add(searchText);
	}
	
	
	public void createSearchButton()
	{
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		wordsTable.setRowSorter(sorter);
		searchButton.setToolTipText("Procurar startword");
		searchButton.setPreferredSize(new Dimension(60, 30));
		searchButton.setBackground(new Color(25, 25, 112));
		searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/zoom_icon&16.png")));
		searchButton.setVisible(true);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = searchText.getText();
				sorter.setRowFilter(RowFilter.regexFilter(text));    //Adicionando o filtro à tabela.
		    }
		});
		searchPanel.add(searchButton);
	}
	
	
	public void createColorStartwordsButton()
	{
		colorStartwordsButton.setToolTipText("Alterar cor dos startwords");
		colorStartwordsButton.setPreferredSize(new Dimension(100, 40));
		colorStartwordsButton.setBackground(new Color(25, 25, 112));
		colorStartwordsButton.setIcon(new ImageIcon(getClass().getResource("/icons/hand_pro_icon&24.png")));
		colorStartwordsButton.setVisible(true);
		colorStartwordsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(null, "Cor dos startwords", colorStartwords);
				if (newColor != colorStartwords) {
					//Atualizando as cores das palavras:
					colorStartwords = newColor;
					
					for (int idxPnl = 0; idxPnl < panels.size(); idxPnl++) {    //Atualizando as cores das palavras em todos os painéis.
						if (panels.get(idxPnl) != null) {    //Verificando se o painél já foi removido (janela de visualização fechada).
							Text[] texts = panels.get(idxPnl).getTexts();    //Obtendo os textos de cada painél para alterar a cor das palavras.
							
							for (int idxTxt = 0; idxTxt < texts.length; idxTxt++) {    //Percorrendo todos os textos de cada painél.
								ArrayList<Word> text = texts[idxTxt].getText();
								for (int idxWrd = 0; idxWrd < text.size(); idxWrd++) {    //Percorrendo as palavras do texto.
									if (text.get(idxWrd).isStartword()) text.get(idxWrd).setColor(colorStartwords);
								}
								/* Atualizando os painéis: */
								texts[idxTxt].getTextBoard().revalidate();
								texts[idxTxt].getTextBoard().repaint();
							}
						}
					}
					
					for (int idxTxt = 0; idxTxt < basicTexts.length; idxTxt++) {    //Atualizando as cores da estrutura de textos.
						ArrayList<WordBase> curText = basicTexts[idxTxt].getText();
						for (int idxWrd = 0; idxWrd < curText.size(); idxWrd++) {    //Percorrendo as palavras de cada texto.
							if (curText.get(idxWrd).startword) curText.get(idxWrd).setColor(colorStartwords);
						}
					}
					
					//Atualizando a cor da lista de frequências das startwords:
					tableModel.setColor(colorStartwords);
					wordsScroll.revalidate();
					wordsScroll.repaint();
				}
			}
		});
		colorPanel.add(colorStartwordsButton);
	}
	
	
	public void createColorStopwordsButton()
	{
		colorStopwordsButton.setToolTipText("Alterar cor dos stopwords");
		colorStopwordsButton.setPreferredSize(new Dimension(100, 40));
		colorStopwordsButton.setBackground(new Color(25, 25, 112));
		colorStopwordsButton.setIcon(new ImageIcon(getClass().getResource("/icons/hand_contra_icon&24.png")));
		colorStopwordsButton.setVisible(true);
		colorStopwordsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(null, "Cor dos stopwords", colorStopwords);
				if (newColor != colorStopwords) {
					//Atualizando as cores das palavras:
					colorStopwords = newColor;
					
					for (int idxPnl = 0; idxPnl < panels.size(); idxPnl++) {    //Atualizando as cores das palavras em todos os painéis.
						if (panels.get(idxPnl) != null) {    //Verificando se o painél já foi removido (janela de visualização fechada).
							Text[] texts = panels.get(idxPnl).getTexts();    //Obtendo os textos de cada painél para alterar a cor das palavras.
							
							for (int idxTxt = 0; idxTxt < texts.length; idxTxt++) {    //Percorrendo todos os textos de cada painél.
								ArrayList<Word> text = texts[idxTxt].getText();
								for (int idxWrd = 0; idxWrd < text.size(); idxWrd++) {    //Percorrendo as palavras do texto.
									if (!text.get(idxWrd).isStartword()) text.get(idxWrd).setColor(colorStopwords);
								}
								/* Atualizando os painéis: */
								texts[idxTxt].getTextBoard().revalidate();
								texts[idxTxt].getTextBoard().repaint();
							}
						}
					}
					
					for (int idxTxt = 0; idxTxt < basicTexts.length; idxTxt++) {    //Atualizando as cores da estrutura de textos.
						ArrayList<WordBase> curText = basicTexts[idxTxt].getText();
						for (int idxWrd = 0; idxWrd < curText.size(); idxWrd++) {    //Percorrendo as palavras de cada texto.
							if (!curText.get(idxWrd).startword) curText.get(idxWrd).setColor(colorStopwords);
						}
					}
					
					//Atualizando a cor da lista de frequências das startwords:
					tableModel.setColor(colorStartwords);
					wordsScroll.revalidate();
					wordsScroll.repaint();
				}
			}
		});
		colorPanel.add(colorStopwordsButton);
	}	
	
	
	public void removeStartwords()
	{
		startwords = new ArrayList<ArrayList<String>>();
	}
	
	
	public void setPanels(ArrayList<TextViewer> panels)
	{
		this.panels = panels;    //Cópia das janelas de visualização, para poder atualizar as cores das palavras.
		this.colorEditor = new ColorEditor(wordsTable, panels, basicTexts);
		wordsTable.setDefaultEditor(Color.class, this.colorEditor);    //Passando as referências dos 'panels' para atualizar as cores das palavras.
	}
	
	
	public void setTexts(TextBase[] texts)
	{
		this.basicTexts = texts;    //Textos de base para controle das cores das palavras.
		colorEditor.setTexts(texts);
	}
	
	
	public Color getColorStartwords()
	{
		return(colorStartwords);
	}
	
	
	public Color getColorStopwords()
	{
		return(colorStopwords);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addStartwords(String startword, int idxCurFile)
	{
		int idxWord = searchWord(startword);
		
		//Se a palavra já foi inserida no dicionário de startwords, incrementamos sua frequência; senão, ela é inserida:
		if (idxWord >= 0) {
			Integer newFreq = Integer.parseInt(String.valueOf(startwords.get(idxWord).get(1)))+1;
			startwords.get(idxWord).set(1, new String(newFreq.toString())); 
			if (idxCurFile != Integer.parseInt(String.valueOf(startwords.get(idxWord).get(3)))) {    //A palavra ocorreu em mais um arquivo diferente.
				Integer newOcu = Integer.parseInt(String.valueOf(startwords.get(idxWord).get(2)))+1;
				startwords.get(idxWord).set(2, new String(newOcu.toString()));
				startwords.get(idxWord).set(3, new String(String.valueOf(idxCurFile)));    //Atualizando o último arquivo em que a palavra ocorreu.
			}
		}
		else {    //Inserindo nova startword.
			ArrayList newWord = new ArrayList();
			newWord.add(startword);
			newWord.add(1);    //Frequência da palavra.
			newWord.add(1);    //Por enquanto, houve ocorrência apenas no arquivo atual ('idxCurFile').
			newWord.add(idxCurFile);    //Último arquivo em que a palavra ocorreu.
			startwords.add(newWord);
		}
	}

	
	public int searchWord(String word)
	{
		int idxWord;
		
		if (startwords != null) {
			for (idxWord = 0; idxWord < startwords.size(); idxWord++) {
				//Se a palavra já tiver sido inserida, incrementamos sua frequência:
				if ( word.equals(String.valueOf(startwords.get(idxWord).get(0))) ) return(idxWord);
			}
		}
		return(-1);
	}
	
	
	public void listStartwords(TextBase[] texts)
	{
		Object[][] table = new Object[startwords.size()][5];
		for (int idxWord = 0; idxWord < startwords.size(); idxWord++) {
			table[idxWord][0] = startwords.get(idxWord).get(0);
			table[idxWord][1] = colorStartwords;
			table[idxWord][2] = Integer.valueOf(String.valueOf(startwords.get(idxWord).get(1)));
			table[idxWord][3] = 1.0/Double.valueOf(String.valueOf(startwords.get(idxWord).get(1)));
			table[idxWord][4] = (Double.valueOf(String.valueOf(startwords.get(idxWord).get(2)))/numTexts)*100.0;
			
			for (int i = 0; i < texts.length; i++) {    //Necessário para calcular a frequência inversa.
				for (WordBase w : texts[i].getText()) {
					if (w.getWord().equals(startwords.get(idxWord).get(0))) w.setInverseFrequency(w.getAbsoluteFrequency()/Float.valueOf(String.valueOf(startwords.get(idxWord).get(2))));
				}	
			}
		}
		wordsTable.removeAll();
		tableModel.setData(table);
		tableModel.setColor(colorStartwords);
		wordsScroll.revalidate();
		wordsScroll.repaint();
	}
	
	
	public JTable getTable()
	{
		return(wordsTable);
	}
	
	
	@SuppressWarnings("unchecked")
	public void reloadTable()
	{
		((DefaultRowSorter<TableModel, Integer>) wordsTable.getRowSorter()).setRowFilter(RowFilter.regexFilter(""));
	}	
	
	
	public void setNumTexts(int num)    //Quantidade de textos. Necessário, pois os textos são adicionados dinamicamente e é preciso fazer o cálculo de ocorrência das palavras (em quantos textos elas aparecem).
	{
		numTexts = num;
	}

	
	@SuppressWarnings("serial")
	class MyTableModel extends AbstractTableModel
	{
		
		
        private String[] columnNames = {"Startword", "Cor", "Frequência", "Frequência inversa", "Abrangência (%)"};
        private Color color = Color.WHITE;
        private Object[][] data = {{"", color, new Integer(0), new Double(0.0), new Double(0.0)}};

        
        public int getColumnCount()
        {
            return columnNames.length;
        }

        
        public int getRowCount()
        {	
            return data.length;
        }

        
        public String getColumnName(int col)
        {
            return columnNames[col];
        }

        
        public Object getValueAt(int row, int col)
        {
            return data[row][col];
        }

        
        public void setValueAt(Object value, int row, int col)
        {
            data[row][col] = value;
        }
        
        
        public boolean isCellEditable(int row, int col)
        {
            if (col == 1) return(true);
            return(false);
        }
        
        
        public void setData(Object[][] data)
        {
        	this.data = data;
        }
        
        
        public void setColor(Color color)
        {
        	this.color = color;
        	
        	//Atualizando a cor das startwords na lista:
        	for (int idx = 0; idx < data.length; idx++) {
        		data[idx][1] = this.color;
        	}
        }
        
        
        //Ordenação relativa, baseada no tipo do objeto:
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            switch(columnIndex){    
            	case 0: return(String.class);
            	case 1: return(Color.class);
            	case 2: return(Integer.class);
                default: return(Double.class);
            }    
        }  
    }
	
	
}