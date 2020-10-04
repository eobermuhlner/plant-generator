package ch.obermuhlner.plantgen.ui.turtle.command;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

import java.util.Deque;

public class PushCommand implements TurtleCommand {

  @Override
  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
    TurtleState state = turtleStates.peek();

    TurtleState newState = new TurtleState();
    newState.x = state.x;
    newState.y = state.y;
    newState.xLeft = state.xLeft;
    newState.yLeft = state.yLeft;
    newState.xRight = state.xRight;
    newState.yRight = state.yRight;
    newState.angle = state.angle;
    newState.thickness = state.thickness;
    newState.length = state.length;

    turtleStates.push(newState);
  }

}
