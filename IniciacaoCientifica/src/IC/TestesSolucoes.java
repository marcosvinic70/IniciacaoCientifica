package IC;

public class TestesSolucoes 
{
	private Grafo grafoBase;
	private Instancia regiao;
	private AlgoritmoLocalizacao alg;
	private Grafo melhorSol;
	
	public TestesSolucoes(Grafo grafoBase,Instancia regiao) 
	{
		this.regiao = regiao;
		alg = new AlgoritmoLocalizacao();
		
		try 
		{
			this.grafoBase = grafoBase.clone();
			melhorSol = grafoBase.clone();
			melhorSol.setErroTotal(Double.MAX_VALUE);
		} 
		catch (CloneNotSupportedException e) 
		{
			e.printStackTrace();
		}

		
	}
	
	public void LocalizarAncorasVizinhosTeste()
	{
		try 
		{
			Grafo grafoAux = grafoBase.clone();
			
			alg.LocalizarAncorasVizinhosTeste(grafoAux, regiao);
			
			escolherMelhorSolucao(grafoAux);
			
		} 
		catch (CloneNotSupportedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void LocalizarAncorasVizinhosPQ()
	{
		try 
		{
			Grafo grafoAux = grafoBase.clone();
			
			alg.LocalizarAncorasVizinhosPQ(grafoAux, regiao);
			
			escolherMelhorSolucao(grafoAux);
			
		} 
		catch (CloneNotSupportedException e) 
		{
			e.printStackTrace();
		}

	}
	
	private void escolherMelhorSolucao(Grafo grafoAux) throws CloneNotSupportedException 
	{
		if(grafoAux.getErroTotalCalculado() < melhorSol.getErroTotal())
			melhorSol = grafoAux;
	}

	public Grafo getGrafoBase() 
	{
		return grafoBase;
	}

	public void setGrafoBase(Grafo grafoBase) 
	{
		this.grafoBase = grafoBase;
	}

	public Grafo getMelhorSol() 
	{
		return melhorSol;
	}
	
}
