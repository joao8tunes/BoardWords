/*
Text

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

package data;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextField;



public class Text
{


	private String textFile;    //Arquivo de texto na íntegra.
	private String fileName;    //Nome do arquivo do post.
	private ArrayList<Word> text = new ArrayList<Word>();    //Texto processado do arquivo filePath.
	private JTextField time = new JTextField();
	private JTextField date = new JTextField();
	private JTextField board = new JTextField();    //JTextField que contém as palavras do texto (evita reposicionamentos desnecessários de todos os pixels das palavras).
	private int minutes;

	
	public Text()
	{	
		board.setBackground(new Color(58, 95, 205));    //Cor padrão de seleção.
		board.setBorder(null);
	}
	
	
	public void setVisibleCap(boolean leg)
	{
		time.setOpaque(leg);
	}
	
	
	public String getFileName()
	{
		return(fileName);
	}
	
	
	public String getWord(int idx)
	{
		return(text.get(idx).getWord());
	}
	
	public JTextField getPixel(int idx)
	{
		return(text.get(idx).getPix());
	}
	
	
	public void setWordOpaque(int idx, boolean b)
	{
		text.get(idx).setOpaque(b);
	}
	
	
	public int size()
	{
		return(getText().size());
	}
	
	
	public JTextField getTextBoard()
	{
		return(board);
	}


	public void setFileName(String file) 
	{
		this.fileName = file;
	}


	public ArrayList<Word> getText() 
	{
		return(text);
	}


	public void setText(ArrayList<Word> text) 
	{
		this.text = text;
	}


	public JTextField getTime() 
	{
		return(time);
	}

	
	public void setPosition(int x, int y)
	{
		time.setBounds(x, y, 50, 12);
		board.setBounds(74, y+2, board.getWidth(), board.getHeight());
		time.setBorder(null);
	}
	

	@SuppressWarnings("static-access")
	public void setTime(String time) 
	{
		String[] hour = time.split(":");
		
		this.time.setText(time);
		this.time.setEditable(false);
		this.time.setHorizontalAlignment(this.time.CENTER);
		this.time.setBackground(Color.WHITE);
		this.time.setForeground(Color.BLACK);
		minutes = Integer.parseInt(hour[0])*60 + Integer.parseInt(hour[1]);    //Transformando o horário de criação em minutos.
	}
	
	
	public void setDate(String date)
	{
		this.date.setName(date);
		time.setToolTipText(date);
	}
	
	
	public String getDate()
	{
		return(this.date.getName());
	}
	
	
	public void setWord(Word newWord)
	{
		text.add(newWord);
		board.add(newWord.getPix());
	}
	
	
	public void setPix(JTextField newPix)
	{
		board.add(newPix);
	}
	
	
	public int getMinutes()
	{
		return(minutes);
	}
	
	
	
	public int getTextHeight()
	{
		return(board.getHeight());
	}
	
	
	
	public int getTextWidth()
	{
		return(board.getWidth());
	}
	
	
	public void setTextFile(String textFile)
	{
		this.textFile = textFile;
	}
	
	
	public String getTextFile()
	{
		return(textFile);
	}

	
	public void setSelected(boolean b)
	{
		for (Word w : text) {    //Selecionando o texto: deixando as palavras invisíveis, destacando a cor de fundo da seleção do texto.
			w.setOpaque(!b);
		}
		board.revalidate();
		board.repaint();
	}


	public void setSelectionColor(Color color)
	{
		board.setBackground(color);
	}
	
	
	public boolean isSelected()
	{
		return(!text.get(0).isOpaque());
	}

	
	public void setTimeColor(Color color, boolean b)
	{
		time.setBackground(color);
		time.setOpaque(b);
	}


}