package ch.obermuhlner.plantgen.ui.turtle.command;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

import java.util.Deque;

public class PopCommand implements TurtleCommand {

  @Override
  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
    turtleStates.pop();
  }

}
