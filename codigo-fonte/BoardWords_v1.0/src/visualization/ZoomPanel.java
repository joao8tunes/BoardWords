/*
ZoomPanel

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

/* Agradecimentos ao Alisson do Carmo e Leonardo Nozawa por toda ajuda e 
suporte para construir essa classe. */

/*Agradecimentos à toda a comunidade Java, pelo suporte técnico prestado;
em especial aos fóruns de discussão, que ajudam a desvendar conceitos e 
dominar técnicas de forma muito produtiva.*/

package visualization;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JPanel;



@SuppressWarnings("serial")
public class ZoomPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener
{

	
	//Quantidade de zoom permitido: 12,5%, 25%, 50%, 100%, 200%, 400%, 800%.
    private double scale = 1.0;    //Escala de controle da quantidade de zoom permitida.
    private int in, out;    //Controle da quantidade de zoom permitida ('in' escalas abaixo e 'out' acima de 100%).
    
    
    public ZoomPanel(int limit) 
    {
    	in = out = limit;
    	this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.setLayout(null);
    }
    
    
    public void mouseScroll(MouseWheelEvent evt)    //Realiza o zoom baseado no scroll do mouse.
    {
        if(evt.getWheelRotation() == 1) zoomOut();
        else zoomIn();
    }
    
    
    private void makeZoom()
    {
        changeElements(this, true);
    }
    
    
    public void zoomIn()
    {
    	if (in > 0) {
    		--in;
    		++out;
    		scale = scale*2.0;
	    	makeZoom();
    		scale = scale/2.0;
	        makeZoom();
    	}
    }
    
    
    public void zoomOut()
    {
    	if (out > 0) {
    		--out;
    		++in;
	    	scale = scale/2.0;
	        makeZoom();
	        scale = scale*2.0;
	    	makeZoom();
    	}
    }
    
    
    public void restore()
    {
    	while (in > out) zoomIn();
    	while (in < out) zoomOut();
    }
    
    
    private void changeElements(JComponent component, boolean root)    //root: identifica se é o painél, o único componente que não precisa ser deslocado.
    {
        component.setSize((int)(component.getWidth()*scale), (int)(component.getHeight()*scale));
        if(!root) component.setLocation((int)(component.getX()*scale), (int)(component.getY()*scale));
        else component.setPreferredSize(new Dimension((int)(component.getWidth()*scale), (int)(component.getHeight()*scale)));    //Necessário para aumentar o painél e atualizar os limites do scroll.
        
        for(Component comps : component.getComponents()){    //Alterando os componentes internos recursivamente.
        	changeElements((JComponent)comps, false);
        }
    }

    
    @Override
    public void mouseDragged(MouseEvent e) 
    {
    }

    
    @Override
    public void mouseMoved(MouseEvent e) 
    {
    }

    
    @Override
    public void mouseClicked(MouseEvent e) 
    {
    }
    
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
    }
    
    
    @Override
    public void mouseReleased(MouseEvent e) 
    {
    }
    
    
    @Override
    public void mouseEntered(MouseEvent e) 
    {
    }
    
    
    @Override
    public void mouseExited(MouseEvent e) 
    {
    }
    
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) 
    {
        mouseScroll(e);
    }
      
    
}