package ch.obermuhlner.plantgen.ui.turtle.command;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

import java.util.Deque;
import java.util.Random;

public class RandomChangeAngleCommand implements TurtleCommand {

  private Random random;

  private double angle;

  private double standardDeviation;

  public RandomChangeAngleCommand(Random random, double angle, double standardDeviation) {
    this.random = random;
    this.angle = angle;
    this.standardDeviation = standardDeviation;
  }

  @Override
  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
    TurtleState state = turtleStates.peek();

    double randomAngle = (random.nextGaussian() * standardDeviation) * angle;

    state.angle += randomAngle;
  }

}
