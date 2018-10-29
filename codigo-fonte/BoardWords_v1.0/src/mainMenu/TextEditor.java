/*
TextEditor

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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;



public class TextEditor 
{

	
	private JFrame frame = new JFrame();
	private JTextPane textPane = new JTextPane();
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane(textPane);
	private String lastFile = null;
	private JButton newButton = new JButton();
	private JButton openButton = new JButton();
	private JButton saveButton = new JButton();
	private JButton saveAsButton = new JButton();
	private JButton searchButton = new JButton();
	private Object[] options = {"Yes", "No", "Cancel"};
	private boolean close = false;
	
	
	public TextEditor()
	{
		frame.setIconImage(new ImageIcon(getClass().getResource("/icons/eye_inv_icon&32.png")).getImage());
		frame.setTitle("Novo texto");
		panel.setBackground(new Color(58, 95, 205));
		panel.setBounds(0, 0, 550, 500);
		frame.setLayout(null);
		panel.setLayout(null);
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(550, 500));
		frame.setLocationRelativeTo(null);
		scroll.setBounds(10, 34, 530, 426);
		scroll.setBorder(null);
		panel.add(scroll, BorderLayout.NORTH);
		panel.setVisible(true);
		scroll.setVisible(true);
		textPane.setVisible(true);
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (!(textPane.getText().isEmpty() && frame.getTitle().equals("Novo texto"))) {
					int choice = JOptionPane.showOptionDialog(null, "Deseja salvar o texto atual?", "Sair", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (choice == JOptionPane.YES_OPTION) {
						JFileChooser path = new JFileChooser();
						path.setDialogTitle("Salvar texto");
						path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
						path.setAcceptAllFileFilterUsed(false);
						if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
							String newFile = path.getSelectedFile().getAbsolutePath();
							boolean save = true;
							
							if (!newFile.endsWith(".txt")) newFile += ".txt";
							File file = new File(newFile);
							if (file.exists()) {
								if (JOptionPane.showConfirmDialog(null, "O arquivo\n"+file.getAbsolutePath()+"\njá existe. Deseja sobrescrevê-lo?") != JOptionPane.YES_OPTION) save = false; 
							}
								
							if (save) {
								try {
									OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
									writer.write(textPane.getText());
									writer.close();
								} 
								catch (IOException e1) {
									e1.printStackTrace();
								}	
							}			
						}
						
						textPane.setText("");
						frame.setTitle("Novo texto");
					}
					else if (choice == JOptionPane.NO_OPTION) {
						frame.setVisible(false);
						frame = null;
						close = true;
					}
				}
				else {
					frame.setVisible(false);
					frame = null;
					close = true;
				}
			}
		});
		textPane.addMouseListener(new MouseListener() 
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
				if (e.getClickCount() == 2) {
					textPane.getHighlighter().removeAllHighlights();
					textPane.revalidate();
					textPane.repaint();
				}
			}
		});
		createNewButton();
		createOpenButton();
		createSaveButton();
		createSaveAsButton();
		createSearchButton();
	}
	
	
	public void createNewButton()
	{
		newButton.setToolTipText("Novo texto");
		newButton.setIcon(new ImageIcon(getClass().getResource("/icons/doc_new_icon&16.png")));
		newButton.setBounds(3, 3, 28, 28);
		newButton.setBackground(new Color(25, 25, 112));
		newButton.setVisible(true);
		newButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!(textPane.getText().isEmpty() && frame.getTitle().equals("Novo texto"))) {
					int choice = JOptionPane.showOptionDialog(null, "Deseja salvar o texto atual?", "Sair", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (choice == JOptionPane.YES_OPTION) {
						JFileChooser path = new JFileChooser();
						path.setDialogTitle("Salvar texto");
						path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
						path.setAcceptAllFileFilterUsed(false);
						if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
							String newFile = path.getSelectedFile().getAbsolutePath();
							boolean save = true;
							
							if (!newFile.endsWith(".txt")) newFile += ".txt";
							File file = new File(newFile);
							if (file.exists()) {
								if (JOptionPane.showConfirmDialog(null, "O arquivo\n"+file.getAbsolutePath()+"\njá existe. Deseja sobrescrevê-lo?") != JOptionPane.YES_OPTION) save = false; 
							}
								
							if (save) {
								try {
									OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
									writer.write(textPane.getText());
									writer.close();
								} 
								catch (IOException e1) {
									e1.printStackTrace();
								}	
							}			
						}
						
						textPane.setText("");
						frame.setTitle("Novo texto");
					}
					else if (choice == JOptionPane.NO_OPTION) {
						textPane.setText("");
						frame.setTitle("Novo texto");
					}
				}
			}
		});
		panel.add(newButton);
	}
	
	
	public void createOpenButton()
	{
		openButton.setToolTipText("Abrir arquivo");
		openButton.setIcon(new ImageIcon(getClass().getResource("/icons/folder_open_icon&16.png")));
		openButton.setBounds(34, 3, 28, 28);
		openButton.setBackground(new Color(25, 25, 112));
		openButton.setVisible(true);
		openButton.addActionListener(new ActionListener() 
		{
			private BufferedReader buffer;

			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Abrir arquivo");
				path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
				path.setAcceptAllFileFilterUsed(false); 
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					lastFile = new String(path.getSelectedFile().getAbsolutePath());
					frame.setTitle(lastFile);
					
					try {
						buffer = new BufferedReader(new InputStreamReader(new FileInputStream(lastFile), "ISO-8859-1"));
					
						textPane.setText("");
						while (buffer.ready()) {
							textPane.setText(textPane.getText()+buffer.readLine());
							if (buffer.ready()) textPane.setText(textPane.getText()+"\n");
						}
						textPane.setCaretPosition(0);
						textPane.requestFocus();
					} 
					catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} 
					catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
			}
		});
		panel.add(openButton);
	}
	
	
	public void createSaveButton()
	{
		saveButton.setToolTipText("Salvar texto");
		saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/save_icon&16.png")));
		saveButton.setBounds(65, 3, 28, 28);
		saveButton.setBackground(new Color(25, 25, 112));
		saveButton.setVisible(true);
		saveButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (lastFile == null) {
					JFileChooser path = new JFileChooser();
					path.setDialogTitle("Salvar texto");
					path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
					path.setAcceptAllFileFilterUsed(false);
					if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						lastFile = new String(path.getSelectedFile().getAbsolutePath());
					}
					else return;
				}
				
				if (!lastFile.endsWith(".txt")) lastFile += ".txt";
				File file = new File(lastFile);
				
				try {
					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
				
					writer.write(textPane.getText());
					writer.close();
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}			
				
				frame.setTitle(lastFile);
			}
		});
		panel.add(saveButton);
	}	
	
	
	public void createSaveAsButton()
	{
		saveAsButton.setToolTipText("Salvar texto como...");
		saveAsButton.setIcon(new ImageIcon(getClass().getResource("/icons/doc_export_icon&16.png")));
		saveAsButton.setBounds(96, 3, 28, 28);
		saveAsButton.setBackground(new Color(25, 25, 112));
		saveAsButton.setVisible(true);
		saveAsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser path = new JFileChooser();
				path.setDialogTitle("Salvar texto como...");
				path.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
				path.setAcceptAllFileFilterUsed(false);
				if (path.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String newFile = path.getSelectedFile().getAbsolutePath();
					boolean save = true;
					
					if (!newFile.endsWith(".txt")) newFile += ".txt";
					File file = new File(newFile);
					if (file.exists()) {
						if (JOptionPane.showConfirmDialog(null, "O arquivo\n"+file.getAbsolutePath()+"\njá existe. Deseja sobrescrevê-lo?") != JOptionPane.YES_OPTION) save = false; 
					}
						
					if (save) {
						try {
							OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
							writer.write(textPane.getText());
							writer.close();
						} 
						catch (IOException e1) {
							e1.printStackTrace();
						}	
						
						frame.setTitle(newFile);
					}			
				}
			}
		});
		panel.add(saveAsButton);
	}	
	
	
	public void createSearchButton()
	{
		searchButton.setToolTipText("Procurar palavra");
		searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/zoom_icon&16.png")));
		searchButton.setBounds(127, 3, 28, 28);
		searchButton.setBackground(new Color(25, 25, 112));
		searchButton.setVisible(true);
		searchButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String word = JOptionPane.showInputDialog(null, "Insira uma palavra:", "Procurar palavra", JOptionPane.PLAIN_MESSAGE);
			
				if (word != null && !word.equals("")) {
					try {
						selectWords(word);
					} 
					catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(searchButton);
	}	
	
	
	public void selectWords(String word) throws BadLocationException
	{
		Pattern p;
		Matcher m;
				
		p = Pattern.compile(word);
		m = p.matcher(textPane.getText().toLowerCase());
		
		textPane.getHighlighter().removeAllHighlights();
		
		while (m.find()) {
	       	textPane.getHighlighter().addHighlight(m.start(), m.end(), new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
	    }
		textPane.revalidate();
		textPane.repaint();
	}
	
	
	public void close()
	{
		frame.setVisible(false);
		frame = null;
		close = true;
	}
	
	
	public boolean isClosed()
	{
		return(close);
	}

	
}