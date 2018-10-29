/*
Month

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JTextField;



public class Month 
{

	
	private JTextField board = new JTextField();
	private JTextField border = new JTextField();
	private ArrayList<Day> days = new ArrayList<Day>();
	@SuppressWarnings("unused")
	private int month, year;
	private boolean selected = false;
	
	
	@SuppressWarnings("deprecation")
	public Month(int month, int year, int x, int y, Color colorBackground, Color selColor, Color validDayColor, Color invalidDayColor) throws Throwable
	{
		Boolean exists = true;
		int curDay = 1, lastDay;
		java.util.Date date = (java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse(curDay+"/"+month+"/"+year);
		
		lastDay = getMaxDay(date);    //Último dia do mês.
		this.month = month;
		this.year = year;
		border.setBounds(x, y, 7*30 + 4, 6*20 + 4);
		border.setEditable(false);
		border.setBackground(Color.GRAY);
		board.setLayout(null);
		board.setBounds(2, 2, 7*30, 6*20);
		board.setEditable(false);
		board.setBackground(selColor);
		border.add(board);
		//Configurando o grid de dias, uma "matriz" de dimensão 6x7.
		//Cada pixel da matriz representa um dia do mês.
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (date.getDay() == j) {    //Posicionando os dias conforme o dia da semana correspondente.
					Day day = new Day(curDay, month, year, date.getDay(), j*30, i*20, exists, this, validDayColor);
					if (!exists) day.setBackground(invalidDayColor);
					days.add(day);
					board.add(day.getBoard());    //Adicionando o pixel do dia ao painél de mês.
					++curDay;    //Incrementando os dias.
					date = sumDays(date, 1);
					if (curDay > lastDay) exists = false;
				}
				else {
					Day day = new Day(curDay, month, year, date.getDay(), j*30, i*20, false, this, invalidDayColor);
					day.setBackground(invalidDayColor);
					days.add(day);
					board.add(day.getBoard());    //Adicionando o pixel do dia ao painél de mês.
				}
			}
		}
	}
	
	
	public JTextField getBoard()
	{
		return(border);
	}
	
	
	public ArrayList<Day> getDays()
	{
		return(days);
	}
	
	
	@SuppressWarnings("deprecation")
	private int getMaxDay(java.util.Date date)    //Obtendo o último dia do mês.
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		newDate.set(date.getYear()+1900, date.getMonth(), newDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		return(newDate.getTime().getDate());
	}


	public java.util.Date sumDays(java.util.Date lowerLimDate, int days)
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(lowerLimDate);
		newDate.add(GregorianCalendar.DAY_OF_MONTH, days);
		return((java.util.Date) newDate.getTime());
	}
	
	
	public void selectedMonth()
	{
		for (int i = 0; i < days.size(); i++) {
			if (days.get(i).exists()) days.get(i).selectDay(selected);
		}
		selected = !selected;
		board.revalidate();
		board.repaint();
	}
	
	
	public void unselectDays()
	{
		for (int i = 0; i < days.size(); i++) {
			if (days.get(i).exists()) days.get(i).selectDay(true);
		}
		board.revalidate();
		board.repaint();
	}
	
	
	public int getMonth()
	{
		return(month);
	}
	
	
	public boolean isSelected()
	{
		return(selected);
	}


	public void changeColor(Color color)    //Alterando a cor de seleção (fundo desse JTextField principal - 'board').
	{
		board.setBackground(color);
		board.revalidate();
		board.repaint();
	}
	
	
}