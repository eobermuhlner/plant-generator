package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

public class LeafCommand implements TurtleCommand {

	private double leafFactor;
	private double leafThicknessFactor;

	public LeafCommand(double leafFactor, double leafThicknessFactor) {
		this.leafFactor = leafFactor;
		this.leafThicknessFactor = leafThicknessFactor;
	}
	
	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();

		double radius = leafFactor + state.thickness * leafThicknessFactor;
		
		double dx = radius * Math.cos(state.angle);
		double dy = radius * Math.sin(state.angle);
		
		gc.setLineWidth(radius);
		gc.strokeLine(state.x2d, state.y2d, state.x2d+dx, state.y2d+dy);

	}

}
