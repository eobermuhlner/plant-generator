package ch.obermuhlner.plantgen.ui.turtle;

import java.util.Deque;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

public interface TurtleCommand {

	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world);

}
