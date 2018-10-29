/*
Day

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JTextField;



public class Day 
{
	
	
	private JTextField board = new JTextField();
	private int day, month, year;
	private String dayWeek;
	private boolean selected = false;    //Controle de seleção do dia.
	private Month monthObject;
	private ArrayList<Integer> filesIndexes = new ArrayList<Integer>();    //Controle dos índices dos arquivos que foram criados nesse dia.
	private boolean exists;
	private double percent = 0.0;    //Percentual de coloração desse dia.
	private Color pixColor;

	
	public Day(int day, int month, int year, int dayWeek, int x, int y, boolean exists, final Month monthObject, Color pixColor)
	{
		this.pixColor = pixColor;
		this.exists = exists;
		board.setBorder(null);
		board.setEditable(false);
		board.setBounds(x, y, 30, 20);    //Dimensão fixa de cada dia (largura: 30, altura: 20).
		this.day = day;
		this.month = month;
		this.year = year;
		this.monthObject = monthObject;
		
		if (exists) {    //Verificando se o dia existe no mês.
			board.setBackground(new Color(0, 0, 0));    //Coloração padrão: considerando a quantidade de arquivos criadas nesse dia como nula.
			
			switch (dayWeek) {
				case 0: this.dayWeek = "Domingo"; break;
				case 1: this.dayWeek = "Segunda"; break;
				case 2: this.dayWeek = "Terça"; break;
				case 3: this.dayWeek = "Quarta"; break;
				case 4: this.dayWeek = "Quinta"; break;
				case 5: this.dayWeek = "Sexta"; break;
				default : this.dayWeek = "Sábado";
			}
			
			board.addMouseListener(new MouseListener() 
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
					if (e.getClickCount() == 2) monthObject.selectedMonth();
					else if (e.getClickCount() == 1) {
						if (monthObject.isSelected()) monthObject.selectedMonth();
						selectDay();
					}
				}
			});
		}
	}

	
	public JTextField getBoard()
	{
		return(board);
	}
	
	
	private void selectDay()
	{
		board.setOpaque(selected);
		selected = !selected;    //Atualizando o estado de seleção do dia.
		board.revalidate();
		board.repaint();
		monthObject.getBoard().revalidate();
		monthObject.getBoard().repaint();
	}
	
	
	public void selectDay(boolean b)
	{
		board.setOpaque(b);
		selected = b;    //Atualizando o estado de seleção do dia.
		board.revalidate();
		board.repaint();
	}
	
	
	public void addFileIndex(int index)
	{
		filesIndexes.add(index);
	}
	
	
	public ArrayList<Integer> getFilesIndexes()
	{
		return(filesIndexes);
	}
	
	
	public boolean exists()    //Verificando se é um dia válido no calendário.
	{
		return(exists);
	}
	
	
	public void setColor(double max)    //Colorindo os pixels baseado na porcentagem que representa em tom de vermelho.
	{
		if (exists) {
			board.setToolTipText(day+"/"+month+"/"+year+" | "+this.dayWeek+" | "+filesIndexes.size()+" texto(s)");
			percent = filesIndexes.size()/max;
			board.setBackground(new Color((int) (pixColor.getRed()*percent), (int) (pixColor.getGreen()*percent), (int) (pixColor.getBlue()*percent)));
		}
	}
	
	
	public void setBackground(Color color)
	{
		board.setBackground(color);
	}
	
	
	public int getDay()
	{
		return(day);
	}


	public void changeColor(Color color)    //Atualizando as cores dos pixels.
	{
		if (exists) {
			pixColor = color;
			board.setBackground(new Color((int) (pixColor.getRed()*percent), (int) (pixColor.getGreen()*percent), (int) (pixColor.getBlue()*percent)));
		}
	}
	
	
}