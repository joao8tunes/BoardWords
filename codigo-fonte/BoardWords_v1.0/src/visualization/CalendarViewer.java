/*
CalendarViewer

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import data.TextBase;
import mainMenu.MainProgress;
import mainMenu.Principal;



public class CalendarViewer 
{

	
	private JFrame frame = new JFrame();
	private JPanel panelMenu = new JPanel();    //Painél de funcionalidades da janela (contém os botões).
	private ZoomPanel zoomPanel = new ZoomPanel(1);
	private JScrollPane scroll = new JScrollPane(zoomPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private ArrayList<Year> years = new ArrayList<Year>();
	private JButton validDayColorButton = new JButton();
	private JButton invalidDayColorButton = new JButton();
	private JButton selectColorButton = new JButton();
	private JButton panelColorButton = new JButton();
	private JButton zoomOutButton = new JButton();
	private JButton zoomInButton = new JButton();
	private JButton newVisButton = new JButton();
	private int maxFiles;
	private Principal principal;
	private Color validDayColor = new Color(255, 0, 0);    //Cor dos pixels válidos: começa com vermelho (padrão).
	private Color invalidDayColor = Color.GRAY;    //Cor dos pixels inválidos: começa com cinza (padrão).
	private Color selColor = new Color(58, 95, 205);    //Cor de seleção: começa com a tonalidade de azul do sistema.


	public CalendarViewer(Principal principal)
	{
		this.principal = principal;
		frame.setTitle("Calendário");
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setLocationRelativeTo(null);    //Abrir janela no meio da tela.
		frame.setResizable(true);
		frame.getContentPane().add(panelMenu, BorderLayout.NORTH);
		frame.getContentPane().add(scroll, BorderLayout.CENTER);
		panelMenu.setLayout(null);    //Necessário para forçar o posicionamento dos botões.
		panelMenu.setPreferredSize(new Dimension(800, 34));
		panelMenu.setBackground(new Color(58, 95, 205));
		scroll.setPreferredSize(new Dimension(800, 566));
		zoomPanel.setBackground(Color.BLACK);    //Cor do painél e dos dias inválidos.
		zoomPanel.setVisible(true);
		setPanelMenu();
		frame.setVisible(false);
		zoomPanel.addMouseListener(new MouseListener() 
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
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if (e.getClickCount() == 2) {    //Removendo a seleção de todos os dias.
					for (Year y : years) {
						y.unselectMonths();
					}
				}
			}
	
		});
	}
	
	
	public void setPanelMenu()
	{	
		createValidDayColorButton();
		createInvalidDayColorButton();
		createPanelColorButton();
		createSelColorButton();
		createNewVisButton();
		createZoomOutButton();
		createZoomInButton();
	}
	
	
	public void createValidDayColorButton()
	{
		validDayColorButton.setToolTipText("Alterar cor dos dias válidos");
		validDayColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/round_checkmark_icon&16.png")));
		validDayColorButton.setBounds(3, 3, 28, 28);
		validDayColorButton.setBackground(new Color(25, 25, 112));
		validDayColorButton.setVisible(true);
		validDayColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Selecionar cor dos pontos válidos", validDayColor);
				
				if (newColor != null && newColor != validDayColor) {
					validDayColor = newColor;
					
					for (Year y : years) {    //Atualizando a cor dos pixels.
						ArrayList<Month> months = y.getMonths();
						
						for (Month m : months) {
							ArrayList<Day> days = m.getDays();
							
							for (Day d : days) {
								d.changeColor(validDayColor);
							}
						}
					}
					
					zoomPanel.repaint();
					zoomPanel.revalidate();
				}
			}
		});
		panelMenu.add(validDayColorButton);
	}
	
	
	public void createInvalidDayColorButton()
	{
		invalidDayColorButton.setToolTipText("Alterar cor dos dias inválidos");
		invalidDayColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/round_delete_icon&16.png")));
		invalidDayColorButton.setBounds(34, 3, 28, 28);
		invalidDayColorButton.setBackground(new Color(25, 25, 112));
		invalidDayColorButton.setVisible(true);
		invalidDayColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Selecionar cor dos pontos inválidos", invalidDayColor);
				
				if (newColor != null && newColor != invalidDayColor) {
					invalidDayColor = newColor;
					
					for (Year y : years) {    //Atualizando a cor de fundo dos dias não válidos.
						ArrayList<Month> months = y.getMonths();
						
						for (Month m : months) {
							ArrayList<Day> days = m.getDays();
							
							for (Day d : days) {
								if (!d.exists()) d.setBackground(newColor);
							}
						}
					}
					
					zoomPanel.repaint();
					zoomPanel.revalidate();
				}
			}
		});
		panelMenu.add(invalidDayColorButton);
	}
	
	
	public void createPanelColorButton()
	{
		panelColorButton.setToolTipText("Alterar cor do painel");
		panelColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/fill_icon&16.png")));
		panelColorButton.setBounds(65, 3, 28, 28);
		panelColorButton.setBackground(new Color(25, 25, 112));
		panelColorButton.setVisible(true);
		panelColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Selecionar cor do painel", zoomPanel.getBackground());
				
				if (newColor != null && newColor != zoomPanel.getBackground()) {
					zoomPanel.setBackground(newColor);
					zoomPanel.repaint();
					zoomPanel.revalidate();
				}
			}
		});
		panelMenu.add(panelColorButton);
	}
	
	
	public void createSelColorButton()
	{
		selectColorButton.setToolTipText("Alterar cor de seleção dos dias");
		selectColorButton.setIcon(new ImageIcon(getClass().getResource("/icons/hand_2_icon&16.png")));
		selectColorButton.setBounds(96, 3, 28, 28);
		selectColorButton.setBackground(new Color(25, 25, 112));
		selectColorButton.setVisible(true);
		selectColorButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Color newColor = JColorChooser.showDialog(null, "Selecionar cor de seleção dos pontos", selColor);
				
				if (newColor != null && newColor != selColor) {
					selColor = newColor;
					
					for (Year y : years) {    //Atualizando a cor de seleção.
						ArrayList<Month> months = y.getMonths();
						
						for (Month m : months) {
							m.changeColor(selColor);
						}
					}
					
					zoomPanel.repaint();
					zoomPanel.revalidate();
				}
			}
		});
		panelMenu.add(selectColorButton);
	}
	
	
	public void createNewVisButton()
	{
		newVisButton.setToolTipText("Visualizar dados selecionados");
		newVisButton.setIcon(new ImageIcon(getClass().getResource("/icons/share_icon&16.png")));
		newVisButton.setBounds(127, 3, 28, 28);
		newVisButton.setBackground(new Color(25, 25, 112));
		newVisButton.setVisible(true);
		newVisButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Thread thread = new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						MainProgress pr = new MainProgress(null);
						ArrayList<Integer> files = new ArrayList<Integer>();
						
						for (Year y : years) {    //Identificando os dias selecionados e seus arquivos.
							if (pr.isCanceled()) {
								pr.close();
								return;
							}
							
							ArrayList<Month> months = y.getMonths();
							
							for (Month m : months) {
								if (pr.isCanceled()) {
									pr.close();
									return;
								}
								
								ArrayList<Day> days = m.getDays();
										
								for (Day d : days) {
									if (pr.isCanceled()) {
										pr.close();
										return;
									}
									
									if (!d.getBoard().isOpaque()) {
										for (Integer idx : d.getFilesIndexes()) {
											files.add(idx);
										}
									}				
								}
							}
						}
						
						if (pr.isCanceled()) {
							pr.close();
							return;
						}
						
						TextBase[] texts = new TextBase[files.size()];
						TextBase[] allTexts = principal.getTexts();
						
						for (int idx = 0; idx < files.size() && !pr.isCanceled(); idx++) {
							texts[idx] = allTexts[files.get(idx)];
						}
						
						if (pr.isCanceled()) {
							pr.close();
							return;
						}
						
						principal.newPanel(texts);
						pr.close();
					}
				});
				thread.start();
			}
		});
		panelMenu.add(newVisButton);
	}	
	
	
	public void createZoomOutButton()
	{
		zoomOutButton.setToolTipText("Diminuir zoom");
		zoomOutButton.setIcon(new ImageIcon(getClass().getResource("/icons/round_minus_icon&16.png")));
		zoomOutButton.setBounds(158, 3, 28, 28);
		zoomOutButton.setBackground(new Color(25, 25, 112));
		zoomOutButton.setVisible(true);
		zoomOutButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				zoomPanel.zoomOut();
				SwingUtilities.invokeLater(new Runnable() 
				{  
					public void run()
					{  
						zoomPanel.repaint();  
	                }  
	            });  	
				frame.revalidate();
				frame.repaint();
			}
		});
		panelMenu.add(zoomOutButton);
	}
	
	
	public void createZoomInButton()
	{
		zoomInButton.setToolTipText("Aumentar zoom");
		zoomInButton.setIcon(new ImageIcon(getClass().getResource("/icons/round_plus_icon&16.png")));
		zoomInButton.setBounds(189, 3, 28, 28);
		zoomInButton.setBackground(new Color(25, 25, 112));
		zoomInButton.setVisible(true);
		zoomInButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				zoomPanel.zoomIn();
				SwingUtilities.invokeLater(new Runnable() 
				{  
					public void run()
					{  
						zoomPanel.repaint();  
	                }  
	            });
				frame.revalidate();
				frame.repaint();
			}
		});
		panelMenu.add(zoomInButton);
	}

	
	public void setVisible(boolean b)
	{
		frame.setVisible(b);
	}

	
	public void setBounds(int firstYear, int lastYear) throws Throwable
	{
		int i = 0;
		zoomPanel.removeAll();
		years = new ArrayList<Year>();
		
		while (firstYear <= lastYear) {
			years.add(new Year(firstYear, i*212, 0, zoomPanel.getBackground(), selColor, validDayColor, invalidDayColor));
			zoomPanel.add(years.get(years.size()-1).getBoard());    //Adicionando o último ano criado ao painél.
			++firstYear;
			++i;
		}
		zoomPanel.setPreferredSize(new Dimension(212*i + 16, 1480));
		zoomPanel.revalidate();
		zoomPanel.repaint();
		maxFiles = 0;
	}


	public void setFilesIndexes(String date, int idx)
	{
		String[] curDate = date.split("/");
		int day = Integer.valueOf(curDate[0]), month = Integer.valueOf(curDate[1]), year = Integer.valueOf(curDate[2]);
		
		for (Year y : years) {    //Adicionando os índices dos arquivos aos respectivos dias.
			if (y.getYear() == year) {
				ArrayList<Month> months = y.getMonths();
			
				for (Month m : months) {
					if (m.getMonth() == month) {
						ArrayList<Day> days = m.getDays();
						
						for (Day d : days) {
							if (d.getDay() == day) {
								d.addFileIndex(idx);
								if (d.getFilesIndexes().size() > maxFiles) maxFiles = d.getFilesIndexes().size();
								return;
							}				
						}
					}
				}
			}
		}
	}
	
	
	public void paintPixels()
	{
		for (Year y : years) {    //Colorindo os pixels de dias conforme a porcentagem da quantidade de arquivos em relação ao dia com maior número de arquivos (maxFiles).
			ArrayList<Month> months = y.getMonths();
			
			for (Month m : months) {
				ArrayList<Day> days = m.getDays();
				
				for (Day d : days) {
					d.setColor(maxFiles);
				}
			}
		}
	}
	
	
}