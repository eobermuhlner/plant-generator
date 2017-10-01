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

	public double getBaseLength(TurtleState turtleState) {
		return leafSize + turtleState.thickness * leafThicknessFactor;
	}

	public double getLeafLength(TurtleState turtleState) {
		return getBaseLength(turtleState) * leafLengthFactor;
	}
	
	public double getLeafWidth(TurtleState turtleState) {
		return getBaseLength(turtleState) * leafWidthFactor;
	}
	
	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();

		double length = getLeafLength(state);
		double width = getLeafWidth(state);
		
		double dxLeft = width * Math.cos(state.angle - widthAngle);
		double dyLeft = width * Math.sin(state.angle - widthAngle);

		double dxRight = width * Math.cos(state.angle + widthAngle);
		double dyRight = width * Math.sin(state.angle + widthAngle);

		double dxLength = length * Math.cos(state.angle);
		double dyLength = length * Math.sin(state.angle);
		
		gc.beginPath();
		gc.moveTo(state.x2d, state.y2d);
		gc.bezierCurveTo(state.x2d + dxLeft, state.y2d + dyLeft, state.x2d + dxLeft, state.y2d + dyLeft, state.x2d + dxLength, state.y2d + dyLength);
		gc.bezierCurveTo(state.x2d + dxRight, state.y2d + dyRight, state.x2d + dxRight, state.y2d + dyRight, state.x2d, state.y2d);
		gc.closePath();
		gc.fill();
	}

}
