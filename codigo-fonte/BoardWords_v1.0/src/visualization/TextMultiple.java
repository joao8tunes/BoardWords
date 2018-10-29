/*
TextMultiple

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import data.Text;



public class TextMultiple
{


	private JFrame frame = new JFrame();
	private JTextPane textPane = new JTextPane();
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane(textPane);
	private String texts;
	
	
	public TextMultiple(int idx, Text[] list) throws IOException, BadLocationException
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setTitle("Textos "+idx);
		panel.setBackground(new Color(58, 95, 205));
		panel.setBounds(0, 0, 550, 500);
		frame.setLayout(null);
		panel.setLayout(null);
		frame.setResizable(false);
		textPane.setEditable(false);
		textPane.setBackground(Color.BLACK);
		textPane.setForeground(Color.WHITE);
		frame.setMinimumSize(new Dimension(550, 500));
		frame.setLocationRelativeTo(null);
		scroll.setBounds(10, 10, 530, 450);
		scroll.setBorder(null);
		panel.add(scroll, BorderLayout.NORTH);
		panel.setVisible(true);
		scroll.setVisible(true);
		textPane.setVisible(true);
		frame.add(panel);
		frame.setVisible(false);
		setTexts(list);    //Imprimindo os textos na janela de visualização.
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				frame.setVisible(false);
			}
		});
	}

	
	public void setTexts(Text[] list) throws BadLocationException
	{
		texts = new String();
		for (Text t : list) {
			texts += "### "+t.getFileName()+" ###\n";    //Nome do arquivo de texto.
			texts += t.getTextFile()+"\n\n\n";    //Texto na íntegra.
		}
		textPane.setText(texts);
		textPane.addMouseListener(new MouseListener() 
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
				if (e.getClickCount() == 2) unselectWords();
			}
		});
	}
	
	
	public void setVisible(boolean b)
	{
		frame.setVisible(b);
	}
	
	
	public void setBackground(Color color)
	{
		textPane.setBackground(color);
	}
	
	
	public void setForeground(Color color)
	{
		textPane.setForeground(color);
	}


	public Color getForeground()
	{
		return(textPane.getForeground());
	}

	
	public void unselectWords()
	{
		textPane.getHighlighter().removeAllHighlights();
		textPane.revalidate();
		textPane.repaint();
	}
	
	
	public void selectWords(ArrayList<Object[]> words) throws BadLocationException
	{
		Pattern p;
		Matcher m;
				
		for (int i = 0; i < words.size(); i++) {    //Percorrendo as palavras selecionadas.
			p = Pattern.compile(" "+(String) words.get(i)[0]+" ");
			m = p.matcher(textPane.getText().toLowerCase());
			while (m.find()) {    //m.start()+1, m.end()-1: estrangulamento da palavra; desconsiderando os espaços acrescentados na busca.
	        	textPane.getHighlighter().addHighlight(m.start()+1, m.end()-1, new DefaultHighlighter.DefaultHighlightPainter((Color) words.get(i)[1]));
	        }
		}
		textPane.revalidate();
		textPane.repaint();
	}

	
	public void updateWords(ArrayList<Object[]> words) throws BadLocationException
	{
		Pattern p;
		Matcher m;
	
		unselectWords();
		
		for (int i = 0; i < words.size(); i++) {    //Percorrendo as palavras selecionadas.
			p = Pattern.compile(" "+(String) words.get(i)[0]+" ");
			m = p.matcher(textPane.getText().toLowerCase());
			while (m.find()) {    //m.start()+1, m.end()-1: estrangulamento da palavra; desconsiderando os espaços acrescentados na busca.
	        	textPane.getHighlighter().addHighlight(m.start()+1, m.end()-1, new DefaultHighlighter.DefaultHighlightPainter((Color) words.get(i)[1]));
	        }
		}
		textPane.revalidate();
		textPane.repaint();
	}
	
	
}