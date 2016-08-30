package ch.obermuhlner.plantgen.ui.turtle;

import java.util.Deque;

import javafx.scene.canvas.GraphicsContext;

public interface TurtleCommand {

	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates);

}
