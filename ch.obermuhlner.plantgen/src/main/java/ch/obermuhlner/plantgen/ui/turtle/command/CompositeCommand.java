package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

public class CompositeCommand implements TurtleCommand {

	private TurtleCommand[] commands;

	public CompositeCommand(TurtleCommand... commands) {
		this.commands = commands;
	}
	
	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		for (TurtleCommand command : commands) {
			command.execute(turtleStates, gc, world);
		}
	}	

}
