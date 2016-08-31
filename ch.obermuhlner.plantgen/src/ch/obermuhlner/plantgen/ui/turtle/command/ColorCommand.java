package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ColorCommand implements TurtleCommand {

	private Color color;

	public ColorCommand(Color color) {
		this.color = color;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		gc.setStroke(color);
		gc.setFill(color);
	}

}
