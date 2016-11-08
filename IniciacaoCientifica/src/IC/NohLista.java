package IC;

public class NohLista 
{
	private Sensor sensor;
	private double distancia;

	public NohLista(Sensor sensor, double distancia)
	{
		this.sensor = sensor;
		this.distancia = distancia;
	}
	
	public NohLista() 
	{
	}

	public double getDist()
	{
		return distancia;
	}
	
	public Sensor getSensor() 
	{
		return sensor;
	}
}