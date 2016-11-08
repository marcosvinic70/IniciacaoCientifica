package IC;

public class Instancia 
{
	private double regiaoMin;
	private double regiaoMax;
	private double alcanceRadio;
	private double ruido;
	private int nroComuns;
	private int nroAncoras;
	
	public Instancia(double rMin, double rMax, double alcanceRadio, double ruido,int nroComuns, int nroAncoras)
	{
		this.regiaoMin = rMin;
		this.regiaoMax = rMax;
		this.alcanceRadio = alcanceRadio;
		this.ruido = ruido;
		this.nroComuns = nroComuns;
		this.nroAncoras = nroAncoras;
	}
	
	public int getNroComuns() 
	{
		return nroComuns;
	}

	public int getNroAncoras() 
	{
		return nroAncoras;
	}
	
	public double getReMin()
	{
		return regiaoMin; 
	}
	
	public double getReMax()
	{
		return regiaoMax; 
	}
	
	public double getRadio()
	{
		return alcanceRadio; 
	}
	
	public double getRuido()
	{
		return ruido; 
	}
	
	public void imprimirRegiao()
	{
		System.out.println(regiaoMin + "\n" + regiaoMax + "\n" + nroComuns + "\n" + nroAncoras + "\n" + alcanceRadio + "\n" + ruido);
	}
}
