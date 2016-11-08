package IC;

public class Ponto
{
	private double x;
	private double y;
	
	public Ponto(double x,double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double Distancia2Pt(Ponto p2) 
	{
		return Math.sqrt( Math.pow( ( x - p2.getX() ),2 ) + Math.pow( ( y - p2.getY()),2 ));
	}
	
	public double getX() 
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	



}
