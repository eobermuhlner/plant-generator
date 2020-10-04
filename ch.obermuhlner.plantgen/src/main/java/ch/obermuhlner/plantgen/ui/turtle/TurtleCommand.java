package ch.obermuhlner.plantgen.ui.turtle;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

import java.util.Deque;

public interface TurtleCommand {

  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world);

}
