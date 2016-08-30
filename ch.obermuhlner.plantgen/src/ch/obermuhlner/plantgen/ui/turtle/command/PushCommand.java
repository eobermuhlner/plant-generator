package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class PushCommand implements TurtleCommand {

	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();

		TurtleState newState = new TurtleState();
		newState.x = state.x;
		newState.y = state.y;
		newState.angle = state.angle;
		
		turtleStates.push(newState);
	}

}
