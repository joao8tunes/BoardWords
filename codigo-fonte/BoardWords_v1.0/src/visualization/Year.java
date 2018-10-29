/*
Year

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
import java.util.ArrayList;
import javax.swing.JTextField;



public class Year 
{

	
	private JTextField board = new JTextField();
	private ArrayList<Month> months = new ArrayList<Month>();
	private int year;
	
	
	public Year(int year, int x, int y, Color colorBackground, Color selColor, Color validDayColor, Color invalidDayColor) throws Throwable
	{
		this.year = year;
		board.setLayout(null);
		board.setBounds(x, y, 212, 1464);
		board.setBorder(null);
		board.setEditable(false);
		
		for (int i = 0; i < 12; i++) {
			Month month;
			month = new Month(i+1, this.year, 0, i*122, colorBackground, selColor, validDayColor, invalidDayColor);
			months.add(month);
			board.add(month.getBoard());
		}
	}
	
	
	public JTextField getBoard()
	{
		return(board);
	}
	
	
	public ArrayList<Month> getMonths()
	{
		return(months);
	}
	
	
	public int getYear()
	{
		return(year);
	}

	
	public void unselectMonths()
	{
		for (Month m : months) {    //Removendo a seleção de todos os pontos.
			m.unselectDays();
		}
	}
	
	
}
