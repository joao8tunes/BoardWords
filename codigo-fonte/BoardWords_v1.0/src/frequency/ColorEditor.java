/*
ColorEditor

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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.text.BadLocationException;
import visualization.TextViewer;
import data.Text;
import data.TextBase;
import data.Word;
import data.WordBase;



@SuppressWarnings("serial")
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{

	
	Color currentColor = Color.GRAY;
    JButton button;
    JColorChooser colorChooser;
    JDialog dialog;
    protected static final String EDIT = "edit";
    JTable wordsTable;
    ArrayList<TextViewer> panels;
    TextBase[] texts;

    
    public ColorEditor(JTable wordsTable, ArrayList<TextViewer> panels, TextBase[] texts)
    {
    	this.wordsTable = wordsTable;
    	this.panels = panels;
    	this.texts = texts;
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button, "Cor do startword", true, colorChooser, this, null);
    }

    
    public void actionPerformed(ActionEvent e)
    {
        if (EDIT.equals(e.getActionCommand())) {
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);
            fireEditingStopped();
        } 
        else {
            currentColor = colorChooser.getColor();
            
            for (int idxPnl = 0; idxPnl < panels.size(); idxPnl++) {    //Atualizando as cores da palavra em todos os painéis.
				if (panels.get(idxPnl) != null) {    //Verificando se o painél já foi removido (janela de visualização fechada).
					Text[] texts = panels.get(idxPnl).getTexts();    //Obtendo os textos de cada painél para alterar a cor das palavras.
					
					for (int idxTxt = 0; idxTxt < texts.length; idxTxt++) {    //Percorrendo todos os textos de cada painél.
						ArrayList<Word> text = texts[idxTxt].getText();
						for (int idxWrd = 0; idxWrd < text.size(); idxWrd++) {    //Percorrendo as palavras do texto.
							if (text.get(idxWrd).getWord().equals(wordsTable.getValueAt(wordsTable.getSelectedRow(), 0).toString())) text.get(idxWrd).setColor(currentColor);
						}
						/* Atualizando os painéis: */
						texts[idxTxt].getTextBoard().revalidate();
						texts[idxTxt].getTextBoard().repaint();
					}
					
					//Atualizando as cores das palavras selecionadas/destacadas:
					int idxWord;
					if ((idxWord = panels.get(idxPnl).searchWord(wordsTable.getValueAt(wordsTable.getSelectedRow(), 0).toString())) != -1) {
						try {
							panels.get(idxPnl).updateWordColor(idxWord, currentColor);
						} 
						catch (BadLocationException e2) {
							e2.printStackTrace();
						}
					}
				}
			}
            
            for (int idxTxt = 0; idxTxt < texts.length; idxTxt++) {    //Atualizando as cores da estrutura de textos.
				ArrayList<WordBase> curText = texts[idxTxt].getText();
				for (int idxWrd = 0; idxWrd < curText.size(); idxWrd++) {    //Percorrendo as palavras de cada texto.
					if (curText.get(idxWrd).getWord().equals(wordsTable.getValueAt(wordsTable.getSelectedRow(), 0).toString())) curText.get(idxWrd).setColor(currentColor);
				}
			}
        }
    }

    
    public Object getCellEditorValue()
    {
        return currentColor;
    }

    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        currentColor = (Color)value;
        return button;
    }

    
    public void setTexts(TextBase[] texts)
	{
		this.texts = texts;    //Textos de base para controle das cores das palavras.
	}
    
    
}