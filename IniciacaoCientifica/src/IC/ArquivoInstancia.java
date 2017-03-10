package IC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ArquivoInstancia 
{
	private Scanner scan;
	private File file;
	private FileReader fr;
	private FileWriter fw;
	private BufferedWriter bw;
	private String arg;
	
	public ArquivoInstancia(String args)
	{
		arg = args;
		
		try 
		{
			String entrada = "./Instancias_4.3_01_e_02/".concat(arg);
			file = new File(entrada);
			fr = new FileReader(file);
			scan = new Scanner(fr);
		}
		catch (IOException e)
		{
			System.err.printf("Erro na abertura do arquivo instancia: %s.\n",e.getMessage());
		}
	}
	

	public Instancia LerInstancia()
	{
		double rMin = Double.valueOf(scan.next());
		double rMax = Double.valueOf(scan.next());
		
		int nroComuns = scan.nextInt();
		int nroAncoras = scan.nextInt();
		
		double radio =  Double.valueOf(scan.next());
		double ruido =  Double.valueOf(scan.next());
		
		Instancia regiao =  new Instancia(rMin,rMax,radio,ruido,nroComuns,nroAncoras);
		
		return regiao;
		
	}

	public Grafo LerGrafo(int nroComuns, int nroAncoras)
	{
		Grafo grafo = new Grafo(nroComuns,nroAncoras);

		leituraAncoras(nroAncoras,grafo);
		
		leituraArestas(nroAncoras,nroComuns,grafo);

		return grafo;
		
	}
	
	private void leituraAncoras(int nroAncoras, Grafo grafo)
	{
		for(int i = 0; i < nroAncoras; i++)
		{	
			Sensor s = new Sensor(i,Double.valueOf(scan.next()),Double.valueOf(scan.next()),true);
			grafo.setListaAncoras(s);
		}
	}
	
	private void leituraArestas(int nroAncoras, int nroComuns, Grafo grafo)
	{
		double distancia;
		int IDorigem;
		int IDdestino;

		for(int i = 0;i < nroAncoras;i++)
		{
			IDorigem = scan.nextInt();
			IDdestino = scan.nextInt();
			
			while(IDdestino != -1)
			{
				distancia = Double.valueOf(scan.next());
				grafo.addArestaDoAncora(IDorigem, IDdestino,distancia);
				
				IDdestino = scan.nextInt();
			}
			scan.nextInt();
		}
		
		for(int i = 0; i < nroComuns; i++)
		{	
			
			IDorigem = scan.nextInt();
			IDdestino = scan.nextInt();
			
			while(IDdestino != -1)
			{
				distancia = Double.valueOf(scan.next());
				grafo.addArestaDoComum(IDorigem, IDdestino,distancia);
				IDdestino = scan.nextInt();
			}
			scan.nextInt();
		}
	}

	public void EscreverResultInstancia(Grafo grafo)
	{
		try
		{
			String nomeInstancia = arg.substring(0, (arg.indexOf("l")));
			String saida = "./Instancias_4.3_01_e_02/".concat(nomeInstancia).concat("MyResult.txt");
			file = new File(saida);
			file.createNewFile();
		
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
	
			ArrayList<Sensor> comuns = grafo.getComuns();
			
			for(Sensor s : comuns)
			{
				
				String x = (String.format("%.12f", s.getPonto().getX())).replaceAll(",", ".");
				String y = (String.format("%.12f", s.getPonto().getY())).replaceAll(",", ".");
	
				bw.write(x + " " + y);
				bw.newLine();
				
			}
		}
		catch(Exception e)
		{
			System.err.printf("Erro na escrita do arquivo resultado: %s.\n",e.getMessage());
		}
		finally
		{
			try
			{
				bw.close();
			
				scan.close();
				fr.close();
				fw.close();
				bw.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}
}
