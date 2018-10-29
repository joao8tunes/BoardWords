/*
TextSingle

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class TextSingle
{


	private JFrame frame = new JFrame();
	private JTextArea textArea = new JTextArea();
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane(textArea);
	private JButton previousButton = new JButton();
	private JButton nextButton = new JButton();
	private ArrayList<String[]> list;
	private int curIdx;
	
	
	public TextSingle(ArrayList<String[]> list, int curIdx) throws IOException
	{
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setTitle(list.get(curIdx)[0]);
		this.curIdx = curIdx;
		panel.setBackground(new Color(58, 95, 205));
		panel.setBounds(0, 0, 550, 500);
		frame.setLayout(null);
		panel.setLayout(null);
		frame.setResizable(false);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		frame.setMinimumSize(new Dimension(550, 500));
		frame.setLocationRelativeTo(null);
		scroll.setBounds(10, 10, 530, 400);
		previousButton.setBounds(135, 420, 120, 40);
		previousButton.setBackground(new Color(25, 25, 112));
		nextButton.setBounds(295, 420, 120, 40);
		nextButton.setBackground(new Color(25, 25, 112));
		previousButton.setToolTipText("Texto anterior");
		nextButton.setToolTipText("Texto posterior");
		previousButton.setIcon(new ImageIcon(getClass().getResource("/icons/arrow_left_icon&16.png")));
		nextButton.setIcon(new ImageIcon(getClass().getResource("/icons/arrow_right_icon&16.png")));
		panel.add(scroll, BorderLayout.NORTH);
		panel.add(previousButton);
		panel.add(nextButton);
		previousButton.setVisible(true);
		nextButton.setVisible(true);
		panel.setVisible(true);
		scroll.setVisible(true);
		textArea.setVisible(true);
		frame.add(panel);
		frame.setVisible(true);
		previousButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try {
					previousText();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		nextButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try {
					nextText();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.list = list;
		setText();
	}
	
	
	public void setText() throws IOException
	{
		frame.setTitle(list.get(curIdx)[0]);
		textArea.setText(list.get(curIdx)[1]);
	}
	
	
	private void previousText() throws IOException
	{
		if (curIdx == 0) curIdx = list.size()-1;
		else --curIdx;
		setText();
	}
	
	
	private void nextText() throws IOException
	{
		if (curIdx == list.size()-1) curIdx = 0;
		else ++curIdx;
		setText();
	}


}