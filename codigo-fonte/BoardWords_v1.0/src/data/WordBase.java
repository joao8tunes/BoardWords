/*
WordBase

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
import java.text.DecimalFormat;



public class WordBase 
{

	
	private String wordString;
	public boolean startword = true;
	private Color color;
	private int absoluteFrequency = 0;
	private float relativeFrequency = 0;
	private float inverseFrequency = 0;
	
	
	
	public WordBase(String word, Color color)
	{
		wordString = word;
		this.color = color;
	}
	
	
	public WordBase()    //Necessário para utilizar o construtor da classe filha.
	{	
	}
	
	
	public void setType(boolean type)
	{
		startword = type;    //Setando o tipo da palavra: startword (true) ou stopword (false).
	}
	
	
	public boolean isStartword()    //Verificando se a palavra é um startword.
	{
		return(startword);
	}
	
	
	public String getWord()
	{
		return(wordString);
	}
	
	
	public Color getColor()
	{
		return(color);
	}
	
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	
	public void setAbsoluteFrequency(Integer freq)
	{
		absoluteFrequency = freq;
	}

	
	public void setRelativeFrequency(Float freq)
	{
		DecimalFormat d = new DecimalFormat("0.000");
		/* O método 'replaceAll' é necessário porque para diferentes tipos de SOs (Linux x Windows), o formato dos números pode ser separado por '.' ou ','. */
		relativeFrequency = Float.valueOf(d.format(freq).replaceAll(",", "\\."));
	}


	public void setInverseFrequency(Float freq)
	{
		DecimalFormat d = new DecimalFormat("0.000");
		/* O método 'replaceAll' é necessário porque para diferentes tipos de SOs (Linux x Windows), o formato dos números pode ser separado por '.' ou ','. */
		inverseFrequency = Float.valueOf(d.format(freq).replaceAll(",", "\\."));
	}
	
	
	public Integer getAbsoluteFrequency()
	{
		return(absoluteFrequency);
	}

	
	public Float getRelativeFrequency()
	{
		return(relativeFrequency);
	}


	public Float getInverseFrequency()
	{
		return(inverseFrequency);
	}
	
	
}