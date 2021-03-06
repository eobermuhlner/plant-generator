package ch.obermuhlner.plantgen.ui.turtle.command;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

import java.util.Deque;

public class ForwardCommand implements TurtleCommand {

  private double step;

  public ForwardCommand(double step) {
    this.step = step;
  }

  @Override
  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
    TurtleState state = turtleStates.peek();

    double dx = step * Math.cos(state.angle);
    double dy = step * Math.sin(state.angle);

    if (gc != null) {
      gc.setLineWidth(state.thickness);
      gc.strokeLine(state.x, state.y, state.x + dx, state.y + dy);
    }

    if (world != null) {
    }

    state.x += dx;
    state.y += dy;
  }

}
