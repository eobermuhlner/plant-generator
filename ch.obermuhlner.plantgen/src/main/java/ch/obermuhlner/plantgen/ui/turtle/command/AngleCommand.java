package ch.obermuhlner.plantgen.ui.turtle.command;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

import java.util.Deque;

public class AngleCommand implements TurtleCommand {

  private double angle;

  public AngleCommand(double angle) {
    this.angle = angle;
  }

  @Override
  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
    TurtleState state = turtleStates.peek();

    state.angle = angle;
  }

}
