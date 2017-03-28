
package IC;

import java.util.*;

public class AlgoritmoLocalizacao
{
	ArrayList<Sensor> aux = new ArrayList<>();
	public void LocalizarAncorasVizinhosTeste(Grafo grafo, Instancia regiao) 
	{
		ArrayList<Sensor> arraySensor = new ArrayList<>();
		
		arraySensor.addAll(grafo.getComuns());
		Collections.shuffle(arraySensor);
		int cont = 0;
		for(Sensor s : arraySensor)
			System.out.print(s.getID()+"  ");
		
		while(!arraySensor.isEmpty())
		{
			Iterator<Sensor> i = arraySensor.iterator();
			
			while(i.hasNext() && (cont < 10))
			{
				Sensor s = i.next();
				

				if((s.getNroVizinhosAncoras() + s.getNroVizinhosPos()) >= 3 && cont < 10)
				{
					System.out.println("\n 3+ vizinhos\n" + s.getID());
					s.imprimirVizinhosPos();
					NohLista[] ancoras = s.getVizinhosAncorasNovos();
					//Por enquanto nohs com mais de 3 vizinhos, estarao sendo escolhidos aleatoriamente
					Ponto p1 =  ancoras[0].getSensor().getPonto();
					Ponto p2 =  ancoras[1].getSensor().getPonto();
					Ponto p3 =  ancoras[2].getSensor().getPonto();
					
					double d1 = ancoras[0].getDist();
					double d2 = ancoras[1].getDist();
					double d3 = ancoras[2].getDist();
					
					Ponto pAnt = s.getPonto();
					Ponto p = Trilateracao(p1,p2,p3,d1,d2,d3);
					System.out.println("\n no"+" "+s.getID() + " Pontos x= : " + p.getX() + " y= " + p.getY() + "   cont= " + cont);	
					System.out.println("Vizinhos utilizados: " + ancoras[0].getSensor().getID() + "   " +  ancoras[1].getSensor().getID() + "  " + ancoras[2].getSensor().getID() + "\n ------------------------------" );
					if(/*Caso trilateracao retorne valor*/(pAnt.getX() != p.getX() || pAnt.getY() != p.getY()) && /*Caso esteja dentro da area*/((p.getX() >= 0  && p.getX() <= 1) && (p.getY() >= 0 && p.getY() <= 1)))
					{
						s.setPonto(p);
						s.AdcionarNroDeVizinho();
						cont = 0;
						i.remove();
					}
					else
					{
						cont++;
					}
	
				}

			}
			
			i = arraySensor.iterator();
			
			while(i.hasNext())
			{
				Sensor s = i.next();

				if(((s.getNroVizinhosAncoras() + s.getNroVizinhosPos()) == 2) || (cont == 10))
				{
					System.out.println("\nnó com 2 vizinhos: " +  s.getID());
				
					Ponto p1;
					Ponto p2;
					NohLista[] ancoras;
					//Por enquanto nohs com mais de 3 vizinhos, estarao sendo escolhidos aleatoriamente
					ancoras = s.getVizinhosAncorasNovos();
					p1 = ancoras[0].getSensor().getPonto();
					p2 = ancoras[1].getSensor().getPonto();
					
					System.out.println("Vizinhos utilizados: " + ancoras[0].getSensor().getID() + "   " +  ancoras[1].getSensor().getID() +"\n ------------------------------" );

					Ponto[] p = Interseccao2Circulos(p1,p2,ancoras[0].getDist(),ancoras[1].getDist() );
					
					if(p[0].getX() > 1 || p[0].getX() < 0 || p[0].getY() > 1 || p[0].getY() < 0)
						s.setPonto(p[1]);
					
					else if(p[1].getX() > 1 || p[1].getX() < 0 || p[1].getY() > 1 || p[1].getY() < 0)
						s.setPonto(p[0]);
					
					else
					{
						double x = s.CalcularErroDaPosicao(p[0], grafo, regiao);
						double y = s.CalcularErroDaPosicao(p[1], grafo, regiao);
					//set a pos de menor erro	
						if(x < y)
							s.setPonto(p[0]);
						else
							s.setPonto(p[1]);
					}
		
					s.AdcionarNroDeVizinho();
					if(cont == 10)cont = 0;
					i.remove();
				}

			}
			
			i = arraySensor.iterator();
			
			while(i.hasNext())
			{
				Sensor s = i.next();
				

				if((s.getNroVizinhosAncoras() + s.getNroVizinhosPos()) == 1)
				{
					System.out.println("\nnó com 1 vizinho: " +  s.getID());
					i.remove();
					
					NohLista n;
					NohLista[] ancoras;						
					ancoras = s.getVizinhosAncorasNovos();
					n = ancoras[0];
					
					
					Ponto p = PosicionarCom1Vizinho(n,s,grafo,regiao);
					
					s.setPonto(p);
					s.AdcionarNroDeVizinho();
					
				}


			}
		}
	}
	

	public void LocalizarAncorasVizinhosPQ(Grafo grafo, Instancia regiao)
	{

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
		
		while(!filaSensores.isEmpty())
		{
		
			while(((filaSensores.peek().getNroVizinhosAncoras() + filaSensores.peek().getNroVizinhosPos()) >= 3) && (cont< 10))
			{
				Sensor s = filaSensores.poll();
				System.out.println("\n 3+ vizinhos\n" + s.getID());
				s.imprimirVizinhosPos();
				Ponto pAnt;
				Ponto p;
				//Por enquanto nohs com mais de 3 vizinhos, estarao sendo escolhidos aleatoriamente
				NohLista[] ancoras = s.getVizinhosAncorasNovos();
				Ponto p1 =  ancoras[0].getSensor().getPonto();
				Ponto p2 =  ancoras[1].getSensor().getPonto();
				Ponto p3 =  ancoras[2].getSensor().getPonto();
	
				double d1 = ancoras[0].getDist();
				double d2 = ancoras[1].getDist();
				double d3 = ancoras[2].getDist();
				
				pAnt = s.getPonto();
				p = Trilateracao(p1,p2,p3,d1,d2,d3);
				
				System.out.println("\n no"+" "+s.getID() + " Pontos x= : " + p.getX() + " y= " + p.getY() + "   cont= " + cont);	
				System.out.println("Vizinhos utilizados: " + ancoras[0].getSensor().getID() + "   " +  ancoras[1].getSensor().getID() + "  " + ancoras[2].getSensor().getID() + "\n ------------------------------" );
				if(/*Caso trilateracao retorne valor*/(pAnt.getX() != p.getX() || pAnt.getY() != p.getY()) && /*Caso esteja dentro da area*/((p.getX() >= 0  && p.getX() <= 1) && (p.getY() >= 0 && p.getY() <= 1)))
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
				
				//atualizar PQ
				for(int i = 0; i < filaSensores.size(); i++)
					aux.add(filaSensores.poll());
				filaSensores.addAll(aux);
				aux.clear();
			}
		
			if(filaSensores.isEmpty())return;
			
			
			
			while(((filaSensores.peek().getNroVizinhosAncoras() + filaSensores.peek().getNroVizinhosPos()) == 2) || (cont == 10))
			{
				Sensor s = filaSensores.poll();
				System.out.println("\n 2 vizinhos\n" + s.getID());
				s.imprimirVizinhosPos();
				Ponto p1;
				Ponto p2;
				NohLista[] ancoras;
				//Por enquanto nohs com mais de 3 vizinhos, estarao sendo escolhidos aleatoriamente
				ancoras = s.getVizinhosAncorasNovos();
				p1 = ancoras[0].getSensor().getPonto();
				p2 = ancoras[1].getSensor().getPonto();
				
				System.out.println("Vizinhos utilizados: " + ancoras[0].getSensor().getID() + "   Distancia:  " +ancoras[0].getDist() + "     " +  ancoras[1].getSensor().getID() + "   Distancia:  " +ancoras[1].getDist() + "\n ------------------------------" );
				Ponto[] p = Interseccao2Circulos(p1,p2,ancoras[0].getDist(),ancoras[1].getDist() );
				
				if(p[0].getX() > 1 || p[0].getX() < 0 || p[0].getY() > 1 || p[0].getY() < 0)
					s.setPonto(p[1]);
				
				else if(p[1].getX() > 1 || p[1].getX() < 0 || p[1].getY() > 1 || p[1].getY() < 0)
					s.setPonto(p[0]);
				else
				{
					double x = s.CalcularErroDaPosicao(p[0], grafo, regiao);
					double y = s.CalcularErroDaPosicao(p[1], grafo, regiao);
				//set a pos de menor erro	
					if(x < y)
						s.setPonto(p[0]);
					else
						s.setPonto(p[1]);
				}

				
				
				s.AdcionarNroDeVizinho();
				
				if(cont == 10)cont = 0;
				
				if(filaSensores.isEmpty())
					break;
				
				//atualizar PQ
				for(int i = 0; i < filaSensores.size(); i++)
					aux.add(filaSensores.poll());
				filaSensores.addAll(aux);
				aux.clear();
			}
			
			if(filaSensores.isEmpty())return;
			
			while((filaSensores.peek().getNroVizinhosAncoras() + filaSensores.peek().getNroVizinhosPos()) == 1)
			{
				Sensor s = filaSensores.poll();
				System.out.println("\n 1 vizinho\n" + s.getID());
				s.imprimirVizinhosPos();
				NohLista n;
				NohLista[] ancoras;
	
				ancoras = s.getVizinhosAncorasNovos();
				n = ancoras[0];

				Ponto p = PosicionarCom1Vizinho(n,s,grafo,regiao);
				
				s.setPonto(p);
				s.AdcionarNroDeVizinho();
				
				if(filaSensores.isEmpty())
					break;
				
				//atualizar PQ
				for(int i = 0; i < filaSensores.size(); i++)
					aux.add(filaSensores.poll());
				filaSensores.addAll(aux);
				aux.clear();
			}
		}
	}
	
	
	private Ponto PosicionarCom1Vizinho(NohLista n,Sensor s, Grafo grafo, Instancia regiao) 
	{
		Ponto A = new Ponto(n.getSensor().getPonto().getX() + n.getDist() , n.getSensor().getPonto().getY());

		Ponto Aux = new Ponto(A.getX(),A.getY());
		
		double erroMin = Double.MAX_VALUE;
				
		for(int i = 0; i < 360; i++)
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

	private Ponto[] Interseccao2Circulos(Ponto p1, Ponto p2, double r1, double r2) 
	{
		/*
		https://stackoverflow.com/questions/3349125/circle-circle-intersection-points
		https://fypandroid.wordpress.com/2011/07/03/how-to-calculate-the-intersection-of-two-circles-java/
		*/
        double d = p1.Distancia2Pt(p2);
        double a = (r1*r1 - r2*r2 + d*d)/(2*d);
        double h = Math.sqrt(r1*r1 - a*a);
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
