package IC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Sensor
{
	private int ID;
	private Ponto ponto;
	private boolean ehAncora;
	private HashMap<Integer,NohLista> vizinhosComuns;
	private HashMap<Integer,NohLista> vizinhosAncoras;
	private int nroVizinhosPos;
	private double erroDaPosicao;
	
	public Sensor(int ID, double x, double y,boolean ehAncora)
	{
		ponto = new Ponto(x,y);
		this.ID = ID;
		this.ehAncora = ehAncora;
		vizinhosComuns = new HashMap<Integer,NohLista>();
		vizinhosAncoras = new HashMap<Integer,NohLista>();	
	}

	public void	adicionarVizinho(Sensor vizinho, double distancia)
	{
		NohLista n = new NohLista(vizinho, distancia);
		
		if(vizinho.isAncora())
			vizinhosAncoras.put(vizinho.getID(),n);

		else
			vizinhosComuns.put(vizinho.getID(),n);
	}
	
	public NohLista[] getVizinhosAncorasNovos()
	{
		Collection<NohLista> c = listaDeNosPosicionados();
		NohLista[] n;
		
		if(c.size() >= 3)
		{
			int nos[] = escolhaAleatoriaDeNos(c);
			
			n = new NohLista[]{getNohEscolhido(nos[0],c),getNohEscolhido(nos[1],c),getNohEscolhido(nos[2],c)};

			return n;
		}
		else if(c.size() == 2)
		{
			Iterator<NohLista> i = c.iterator();
			
			n = new NohLista[]{i.next(),i.next()};
			
			return n;
			
		}
		else if(c.size() == 1)
		{
			Iterator<NohLista> i = c.iterator();
			n = new NohLista[]{i.next()};
			return n;
		}
		
		return null;
		
	}
	
	private NohLista getNohEscolhido(int x, Collection<NohLista> z) 
	{
		Iterator<NohLista> i = z.iterator();

		NohLista no = i.next();
		int cont = 1;
		
		while(cont != x)
		{
			no = i.next();
			cont++;
		}
		return no;
	}
	
	private Collection<NohLista> listaDeNosPosicionados() 
	{
		Collection<NohLista> a = vizinhosAncoras.values();
		Collection<NohLista> b = vizinhosComuns.values();
		Collection<NohLista> c = new ArrayList<NohLista>();
		Iterator<NohLista> i = b.iterator();
		
		int j = 0;
		c.addAll(a);
		
		while(j != vizinhosComuns.size())
		{
			NohLista x = i.next();
			
			if(x.getSensor().getPonto().getX() != -1)
			{
				c.add(x);
			}
			j++;
		}
		return c;
	}

	private int[] escolhaAleatoriaDeNos(Collection<NohLista> x) 
	{
		int no1;
		int no2;
		int no3;
		
		Random r = new Random();
		do
		{
			no1 = r.nextInt(x.size()+1);
			
			no2 = r.nextInt(x.size()+1);
			
			no3 = r.nextInt(x.size()+1);
			
		}while(no1 == no2 || no1 == no3 || no2 == no3 || no1 == 0|| no2 == 0|| no3 == 0);

		int nos[] = new int[]{no1,no2,no3};
		
		return nos;
	}
	
	public double CalcularErroDaPosicao(Ponto p, Grafo g, Instancia i)
	{
		double erro = 0;
		
		ArrayList<Sensor> nohs = new ArrayList<>();
		nohs.addAll(g.getAncoras());
		nohs.addAll(g.getComuns());
		
		for(Sensor s : nohs)
		{
			if(s.getPonto().getX() != -1)
			{
				double d = CalcDistancia(s,p);

				if(vizinhosComuns.containsKey(s.getID()) && !s.isAncora())
				{
					double e = Math.abs(d - vizinhosComuns.get(s.getID()).getDist());
					
					if(e >= 2*i.getRuido())
						erro +=e; 
				}
				
				else if(vizinhosAncoras.containsKey(s.getID()) && s.isAncora())
				{
					double e = Math.abs(d - vizinhosAncoras.get(s.getID()).getDist());
					
					if(e >= 2*i.getRuido())
						erro +=e; 
				}

				else
					if(d < i.getRadio() && s.getID() != ID)
					{
						double e = Math.abs(d - i.getRadio());
						
						if(e >= 2*i.getRuido())
							erro +=e; 
					}
			}
		}
		erroDaPosicao = erro;
		System.out.println("\n errooooooooooooooooooooooo " + erro);
		return erro;
		
	}
	
	public double CalcularErroMin1Vizinho(Ponto p, Grafo g, Instancia i) 
	{
		double erro = 0;
		
		ArrayList<Sensor> nohs = new ArrayList<>();
		
		nohs.addAll(g.getAncoras());
		nohs.addAll(g.getComuns());
	
		for(Sensor s : nohs)
		{
			if(s.getPonto().getX() != -1)
			{
				
				double d = CalcDistancia(s,p);
				
				if(!vizinhosComuns.containsKey(s.getID()) && !s.isAncora() && d < i.getRadio())
				{
					double e = Math.abs(d - i.getRadio());
					
					if(e >= 2 * i.getRuido() )
						erro +=e; 
					
				}
				else if(!vizinhosAncoras.containsKey(s.getID()) && s.isAncora() && d < i.getRadio())
				{
					double e = Math.abs(d - i.getRadio());
					
					if(e >= 2 * i.getRuido() )
						erro +=e; 
				}
			}
		}
		return erro;
	}

	private double CalcDistancia(Sensor s, Ponto p)
	{

		String res = String.format("%.12f",Math.sqrt( Math.pow( ( p.getX() - s.getPonto().getX() ),2 ) + Math.pow( (  p.getY() - s.getPonto().getY()),2 ) ));
		
		res = res.replace(",",".");
		Double d = Double.parseDouble(res);
		
		return d;
		
	}
	public void AdcionarNroDeVizinho()
	{
		Collection<NohLista> aux = vizinhosComuns.values();
		
		for(NohLista x : aux)
		{
			x.getSensor().setNroVizinhosPos(x.getSensor().getNroVizinhosPos() + 1);
		}
	}

	public void imprimirVizinhos()
	{
		System.out.print("Vizinhos comuns: ");

		Collection<NohLista> a = vizinhosComuns.values();
		
		for(NohLista x : a)
		{
			System.out.print(x.getSensor().getID() + " ");
		}	

		System.out.print("\n        Vizinhos ancoras: ");
		
		Collection<NohLista> c = vizinhosAncoras.values();
		
		for(NohLista n : c)
		{
			System.out.print(n.getSensor().getID() + " ");
		}
	}
	public void imprimirVizinhosPos()
	{
		System.out.print("Vizinhos comuns posicionados: ");

		Collection<NohLista> a = vizinhosComuns.values();
		
		for(NohLista x : a)
		{
			if(x.getSensor().getPonto().getX() != -1)
			System.out.print(x.getSensor().getID() + " ");
		}	

		System.out.print("\n        Vizinhos ancoras: ");
		
		Collection<NohLista> c = vizinhosAncoras.values();
		
		for(NohLista n : c)
		{
			System.out.print(n.getSensor().getID() + " ");
		}
	}	
 	public Ponto getPonto()
	{
		return ponto;
	}

 	public void setPonto(Ponto ponto)
 	{
 		this.ponto = ponto;
 	}
 
 	public boolean isAncora()
	{
		return ehAncora;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public int getNroVizinhosAncoras()
	{
		return vizinhosAncoras.size();
	}

	public int getNroVizinhosPos() 
	{
		return nroVizinhosPos;
	}

	public void setNroVizinhosPos(int nroVizinhosPos) 
	{
		this.nroVizinhosPos = nroVizinhosPos;
	}

	public HashMap<Integer, NohLista> getVizinhosComuns()
	{
		return vizinhosComuns;
	}

	public double getErroDaPosicao() {
		return erroDaPosicao;
	}

	public void setErroDaPosicao(int erroDaPosicao) {
		this.erroDaPosicao = erroDaPosicao;
	}

}
