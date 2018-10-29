/*
DateInterval

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
import javax.swing.JTextField;



public class DateInterval 
{
	
	
	private JTextField interval = new JTextField();    //Texto de exibição do intervalo.

	
	public DateInterval(String interval, int x, int y, Color color, boolean opaque)
	{
		this.interval.setText(interval);    //Definindo o intervalo.
		this.interval.setBounds(x, y, 150, 12);    //Definindo o tamanho (fixo) e a localização (variável) do marcador de intervalo.
		this.interval.setBorder(null);
		this.interval.setHorizontalAlignment(JTextField.CENTER);
		this.interval.setEditable(false);
		this.interval.setBackground(color);
		this.interval.setForeground(Color.BLACK);
		this.interval.setOpaque(opaque);
	}
	
	
	public JTextField getInterval()
	{
		return(interval);
	}
	
	
}
