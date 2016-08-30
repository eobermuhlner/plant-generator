package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class CompositeCommand implements TurtleCommand {

	private TurtleCommand[] commands;

	public CompositeCommand(TurtleCommand... commands) {
		this.commands = commands;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		for (TurtleCommand command : commands) {
			command.execute(gc, turtleStates);
		}
	}	

}
