package ch.obermuhlner.plantgen.ui.turtle;

public class TurtleState {
	public double x;
	public double y;

	public double angle;
	
	public double thickness;
	
	public double length;

	@Override
	public String toString() {
		return "TurtleState [x=" + x + ", y=" + y + ", angle=" + angle + ", thickness=" + thickness + ", length="
				+ length + "]";
	}
	
}
