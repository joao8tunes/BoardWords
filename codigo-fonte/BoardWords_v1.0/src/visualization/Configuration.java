/*
Configuration

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import data.Text;



@SuppressWarnings("unused")
public class Configuration
{

	
	private JFrame frame = new JFrame();
	private JPanel titlePanelType = new JPanel();
	private JPanel checksPanel = new JPanel();
	private JPanel titleHorPanel = new JPanel();
	private JPanel titleVertPanel = new JPanel();
	private JPanel daysPanel = new JPanel();
	private JPanel minutesPanel = new JPanel();
	private JPanel minPanel = new JPanel();
	private JButton dayButton = new JButton("Dia");
	private JButton monthButton = new JButton("Mês");
	private JButton yearButton = new JButton("Ano");
	private JTextField valueBox = new JTextField("1");
	private JButton minButton = new JButton("Minuto");
	private JButton hourButton = new JButton("Hora");
	private JTextField minBox = new JTextField("1");
	private JLabel horLabel = new JLabel("Agrupamento de datas");
	private JLabel vertLabel = new JLabel("Agrupamento de horários");
	private JCheckBox checkGroup = new JCheckBox("Agrupamento");
	private JCheckBox checkLeg = new JCheckBox("Legenda");
	private JCheckBox checkFreq = new JCheckBox("Frequência");
	private Text[] texts;
	private JPanel panelDisplay;
	private String typeGroup = "day";    //Agrupamento de dias (calendário).
	private String typeSubGroup = "hour";    //Agrupamento de minutos (horário).
	private ArrayList<JTextField> labels;    //Componentes de legenda dos agrupamentos e horários.
	
	
	public Configuration(int index, Text[] texts, JPanel panelDisplay)
	{
		this.panelDisplay = panelDisplay;
		this.texts = texts;    //Cópia dos dados para alterar os atributos.
		frame.setTitle("Configurações "+index);
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(410, 285));
		frame.setLocationRelativeTo(null);    //Abrir janela no meio da tela.
		frame.setVisible(false);
		setPanelDays();
		setPanelMinutes();
		setMinPanel();
		createCheckPanel();
	}
	
	
	protected void setPanelDays()
	{
		titleHorPanel.setBackground(new Color(58, 95, 205));
		titleHorPanel.setBounds(0, 0, 410, 30);
		horLabel.setBounds(10, 5, 400, 15);
		horLabel.setForeground(Color.WHITE);
		titleHorPanel.add(horLabel);
		frame.add(titleHorPanel);
		
		daysPanel.setBackground(new Color(58, 95, 205));
		daysPanel.setBounds(0, 30, 410, 80);
		createDayButton();
		createMonthButton();
		createYearButton();
		createValueBox();
		daysPanel.setVisible(true);
		frame.add(daysPanel);
	}


	private void createDayButton()
	{
		dayButton.setPreferredSize(new Dimension(120, 30));
		dayButton.setForeground(Color.WHITE);
		dayButton.setBackground(new Color(25, 25, 112));
		dayButton.setToolTipText("Agrupamento de dias");
		//Configuração padrão:
		dayButton.setOpaque(false);
		monthButton.setOpaque(false);
		yearButton.setOpaque(false);
		valueBox.setOpaque(false);
		valueBox.setEditable(false);
		dayButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (checkGroup.isSelected()) {
					dayButton.setOpaque(true);
					monthButton.setOpaque(false);
					yearButton.setOpaque(false);
					daysPanel.revalidate();
					daysPanel.repaint();
					typeGroup = "day";
				}
			}
		});
		daysPanel.add(dayButton);
	}
	
	
	private void createMonthButton()
	{
		monthButton.setPreferredSize(new Dimension(120, 30));
		monthButton.setForeground(Color.WHITE);
		monthButton.setBackground(new Color(25, 25, 112));
		monthButton.setToolTipText("Agrupamento de mêses");
		monthButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (checkGroup.isSelected()) {
					dayButton.setOpaque(false);
					monthButton.setOpaque(true);
					yearButton.setOpaque(false);
					daysPanel.revalidate();
					daysPanel.repaint();
					typeGroup = "month";
				}
			}
		});
		daysPanel.add(monthButton);
	}
	
	
	private void createYearButton()
	{
		yearButton.setPreferredSize(new Dimension(120, 30));
		yearButton.setForeground(Color.WHITE);
		yearButton.setBackground(new Color(25, 25, 112));
		yearButton.setToolTipText("Agrupamento de anos");
		yearButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (checkGroup.isSelected()) {
					dayButton.setOpaque(false);
					monthButton.setOpaque(false);
					yearButton.setOpaque(true);
					daysPanel.revalidate();
					daysPanel.repaint();
					typeGroup = "year";
				}
			}
		});
		daysPanel.add(yearButton);
	}
	
	
	private void createValueBox()
	{
		valueBox.setPreferredSize(new Dimension(60, 30));
		valueBox.setToolTipText("Valor do agrupamento");
		valueBox.setDocument(new MaxInteger(3));    //Intervalo de anos: 1 - 999.
		valueBox.setText("1");
		daysPanel.add(valueBox);
	}
	
	
	protected void setPanelMinutes()
	{
		titleVertPanel.setBackground(new Color(58, 95, 205));
		titleVertPanel.setBounds(0, 110, 410, 30);
		vertLabel.setBounds(10, 5, 400, 15);
		vertLabel.setForeground(Color.WHITE);
		titleVertPanel.add(vertLabel);
		titleVertPanel.setVisible(true);
		frame.add(titleVertPanel);
		
		minutesPanel.setBackground(new Color(58, 95, 205));
		minutesPanel.setBounds(0, 140, 410, 40);
		createMinButton();
		createHourButton();
		frame.add(minutesPanel);
	}
	
	
	private void createMinButton()
	{
		minButton.setPreferredSize(new Dimension(120, 30));
		minButton.setForeground(Color.WHITE);
		minButton.setBackground(new Color(25, 25, 112));
		minButton.setToolTipText("Agrupamento de minutos");
		//Configuração padrão:
		minButton.setOpaque(false);
		hourButton.setOpaque(false);
		minBox.setOpaque(false);
		minBox.setEditable(false);
		minPanel.revalidate();
		minPanel.repaint();
		minButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (checkGroup.isSelected()) {
					minButton.setOpaque(true);
					hourButton.setOpaque(false);
					minutesPanel.revalidate();
					minutesPanel.repaint();
					typeSubGroup = "minute";
					String oldValue = minBox.getText();
					minBox.setDocument(new MaxInteger(4));
					minBox.setText(oldValue);
				}
			}
		});
		minutesPanel.add(minButton);
	}
	

	private void createHourButton()
	{
		hourButton.setPreferredSize(new Dimension(120, 30));
		hourButton.setForeground(Color.WHITE);
		hourButton.setBackground(new Color(25, 25, 112));
		hourButton.setToolTipText("Agrupamento de horas");
		hourButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (checkGroup.isSelected()) {
					minButton.setOpaque(false);
					hourButton.setOpaque(true);
					minutesPanel.revalidate();
					minutesPanel.repaint();
					typeSubGroup = "hour";
					String oldValue = minBox.getText();
					minBox.setDocument(new MaxInteger(2));
					minBox.setText(oldValue);
				}
			}
		});
		minutesPanel.add(hourButton);
	}
	
	
	private void setMinPanel()
	{
		minPanel.setBackground(new Color(58, 95, 205));
		minPanel.setBounds(0, 180, 410, 45);
		minBox.setPreferredSize(new Dimension(60, 30));
		minBox.setToolTipText("Valor do agrupamento");
		minBox.setDocument(new MaxInteger(2));    //Intervalo de horas:  1 - 24.
		minBox.setText("1");
		minPanel.add(minBox);
		frame.add(minPanel);
	}
	
	
	public void createCheckPanel()
	{
		checksPanel.setBounds(0, 225, 410, 45);
		checksPanel.setBackground(new Color(58, 95, 205));
		checksPanel.setVisible(true);
		checkGroup.setForeground(Color.WHITE);
		checkGroup.setSelected(false);
		checkGroup.setBackground(new Color(58, 95, 205));
		checkGroup.setToolTipText("Agrupamento dos textos");
		checkGroup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (checkGroup.isSelected()) {
					dayButton.setOpaque(true);
					monthButton.setOpaque(false);
					yearButton.setOpaque(false);
					valueBox.setOpaque(true);
					valueBox.setEditable(true);
					minButton.setOpaque(false);
					hourButton.setOpaque(true);
					minBox.setOpaque(true);
					minBox.setEditable(true);
				}
				else {
					dayButton.setOpaque(false);
					monthButton.setOpaque(false);
					yearButton.setOpaque(false);
					valueBox.setOpaque(false);
					valueBox.setEditable(false);
					minButton.setOpaque(false);
					hourButton.setOpaque(false);
					minBox.setOpaque(false);
					minBox.setEditable(false);
				}
				
				daysPanel.revalidate();
				daysPanel.repaint();
				minutesPanel.revalidate();
				minutesPanel.repaint();
				minPanel.revalidate();
				minPanel.repaint();	
			}
		});
		checksPanel.add(checkGroup, BorderLayout.NORTH);
		checkLeg.setForeground(Color.WHITE);
		checkLeg.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (labels != null) {
					for (JTextField j : labels) {
						j.setOpaque(checkLeg.isSelected());
					}
					
					panelDisplay.revalidate();
					panelDisplay.repaint();
				}
			}
		});
		checkLeg.setSelected(false);
		checkLeg.setBackground(new Color(58, 95, 205));
		checkLeg.setToolTipText("Legenda dos tempos e agrupamentos");
		checksPanel.add(checkLeg, BorderLayout.CENTER);
		checkFreq.setForeground(Color.WHITE);
		checkFreq.setSelected(false);
		checkFreq.setBackground(new Color(58, 95, 205));
		checkFreq.setToolTipText("Frequências das palavras");
		checksPanel.add(checkFreq, BorderLayout.SOUTH);
		frame.add(checksPanel);
	}
	
	
	public void setVisible(boolean choice)
	{
		frame.setVisible(choice);
	}
	
	
	public boolean isGroup()
	{
		return(checkGroup.isSelected());
	}
	
	
	public boolean isLeg()
	{
		return(checkLeg.isSelected());
	}
	
	
	public int getHorValue()
	{
		return(Integer.parseInt(valueBox.getText()));
	}
	
	
	public int getVertValue()
	{
		return(Integer.parseInt(minBox.getText()));
	}
	
	
	public boolean isValid()
	{	
		if (valueBox.getText().isEmpty() || minBox.getText().isEmpty() || Long.parseLong(valueBox.getText()) == 0 || Long.parseLong(minBox.getText()) == 0 || (Long.parseLong(minBox.getText()) > 1440 && minButton.isOpaque()) || (Long.parseLong(minBox.getText()) > 24 && hourButton.isOpaque())) {
		    JOptionPane.showMessageDialog(null, "Campo(s) de configuração inválido(s).\nIntervalo de dias/mêses/anos: 1 até 999;\nintervalo de minutos: 1 até 1440;\nintervalo de horas: 1 - 24.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		    valueBox.setText("1");
		    minBox.setText("1");
		    return(false);
		}
		return(true);

	} 
	
	
	public String getTypeGroup()
	{
		return(typeGroup);
	}
	
	
	public String getTypeSubGroup()
	{
		return(typeSubGroup);
	}
	
	
	public void doClickMinButton()
	{
		if (minButton.isOpaque()) return;
		minButton.doClick();
	}
	
	
	public void doClickHourButton()
	{
		if (hourButton.isOpaque()) return;
		hourButton.doClick();
	}
	
	
	public void doClickDayButton()
	{
		if (dayButton.isOpaque()) return;
		dayButton.doClick();
	}
	
	
	public void doClickMonthButton()
	{
		if (monthButton.isOpaque()) return;
		monthButton.doClick();
	}
	
	
	public void doClickYearButton()
	{
		if (yearButton.isOpaque()) return;
		yearButton.doClick();
	}
	
	
	public void doClickCheckGroup(boolean choice)
	{
		checkGroup.setSelected(choice);
		minBox.setOpaque(choice);
		valueBox.setOpaque(choice);
		minutesPanel.revalidate();
		minutesPanel.repaint();
		daysPanel.revalidate();
		daysPanel.repaint();
	}
	
	
	public void doClickCheckLeg(boolean choice)
	{
		checkLeg.setSelected(choice);
	}

	
	public void doClickCheckFreq(boolean choice)
	{
		checkFreq.setSelected(choice);
	}
	
	
	public void setHorValue(String value)
	{
		minBox.setText(value);
	}
	
	
	public void setVertValue(String value)
	{
		valueBox.setText(value);
	}
	
	
	public void setLabels(ArrayList<JTextField> labels)
	{
		this.labels = labels;
	}
	
	
	public boolean isFreq()
	{
		return(checkFreq.isSelected());
	}
	
	
}