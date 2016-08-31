package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class LeafCommand implements TurtleCommand {

	private double leafFactor;

	public LeafCommand(double leafFactor) {
		this.leafFactor = leafFactor;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();

		double radius = state.thickness * leafFactor;
		
		double top = state.x - radius;
		double left = state.y - radius;
		
		gc.fillOval(top, left, radius * 2, radius * 2);
	}

}
