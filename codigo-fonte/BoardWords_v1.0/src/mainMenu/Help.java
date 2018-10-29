/*
Help

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class Help 
{

	
	private JFrame frame = new JFrame();
	private JEditorPane textArea = new JEditorPane();
	private JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JPanel panel = new JPanel();
	private JButton helpButton = new JButton();
	private String credText = "";
	private JLabel credImage = new JLabel();

	
	public Help()
	{
		textArea.setContentType("text/html");
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setTitle("Ajuda e créditos");
		frame.setLayout(null);
		panel.setLayout(null);
		panel.setBackground(new Color(58, 95, 205));
		panel.setBounds(0, 0, 550, 500);
		frame.setResizable(false);
		textArea.setEditable(false);
		frame.setMinimumSize(new Dimension(550, 500));
		frame.setLocationRelativeTo(null);    //Abrir janela no meio da tela.
		scroll.setBounds(10, 100, 530, 360);
		helpButton.setBounds(420, 20, 120, 60);
		helpButton.setBackground(new Color(25, 25, 112));
		helpButton.setToolTipText("Instruções de uso");
		credImage.setBounds(255, 20, 120, 60);
		helpButton.setIcon(new ImageIcon(getClass().getResource("/icons/users_icon&32.png")));
		credImage.setIcon(new ImageIcon(getClass().getResource("/icons/coffe_cup_icon&48.png")));
		panel.add(scroll, BorderLayout.NORTH);
		panel.add(helpButton);
		panel.add(credImage);
		helpButton.setVisible(true);
		panel.setVisible(true);
		scroll.setVisible(true);
		textArea.setVisible(true);
		frame.add(panel);
		frame.setVisible(false);
		
		BufferedReader curFile;
		try {
			curFile = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/files/credits.html"), "ISO-8859-1"));
			while (curFile.ready()) {    //Lendo todas as linhas do arquivo atual.
				credText += curFile.readLine()+"\n";
			}
			
			curFile.close();
		} 
		catch (IOException e2) {
			e2.printStackTrace();
		}
		
		helpButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
		            File dir = new File(System.getProperty("java.io.tmpdir"));    //Criando uma cópia do manual na pasta temporária do Java.
		            
		            dir.setWritable(true);
		            
		            File temp = new File(dir.getAbsolutePath()+File.separatorChar+"Manual_do_Usuario-BoardWords_v1.0.0.pdf");    //Criando um link para um arquivo temporário com o mesmo nome do arquivo original.
		            InputStream is = this.getClass().getResourceAsStream("/files/Manual_do_Usuario-BoardWords_v1.0.0.pdf");
		            FileOutputStream link = new FileOutputStream(temp);
		            FileWriter fw = new FileWriter(temp);
		            byte[] buffer = new byte[512*1024];    //Definindo um buffer não tão grande.
		            int read;
		            
		            while ((read = is.read(buffer)) != -1) {
		            	link.write(buffer, 0, read);
		            }

		            fw.close();
		            link.close();
		            is.close();
		            Desktop.getDesktop().open(temp);
		        } 
				catch (IOException ex) {
		            JOptionPane.showMessageDialog(null, "Não foi possível abrir o manual de instruções.\nÉ necessária a configuração de um leitor\nde arquivos .pdf para executar essa função.", "Erro", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		
		textArea.setText(credText);
		textArea.revalidate();
		textArea.repaint();
		textArea.setCaretPosition(0);
		textArea.requestFocus();
		frame.validate();
		frame.repaint();
	}
	
	
	public void setVisible(boolean choice)
	{
		frame.setVisible(choice);
	}

	
}