package IC;

public class Programa
{
	public static void main(String[] args) throws CloneNotSupportedException
	{
		ArquivoInstancia Arq = new ArquivoInstancia(args[0]);
		
		Instancia regiao = Arq.LerInstancia();
		
		Grafo grafoBase = Arq.LerGrafo(regiao.getNroComuns(),regiao.getNroAncoras());
						
		TestesSolucoes testes = new TestesSolucoes(grafoBase,regiao);
		
		testes.LocalizarAncorasVizinhosPQ();
	
		
		//if(regiao.getRuido() > 0)
		//{
			for(int i = 0; i < 10; i++)
				testes.LocalizarAncorasVizinhosTeste();
		//}
		testes.getMelhorSol().Imprime();
		Arq.EscreverResultInstancia(testes.getMelhorSol());

	}
}
