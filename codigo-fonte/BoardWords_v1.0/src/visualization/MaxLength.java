/*
MaxLength

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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;



@SuppressWarnings("serial")
public class MaxLength extends PlainDocument 
{

	
	private int maxLength;

	
    public MaxLength(int maxlen) 
    {
        super();    
        if (maxlen <= 0) throw new IllegalArgumentException("You must specify a maximum length!");
        maxLength = maxlen;
    }

    
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
    {
        if (str == null || getLength() == maxLength) return;
        
        int totalLen = (getLength() + str.length());
        
        if (totalLen <= maxLength) {
            super.insertString(offset, str, attr);
            return;
        }
        
        String newStr = str.substring(0, (maxLength - getLength()));
        super.insertString(offset, newStr, attr);
    }


}