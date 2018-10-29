/*
Word

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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

import visualization.Configuration;



public class Word extends WordBase
{


	private JTextField word = new JTextField();
	private String frequencies;
	
	
	public Word(final String startword, Color color, int idx, final Configuration config)
	{
		this.word.setToolTipText(startword);
		this.word.setBorder(null);
		this.word.setBounds(8*idx, 0, 8, 8);
		this.word.setBackground(color);
		this.word.setEditable(false);
		this.word.setVisible(true);
		this.word.addMouseListener(new MouseListener() 
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
				if (!config.isFreq()) word.setToolTipText(startword);
				else word.setToolTipText("<html>p: "+startword+frequencies);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
			}
		});
	}
	
	
	public String getWord()
	{
		return(word.getToolTipText());
	}
	
	
	public Color getColor()
	{
		return(word.getBackground());
	}
	
	
	public void setColor(Color color)
	{
		this.word.setBackground(color);
	}
	
	
	public void setLocation(Point local)
	{
		word.setLocation(local);
	}
	
	
	public Point getLocation()
	{
		return(word.getLocation());
	}
	
	
	public JTextField getPix()
	{
		return(word);
	}

	
	public void setOpaque(boolean b)
	{
		word.setOpaque(b);
		word.revalidate();
		word.repaint();
	}


	public boolean isOpaque()
	{
		return(word.isOpaque());
	}
	
	
	public void joinFrequencies()
	{
		frequencies = "<br>fa: "+getAbsoluteFrequency()+"<br>fr: "+getRelativeFrequency()+"<br>fi: "+getInverseFrequency()+"</html>";    //Frequências formatadas.
	}


}