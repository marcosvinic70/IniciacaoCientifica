package IC;

import java.util.*;

public class Grafo implements Cloneable
{
	private int nroComuns;
	private int nroAncoras;
	private ArrayList<Sensor> ancoras;
	private ArrayList<Sensor> comuns;
	private double erroTotal;
	
	public Grafo(int nroComuns,int nroAncoras)
	{
		this.nroComuns = nroComuns;
		this.nroAncoras = nroAncoras;

		ancoras = new ArrayList<>();
		comuns = new ArrayList<>();
		
		preencherComuns();
	}
	
	private void preencherComuns()
	{
		for(int i = 0; i < nroComuns; i++)
		{
			Sensor s = new Sensor(i,-1.0,-1.0,false);
			comuns.add(s);
		}
	}
	
	public void addArestaDoAncora(int IDorigem, int IDdestino,double distancia)
	{
		Sensor origem = ancoras.get(IDorigem);
		Sensor destino = comuns.get(IDdestino);

		origem.adicionarVizinho(destino, distancia);
		destino.adicionarVizinho(origem, distancia);
	}
	
	public void addArestaDoComum(int IDorigem, int IDdestino,double distancia)
	{
		Sensor origem = comuns.get(IDorigem);
		Sensor destino = comuns.get(IDdestino);

		origem.adicionarVizinho(destino, distancia);
		destino.adicionarVizinho(origem, distancia);
	}
	
	public void Imprime()
	{
		for(int i = 0; i < nroAncoras; i++)
		{
			Sensor s = ancoras.get(i);
			System.out.print("\n\nNoh:" +  s.getID() + "    Pos x: " + s.getPonto().getX() + "  Pos y: "+ s.getPonto().getY() + " \n");
			s.imprimirVizinhos();
		}
		
		for(int i = 0; i < nroComuns; i++)
		{
			Sensor s = comuns.get(i);
			System.out.print("\n\nNoh:" + s.getID() + "    Pos x: " + s.getPonto().getX() + "  Pos y: "+ s.getPonto().getY() + " \n");
			s.imprimirVizinhos();
		}
	}
	
	private void calcularErroTotalSolucao()
	{
		double erroTotal = 0;
		
		for(Sensor s: comuns)
			erroTotal += s.getErroDaPosicao();
		this.erroTotal = erroTotal;
	}
	
	@Override
    public Grafo clone() throws CloneNotSupportedException 
	{
        Grafo grafo = new Grafo(nroComuns,nroAncoras);
        
        for(int i = 0; i < nroAncoras; i++)
		{	
			Sensor s = new Sensor(i,ancoras.get(i).getPonto().getX(),ancoras.get(i).getPonto().getY(),true);
			grafo.setListaAncoras(s);
		}

       
        for(int i = 0; i < nroAncoras; i++)
        {
        	Sensor s = ancoras.get(i);
        	
        	Collection<NohLista> aux = s.getVizinhosComuns().values();
        	
        	Iterator<NohLista> x = aux.iterator();
        	
        	for(int j = 0; j < aux.size(); j++)
        	{
        		NohLista n = x.next();
        		grafo.addArestaDoAncora(i, n.getSensor().getID(), n.getDist());
        	}
        }

        for(int i = 0; i < nroComuns; i++)
        {
        	Sensor s = comuns.get(i);
        	
        	Collection<NohLista> aux = s.getVizinhosComuns().values();
        	
        	Iterator<NohLista> x = aux.iterator();
       
        	for(int j = 0; j < aux.size(); j++)
        	{
        		NohLista n = x.next();
        		grafo.addArestaDoComum(i, n.getSensor().getID(), n.getDist());
        	}
        	
        }
        
        return grafo;
    }
	
	public ArrayList<Sensor> getAncoras() 
	{
		return ancoras;
	}

	public int getNroComuns()
	{
		return nroComuns;
	}
	
	public int getNroAncoras()
	{
		return nroAncoras;
	}
	
	public void setListaAncoras(Sensor s)
	{
		ancoras.add(s);
	}

	public ArrayList<Sensor> getComuns()
	{
		return comuns;
	}
	public void setErroTotal(double erroTotal)
	{
		this.erroTotal = erroTotal;
	}
	public double getErroTotalCalculado()
	{
		calcularErroTotalSolucao();
		return erroTotal;
	}
	
	public double getErroTotal()
	{
		return erroTotal;
	}
		
}
