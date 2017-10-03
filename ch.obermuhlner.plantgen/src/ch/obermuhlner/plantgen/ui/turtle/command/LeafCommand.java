package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class LeafCommand implements TurtleCommand {

	private final Color leaf1Color;
	private final Color leaf2Color;
	private final double colorCenterOffset;
	private final double color2Offset;
	private final double leafSize;
	private final double leafThicknessFactor;
	private final double leafLengthFactor;
	private final double leafWidthFactor;
	private final double widthAngle;

	public LeafCommand(Color leaf1Color, Color leaf2Color, double colorCenterOffset, double color2Offset, double leafSize, double leafThicknessFactor, double leafLengthFactor, double leafWidthFactor, double widthAngle) {
		this.leaf1Color = leaf1Color;
		this.leaf2Color = leaf2Color;
		this.colorCenterOffset = colorCenterOffset;
		this.color2Offset = color2Offset;
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
		double colorCenterLength = length * colorCenterOffset;
		
		double dxLeft = width * Math.cos(state.angle - widthAngle);
		double dyLeft = width * Math.sin(state.angle - widthAngle);

		double dxRight = width * Math.cos(state.angle + widthAngle);
		double dyRight = width * Math.sin(state.angle + widthAngle);

		double dxLength = length * Math.cos(state.angle);
		double dyLength = length * Math.sin(state.angle);

		double dxColorCenter = colorCenterLength * Math.cos(state.angle);
		double dyColorCenter = colorCenterLength * Math.sin(state.angle);
		
		RadialGradient radialGradient = new RadialGradient(0, 0, state.x + dxColorCenter, state.y + dyColorCenter, length, false, CycleMethod.NO_CYCLE, new Stop(0.0, leaf1Color), new Stop(color2Offset, leaf2Color));
		gc.setFill(radialGradient);

		gc.beginPath();
		gc.moveTo(state.x, state.y);
		gc.bezierCurveTo(state.x + dxLeft, state.y + dyLeft, state.x + dxLeft, state.y + dyLeft, state.x + dxLength, state.y + dyLength);
		gc.bezierCurveTo(state.x + dxRight, state.y + dyRight, state.x + dxRight, state.y + dyRight, state.x, state.y);
		gc.closePath();
		gc.fill();
	}

}
