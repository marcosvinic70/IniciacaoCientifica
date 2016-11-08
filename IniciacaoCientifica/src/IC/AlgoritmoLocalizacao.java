
package IC;

import java.util.*;

public class AlgoritmoLocalizacao
{

    // Obtendo muitos NaN
   
	public void LocalizarAncorasVizinhosTeste(Grafo grafo, Instancia regiao) 
	{
		ArrayList<Sensor> arraySensor = new ArrayList<>();
		
		arraySensor.addAll(grafo.getComuns());
		
		Collections.shuffle(arraySensor);
		int cont = 0;
		
		while(!arraySensor.isEmpty())
		{
			Iterator<Sensor> i = arraySensor.iterator();

			while(i.hasNext())
			{
				Sensor s = i.next();
				
				if((s.getNroVizinhosAncoras() + s.getNroVizinhosPos()) >= 3 && cont < 10)
				{
					
					System.out.println("oi  " + s.getID());


					NohLista[] ancoras = s.getVizinhosAncorasNovos();
	
					Ponto p1 =  ancoras[0].getSensor().getPonto();
					Ponto p2 =  ancoras[1].getSensor().getPonto();
					Ponto p3 =  ancoras[2].getSensor().getPonto();
					
					double d1 = ancoras[0].getDist();
					double d2 = ancoras[1].getDist();
					double d3 = ancoras[2].getDist();
					
					Ponto pAnt = s.getPonto();
					Ponto p = Trilateracao(p1,p2,p3,d1,d2,d3);
					
					if(p.getX() > 1 || p.getX() < 0 || p.getY() > 1 || p.getY() < 0)
					{
						cont++;
						continue;
						
						
						//Loop infinito quando não obtem resposta viavel, porem travando no cont = 10
					}
						
					
					if(pAnt.getX() != p.getX() || pAnt.getY() != p.getY())
					{
						s.setPonto(p);
						s.AdcionarNroDeVizinho();
						cont = 0;
					}
					else
					{
						cont++;
						continue;
					}
					
					
					i.remove();
				}
			}
			
			i = arraySensor.iterator();
			
			while(i.hasNext())
			{
				Sensor s = i.next();
				
				if((s.getNroVizinhosAncoras() + s.getNroVizinhosPos()) == 2)
				{
					i.remove();
	
					Random r = new Random();
					Ponto p1;
					Ponto p2;
					NohLista[] ancoras;
					
					if((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()) > 2)
					{
						ancoras = s.getVizinhosAncorasNovos();
						p1 = ancoras[r.nextInt((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()))].getSensor().getPonto();
						p2 = ancoras[r.nextInt((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()))].getSensor().getPonto();
					}	
					else
					{
						ancoras = s.getVizinhosAncorasNovos();
						p1 = ancoras[0].getSensor().getPonto();
						p2 = ancoras[1].getSensor().getPonto();
					}
					
					Ponto[] p = Interseccao2Circulos(p1,p2,regiao.getRadio());
					
					if(p[0].getX() > 1 || p[0].getX() < 0 || p[0].getY() > 1 || p[0].getY() < 0)
						s.setPonto(p[1]);
					
					else if(p[1].getX() > 1 || p[1].getX() < 0 || p[1].getY() > 1 || p[1].getY() < 0)
						s.setPonto(p[0]);
					
					else
						s.setPonto(p[r.nextInt(2)]);
						
					s.AdcionarNroDeVizinho();
					
				}

			}
			
			i = arraySensor.iterator();
			while(i.hasNext())
			{
				Sensor s = i.next();
				
				if((s.getNroVizinhosAncoras() + s.getNroVizinhosPos()) == 1)
				{
					i.remove();
					
					NohLista n;
					NohLista[] ancoras;
					Random r = new Random();

					if((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()) > 1)
					{
						ancoras = s.getVizinhosAncorasNovos();
						n = ancoras[r.nextInt((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()))];
					}	
					else
					{
						ancoras = s.getVizinhosAncorasNovos();
						n = ancoras[0];
					}
					
					Ponto p = PosicionarCom1Vizinho(n,s,grafo,regiao);
					
					s.setPonto(p);
					s.AdcionarNroDeVizinho();
					
				}
				
			}
		}
	}
	

	public void LocalizarAncorasVizinhosPQ(Grafo grafo, Instancia regiao)
	{
    	// Obs 3: sempre que uma solução gerada (por qualquer dos métodos) estiver fora da área da rede (< 0 ou > 1) deve ser descartada

		PriorityQueue<Sensor> filaSensores = new PriorityQueue<Sensor>(grafo.getNroComuns(), 
				new Comparator<Sensor>() 
			{	
		        public int compare(Sensor s1, Sensor s2) 
		        {
		        	if((s1.getNroVizinhosAncoras() + s1.getNroVizinhosPos()) > (s2.getNroVizinhosAncoras() + s2.getNroVizinhosPos()))
		        		return -1;
		        	else if((s1.getNroVizinhosAncoras() + s1.getNroVizinhosPos()) < (s2.getNroVizinhosAncoras() + s2.getNroVizinhosPos()))
		        		return 1;
		        	else
		        		return 0;
		        }
			}
		);
		
		filaSensores.addAll(grafo.getComuns());
		int cont = 0;
		
		if(filaSensores.isEmpty())return;
		
		while((filaSensores.peek().getNroVizinhosAncoras() + filaSensores.peek().getNroVizinhosPos()) >= 3 && cont < 50/*limite qlqer*/ )
		{
			Sensor s = filaSensores.poll();

			NohLista[] ancoras = s.getVizinhosAncorasNovos();
			Ponto p1 =  ancoras[0].getSensor().getPonto();
			Ponto p2 =  ancoras[1].getSensor().getPonto();
			Ponto p3 =  ancoras[2].getSensor().getPonto();
			
			double d1 = ancoras[0].getDist();
			double d2 = ancoras[1].getDist();
			double d3 = ancoras[2].getDist();
			
			Ponto pAnt = s.getPonto();
			Ponto p = Trilateracao(p1,p2,p3,d1,d2,d3);
			
			if(pAnt.getX() != p.getX() || pAnt.getY() != p.getY())
			{
				s.setPonto(p);
				s.AdcionarNroDeVizinho();
				cont = 0;
			}
			else
			{
				cont++;
				filaSensores.add(s);
			}

			if(filaSensores.isEmpty())
				break;

		}
		
		if(filaSensores.isEmpty())return;
		
		
		
		// Obs 1: pode pegar caras que tenham mais de 2 vizinhos e, nesse caso, precisa sortear os dois que vão pegar
		while((filaSensores.peek().getNroVizinhosAncoras() + filaSensores.peek().getNroVizinhosPos()) >= 2)
		{
			Sensor s = filaSensores.poll();
			Random r = new Random();
			Ponto p1;
			Ponto p2;
			NohLista[] ancoras;
			
			if((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()) > 2)
			{
				ancoras = s.getVizinhosAncorasNovos();
				p1 = ancoras[r.nextInt((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()))].getSensor().getPonto();
				p2 = ancoras[r.nextInt((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()))].getSensor().getPonto();
			}	
			else
			{
				ancoras = s.getVizinhosAncorasNovos();
				p1 = ancoras[0].getSensor().getPonto();
				p2 = ancoras[1].getSensor().getPonto();
			}
			
			Ponto[] p = Interseccao2Circulos(p1,p2,regiao.getRadio());
			
			if(p[0].getX() > 1 || p[0].getX() < 0 || p[0].getY() > 1 || p[0].getY() < 0)
				s.setPonto(p[1]);
			
			else if(p[1].getX() > 1 || p[1].getX() < 0 || p[1].getY() > 1 || p[1].getY() < 0)
				s.setPonto(p[0]);
			
			else
				s.setPonto(p[r.nextInt(2)]);
				
			s.AdcionarNroDeVizinho();
			
			if(filaSensores.isEmpty())
				break;

		}
		
		if(filaSensores.isEmpty())return;
		
		// Obs 2: mesma anotação do while anterior
		while((filaSensores.peek().getNroVizinhosAncoras() + filaSensores.peek().getNroVizinhosPos()) >= 1)
		{
			Sensor s = filaSensores.poll();
			NohLista n;
			NohLista[] ancoras;
			Random r = new Random();

			if((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()) > 1)
			{
				ancoras = s.getVizinhosAncorasNovos();
				n = ancoras[r.nextInt((s.getNroVizinhosPos() + s.getNroVizinhosAncoras()))];
			}	
			else
			{
				ancoras = s.getVizinhosAncorasNovos();
				n = ancoras[0];
			}
			
			Ponto p = PosicionarCom1Vizinho(n,s,grafo,regiao);
			
			s.setPonto(p);
			s.AdcionarNroDeVizinho();
			
			if(filaSensores.isEmpty())
				break;

		}
		
	}
	
	
	private Ponto PosicionarCom1Vizinho(NohLista n,Sensor s, Grafo grafo, Instancia regiao) 
	{
		Ponto A = new Ponto(n.getSensor().getPonto().getX() + n.getDist() , n.getSensor().getPonto().getY());

		Ponto Aux = new Ponto(A.getX(),A.getY());
		
		double erroMin = Double.MAX_VALUE;
		
		// Obs 5: alterado para 360 e mais abaixo coordenada somada com do nó N
		
		for(int i = 5; i < 360; i+=5)
		{		
			double erro = s.CalcularErroMin1Vizinho(Aux,grafo,regiao);
			
			if(erro < erroMin)
			{
				if((Aux.getX() <= 1 && Aux.getX() >= 0) && (Aux.getY() <= 1 && Aux.getY() >= 0))
				{
					erroMin = erro;
					A.setX(Aux.getX());
					A.setY(Aux.getY());
				}
			}
			
			Aux.setX(n.getSensor().getPonto().getX() + Math.cos(Math.toRadians(i)) * n.getDist());
			Aux.setY(n.getSensor().getPonto().getY() + Math.sin(Math.toRadians(i)) * n.getDist());
		}
		
		return A;
	}

	private Ponto[] Interseccao2Circulos(Ponto p1, Ponto p2, double r) 
	{
		/*
		https://stackoverflow.com/questions/3349125/circle-circle-intersection-points
		https://fypandroid.wordpress.com/2011/07/03/how-to-calculate-the-intersection-of-two-circles-java/
		*/
        double d = p1.Distancia2Pt(p2);
        double a = (r*r - r*r + d*d)/(2*d);
        double h = Math.sqrt(r*r - a*a);
        double p3x = p1.getX() + (((p2.getX() - p1.getX())* a) /d);
        double p3y = p1.getY() + (((p2.getY() - p1.getY())* a) /d);
        
        double x3 = p3x + h*(p2.getY() - p1.getY())/d;
        double y3 = p3y - h*(p2.getX() - p1.getX())/d;
        double x4 = p3x - h*(p2.getY() - p1.getY())/d;
        double y4 = p3y + h*(p2.getX() - p1.getX())/d;
 
        return new Ponto[]{new Ponto(x3, y3),new Ponto(x4, y4)};
	
	}	

	private Ponto Trilateracao(Ponto p1, Ponto p2, Ponto p3, double d1, double d2, double d3)
	{
		
		double x1sq = p1.getX() * p1.getX();
		double x2sq = p2.getX() * p2.getX();
		double x3sq = p3.getX() * p3.getX();
		double y1sq = p1.getY() * p1.getY();
		double y2sq = p2.getY() * p2.getY();
		double y3sq = p3.getY() * p3.getY();
		double d1sq = d1 * d1;
		double d2sq = d2 * d2;
		double d3sq = d3 * d3;
		
			
		double numerador1 = (p2.getX() - p1.getX()) * (x3sq + y3sq - d3sq) + (p1.getX() - p3.getX()) * (x2sq + y2sq - d2sq) + (p3.getX() - p2.getX()) * (x1sq  + y1sq - d1sq);
		
		double denominador1 = 2 * (p3.getY() * (p2.getX() - p1.getX()) + p2.getY() * (p1.getX() - p3.getX()) + p1.getY() * (p3.getX() - p2.getX()));
		
		double y = numerador1/denominador1;
		
		double numerador2 = d2sq - d1sq + x1sq - x2sq + y1sq - y2sq - 2 * (p1.getY() - p2.getY()) * y;
		
		double denominador2 = 2 * (p1.getX() - p2.getX());
		
		double x = numerador2/denominador2;
		Ponto pontoResult = new Ponto(x,y);
		return pontoResult;
	}


	
}
