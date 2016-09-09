package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

public class PushCommand implements TurtleCommand {

	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();

		TurtleState newState = new TurtleState();
		newState.x2d = state.x2d;
		newState.y2d = state.y2d;
		newState.angle = state.angle;
		newState.thickness = state.thickness;
		newState.length = state.length;
		
		turtleStates.push(newState);
	}

}
