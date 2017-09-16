package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

public class LeafCommand implements TurtleCommand {

	private double leafFactor;
	private double leafThicknessFactor;
	private double leafLengthFactor;

	public LeafCommand(double leafFactor, double leafThicknessFactor, double leafLengthFactor) {
		this.leafFactor = leafFactor;
		this.leafThicknessFactor = leafThicknessFactor;
		this.leafLengthFactor = leafLengthFactor;
	}
	
	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();

		double radius = leafFactor + state.thickness * leafThicknessFactor;
		
		double dxLeft = radius * 0.5 * Math.cos(state.angle - Math.PI/2);
		double dyLeft = radius * 0.5 * Math.sin(state.angle - Math.PI/2);

		double dxRight = radius * 0.5 * Math.cos(state.angle + Math.PI/2);
		double dyRight = radius * 0.5 * Math.sin(state.angle + Math.PI/2);

		double dx = radius * leafLengthFactor * Math.cos(state.angle);
		double dy = radius * leafLengthFactor * Math.sin(state.angle);
		
		double[] xPoints = new double[] { state.x2d, state.x2d + dxLeft, state.x2d + dx, state.x2d + dxRight };
		double[] yPoints = new double[] { state.y2d, state.y2d + dyLeft, state.y2d + dy, state.y2d + dyRight };

		gc.setLineWidth(1.0);
		gc.fillPolygon(xPoints, yPoints, xPoints.length);
	}

}
