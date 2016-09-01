package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class LengthFactorCommand implements TurtleCommand {

	private double lengthFactor;

	public LengthFactorCommand(double lengthFactor) {
		this.lengthFactor = lengthFactor;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();
		
		state.length *= lengthFactor;
	}

}
