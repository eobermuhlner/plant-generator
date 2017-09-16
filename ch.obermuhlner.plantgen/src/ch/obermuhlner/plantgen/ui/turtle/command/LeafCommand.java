package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

public class LeafCommand implements TurtleCommand {

	private double leafSize;
	private double leafThicknessFactor;
	private double leafLengthFactor;
	private double leafWidthFactor;
	private double widthAngle;

	public LeafCommand(double leafSize, double leafThicknessFactor, double leafLengthFactor, double leafWidthFactor, double widthAngle) {
		this.leafSize = leafSize;
		this.leafThicknessFactor = leafThicknessFactor;
		this.leafLengthFactor = leafLengthFactor;
		this.leafWidthFactor = leafWidthFactor;
		this.widthAngle = widthAngle;
	}
	
	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();

		double baseLength = leafSize + state.thickness * leafThicknessFactor;
		double length = baseLength * leafLengthFactor;
		double width = baseLength * leafWidthFactor;
		
		double dxLeft = width * Math.cos(state.angle - widthAngle);
		double dyLeft = width * Math.sin(state.angle - widthAngle);

		double dxRight = width * Math.cos(state.angle + widthAngle);
		double dyRight = width * Math.sin(state.angle + widthAngle);

		double dxLength = length * Math.cos(state.angle);
		double dyLength = length * Math.sin(state.angle);
		
		double[] xPoints = new double[] { state.x2d, state.x2d + dxLeft, state.x2d + dxLength, state.x2d + dxRight };
		double[] yPoints = new double[] { state.y2d, state.y2d + dyLeft, state.y2d + dyLength, state.y2d + dyRight };

		gc.setLineWidth(1.0);
		gc.fillPolygon(xPoints, yPoints, xPoints.length);
	}

}
