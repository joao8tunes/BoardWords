/*
TextBase

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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
import visualization.Configuration;
import visualization.TextSingle;
import visualization.TextViewer;
import frequency.Frequency;



public class TextBase 
{

	
	private String textFile;    //Arquivo texto na íntegra.
	private String fileName;    //Nome do arquivo do post.
	private String date;
	private String time;
	private ArrayList<WordBase> text = new ArrayList<WordBase>();    //Texto processado do arquivo filePath.
	
	
	public TextBase(String fileName, String filePath, String stopwordsPath, Frequency freq, int index) throws ParseException    //Estrutura utilizada como base na classe 'Main' para armazenar os arquivos processados no 'modo texto'.
	{
		String line[], aux[], day, month, year, hour, minute, bufferLine;
		BufferedReader stopwordsFile;
		
		try {
			this.textFile = "";
			this.fileName = fileName;    //Nome do arquivo.
			if (!this.fileName.endsWith("txt")) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
				textFile = null;
				return;
			}
			
			line = fileName.split("_");
			aux = line[0].split("-");    //Mês, dia e ano, respectivamente.
			day = aux[1];
			month = aux[0];
			year = aux[2];
			aux = line[1].split("\\.");    //Retirando a extensão do arquivo.
			aux = aux[0].split("-");    //Hora e minuto, respectivamente.
			hour = aux[0];
			minute = aux[1];
			
			
			try {    //Verificando integridade dos nomes dos arquivos.
				if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
					textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
					JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Integer.parseInt(year) < 1) {
					textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
					JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}				
				if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > getMaxDay((java.util.Date) (new SimpleDateFormat("dd/MM/yyyy")).parse("1/"+month+"/"+year))) {
					textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
					JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (Integer.parseInt(hour) < 0 || Integer.parseInt(hour) > 23) {
					textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
					JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Integer.parseInt(minute) < 0 || Integer.parseInt(day) > 59) {
					textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
					JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				} 
			} 
			catch (NumberFormatException n) 
			{
				textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
				JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			date = day+"/"+month+"/"+year;
			time = hour+":"+minute;
			line = null;
			
			/* Filtrando o arquivo de entrada de acordo com as palavras do arquivo 'stopwordsPath': */
			try {
				BufferedReader curFile = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+File.separatorChar+this.fileName), "ISO-8859-1"));    //A codificação dos arquivos deve ser a ISO-8859-1.
						
				//Descartando as três primeiras linhas de cada arquivo (cabeçalho):
				textFile += curFile.readLine()+"\n";
				textFile += curFile.readLine()+"\n";
				textFile += curFile.readLine()+"\n";
				
				ArrayList<Object[]> listFreq = new ArrayList<Object[]>();    //Necessário para calcular as frequências das palavras dentro do texto.
				String stopword = null;    //String auxiliar para armazenar o stopword atual (evita outro readLine()).
				
				while (curFile.ready()) {    //Lendo todas as linhas do arquivo atual.
					bufferLine = curFile.readLine();
					textFile += bufferLine+"\n";    //Criando uma cópia do arquivo de texto na memória.
					line = bufferLine.toLowerCase().split(" ");    //Separando todas as palavras de cada linha do texto.
					
					for (int idxLine = 0; idxLine < line.length; idxLine++) {    //Lendo cada palavra de cada linha.
						stopwordsFile = new BufferedReader(new FileReader(stopwordsPath));
						
						//Removendo caracteres especiais da string:
						String[] code = {"[", "]", "'", "<", ">", "\\", "|", "/", ".", ",", ";", "̣", "·", "!", "?", "#", "\\$", "%", "\"", "&", "*", "(", ")", "_", "+", "{", "}", "º", "ª", "´", "§", "¬", "¢", "£", "³", "²", "¹", "¬"};
						for (int idxCode = 0; idxCode < code.length; idxCode++) {
							/* Removendo os caracteres especiais de cada string (palavra): */
							line[idxLine] = line[idxLine].replace(code[idxCode], "");
						}
						line[idxLine] = line[idxLine].replaceAll("^\\s+", "");    //Removendo espaços em branco (\\s) no início da string (^), independente da quantidade (+).
						line[idxLine] = line[idxLine].replaceAll("\\s+$", "");    //Removendo espaços em branco (\\s) no fim da string ($), independente da quantidade (+).
						line[idxLine] = line[idxLine].toLowerCase();    //Deixando a string em minúsculas.
						
						while (stopwordsFile.ready()) {    //Verificando se a palavra atual é uma stopword.
							stopword = stopwordsFile.readLine().toLowerCase().trim();
							if (stopword.equals(line[idxLine]) || line[idxLine].equals("")) break;
						}
						
						if (line[idxLine].equals("")) continue;    //Excluindo palavras nulas, vazias.
						
						WordBase newWord = new WordBase(line[idxLine], freq.getColorStopwords());
						
						if (!stopword.equals(line[idxLine])) {    //Comprovando se a palavra atual é uma startword.
							newWord.setType(true);
							newWord.setColor(freq.getColorStartwords());
							freq.addStartwords(line[idxLine], index);    //Adicionando ou incrementando, caso exista, essa palavra na tabela de frequências.
						}
						else newWord.setType(false);    //A palavra é uma stopword.
						
						addFrequencies(listFreq, line[idxLine]);    //Computando as frequências das palavras, independente de serem startword.
						text.add(newWord);    //Adicionando uma nova palavra ao texto (ArrayList<Word>).
					}
				}
				curFile.close();    //Fechando o arquivo atual para leitura.
				
				/* Calculando as frequências das palavras no texto: */
				for (WordBase w : text) {
					w.setAbsoluteFrequency(getFrequency(listFreq, w.getWord()));
					w.setRelativeFrequency(Float.valueOf(String.valueOf(getFrequency(listFreq, w.getWord())))/Float.valueOf(text.size()));
				}
			} 
			catch (IOException excp) {			
				textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
				JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (ArrayIndexOutOfBoundsException a) {
			textFile = null;    //Controle para verificar se o arquivo foi processado corretamente.
			JOptionPane.showMessageDialog(null, "Ocorreu um erro de E/S no arquivo:\n"+fileName+".\nO arquivo pode estar corrompido ou\nnão ser do formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	public TextBase()    //Necessário para utilizar o construtor da classe filha (Text).
	{
	}
	
	
	public Text clone(final int idxText, final TextViewer panel, Configuration config)    //Clonagem real desse objeto (painel de texto) com independência de referências.
	{
		final Text clone = new Text();
		
		clone.setTextFile(textFile);
		clone.setFileName(fileName);
		clone.setText(new ArrayList<Word>());
		clone.setDate(this.date); 
		clone.setTime(this.time);
		clone.getTextBoard().setLayout(null);
		clone.getTextBoard().setBounds(0, 0, 8*text.size(), 8);
		clone.getTextBoard().setEditable(false);
		
		for (int idx = 0; idx < this.text.size(); idx++) {
			/* Copiando as configurações das palavras: */
			Word newWord = new Word(this.text.get(idx).getWord(), this.text.get(idx).getColor(), idx, config);
			newWord.setType(this.text.get(idx).startword);    //Verificando se a palavra é um startword (true) ou stopword (false).
			newWord.setAbsoluteFrequency(this.text.get(idx).getAbsoluteFrequency());
			newWord.setRelativeFrequency(this.text.get(idx).getRelativeFrequency());
			newWord.setInverseFrequency(this.text.get(idx).getInverseFrequency());
			newWord.joinFrequencies();
			newWord.getPix().addMouseListener(new MouseListener()    //Evento de atualização da identificação dos textos no painél (data e hora) de forma instantânea.
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
					panel.setDateLabel("");
					panel.setScheduleLabel("");
				}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					panel.setDateLabel(clone.getDate());
					panel.setScheduleLabel(clone.getTime().getText());
				}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					switch (e.getClickCount()) {
						case 1: {
							if (panel.isSelWords()) panel.removeAllSelections();
							clone.setSelected(!clone.isSelected()); break;
						}
						case 2: {
							ArrayList<String[]> texts = new ArrayList<String[]>();    //Matriz n(textos)x2(nome_arquivo, texto).
							Text[] objText = panel.getTexts();
							
							for (int i = 0; i < objText.length; i++) {    //Obtendo todos os textos na íntegra.
								String[] data = new String[2];
								
								data[0] = objText[i].getFileName();
								data[1] = objText[i].getTextFile();
								texts.add(data);
							}
							
							try {
								new TextSingle(texts, idxText);
							} 
							catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			});
			clone.setWord(newWord);    //Criando uma cópia do pixel para a nova estrutura de dados (a adição ao painél é feita na classe Text).
		}
		return(clone);
	}
	
	
	public ArrayList<WordBase> getText()
	{
		return(text);
	}
	
	
	public String getDate()
	{
		return(date);
	}
	
	
	public boolean exists()
	{
		return(textFile != null);
	}
	
	
	private int getMaxDay(java.util.Date date)    //Obtendo o último dia do mês.
	{
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(date);
		return(newDate.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	
	public void addFrequencies(ArrayList<Object[]> list, String word)
	{
		for (Object[] o : list) {
			if (((String) o[0]).equals(word)) {    //Aumentando a frequência dentro do texto.
				o[1] = ((Integer) o[1])+1;
				return;
			}
		}
		
		Object[] o = new Object[2];
		o[0] = word;
		o[1] = 1;
		list.add(o);    //Criando nova startword para computar a frequência.
	}
	
	
	public Integer getFrequency(ArrayList<Object[]> list, String word)
	{
		for (Object[] o : list) {
			if (((String) o[0]).equals(word)) {
				return(((Integer) o[1]));
			}
		}
		
		return(-1);
	}
	
}