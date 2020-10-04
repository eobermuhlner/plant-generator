package ch.obermuhlner.plantgen.ui.turtle;

public class TurtleState {
	public double x;
	public double y;

	public double xLeft;
	public double yLeft;

	public double xRight;
	public double yRight;
	
	public double angle;
	
	public double thickness;
	
	public double length;
	
	public void update() {
		xLeft = x + thickness * Math.cos(angle - Math.PI / 2);
		yLeft = y + thickness * Math.sin(angle - Math.PI / 2);
		xRight = x + thickness * Math.cos(angle + Math.PI / 2);
		yRight = y + thickness * Math.sin(angle + Math.PI / 2);
	}
}
