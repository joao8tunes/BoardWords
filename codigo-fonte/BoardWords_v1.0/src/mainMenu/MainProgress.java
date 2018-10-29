/*
MainProgress

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

package mainMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class MainProgress
{

	
	private JFrame frame;
	private JPanel panel;
	private JButton cancelButton;
    private Principal p;    //Classe onde o processo está ocorrendo; necessário para informar seu cancelamento.
    private boolean canceled = false;
    
    
    public MainProgress(final Principal p)
	{
    	this.p = p;
    	frame = new JFrame();
    	panel = new JPanel();
    	cancelButton = new JButton("Cancelar");
    	panel.setBackground(Color.WHITE);
    	frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
    	cancelButton.setBackground(new Color(25, 25, 112));
    	cancelButton.setForeground(Color.WHITE);
    	panel.add(new JLabel("", new ImageIcon(getClass().getResource("/files/loader.gif")), JLabel.CENTER));
    	panel.add(cancelButton);
    	frame.setMinimumSize(new Dimension(160, 150));
    	frame.setResizable(false);
    	frame.setLocationRelativeTo(null);
    	frame.add(panel);
    	panel.setVisible(true);
    	frame.setVisible(true);
    	cancelButton.addActionListener(new ActionListener() 
    	{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (p != null) p.cancelProcess();
				else canceled = true;
			}
		});
    	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				cancel();
			}
		});
	}
    
    
    public void close()
    {
    	Toolkit.getDefaultToolkit().beep();
    	frame.setVisible(false);
    	frame.setEnabled(false);
    }
	
	
    public void error()
    {
    	Toolkit.getDefaultToolkit().beep();
    	close();
    }
    
    public void cancel()
    {
    	Toolkit.getDefaultToolkit().beep();
    	if (p != null) {
    		p.cancelProcess();
    		close();
    	}
    	else canceled = true;
    }
    
    
    public void setVisible(boolean b)
    {
    	frame.setVisible(b);
    }
    
    
    public boolean isCanceled()
    {
    	return(canceled);
    }
    
    
}