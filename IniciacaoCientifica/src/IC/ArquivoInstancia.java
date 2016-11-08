/*
4.3_01-0_loc ok
4.3_01-1_loc ok
4.3_01-2_loc erro
4.3_01-3_loc ok
4.3_01-4_loc ok
4.3_01-5_loc ok
4.3_01-6_loc ok
4.3_01-7_loc ok
4.3_01-8_loc ok
4.3_01-9_loc ok
4.3_02-0_loc ok
4.3_02-1_loc ok
4.3_02-2_loc ok
4.3_02-3_loc ok
4.3_02-4_loc ok
4.3_02-5_loc erro
4.3_02-6_loc ok
4.3_02-7_loc erro
4.3_02-8_loc erro
4.3_02-9_loc ok

*/
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
	
	public ArquivoInstancia()
	{
		try 
		{
			file = new File("./4.1-1_loc.txt");
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
			file = new File("resultado.txt");
			file.createNewFile();
		
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
	
			ArrayList<Sensor> comuns = grafo.getComuns();
			
			for(Sensor s : comuns)
			{
				
				String x = (String.format("%.7f", s.getPonto().getX())).replaceAll(",", ".");
				String y = (String.format("%.7f", s.getPonto().getY())).replaceAll(",", ".");
	
				bw.write(x + "   " + y);
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
