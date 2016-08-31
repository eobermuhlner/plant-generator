package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class ThicknessFactorCommand implements TurtleCommand {

	private double thicknessFactor;

	public ThicknessFactorCommand(double thicknessFactor) {
		this.thicknessFactor = thicknessFactor;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();
		
		state.thickness *= thicknessFactor;
	}

}
