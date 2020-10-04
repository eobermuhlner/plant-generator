package ch.obermuhlner.plantgen.ui.turtle.command;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.Deque;
import java.util.Random;

public class RandomForwardCommand implements TurtleCommand {

  private final Random random;

  private final double stepFactor;

  private final double standardDeviation;

  private final Color color;

  public RandomForwardCommand(Random random, Color color, double stepFactor, double standardDeviation) {
    this.random = random;
    this.color = color;
    this.stepFactor = stepFactor;
    this.standardDeviation = standardDeviation;
  }

  @Override
  public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
    TurtleState state = turtleStates.peek();

    double randomStep = (random.nextGaussian() * standardDeviation + 1.0) * stepFactor * state.length;

    double dx = randomStep * Math.cos(state.angle);
    double dy = randomStep * Math.sin(state.angle);

    double x1 = state.xLeft;
    double y1 = state.yLeft;

    double x2 = state.x + dx + state.thickness * Math.cos(state.angle - Math.PI / 2);
    double y2 = state.y + dy + state.thickness * Math.sin(state.angle - Math.PI / 2);

    double x3 = state.x + dx + state.thickness * Math.cos(state.angle + Math.PI / 2);
    double y3 = state.y + dy + state.thickness * Math.sin(state.angle + Math.PI / 2);

    double x4 = state.xRight;
    double y4 = state.yRight;

    LinearGradient gradient = new LinearGradient(
        x1,
        y1,
        x4,
        y4,
        false,
        CycleMethod.NO_CYCLE,
        new Stop(0, color.brighter()),
        new Stop(1, color)
    );
    gc.setFill(gradient);

    double[] xPoints = new double[] {x1, x2, x3, x4};
    double[] yPoints = new double[] {y1, y2, y3, y4};
    gc.fillPolygon(xPoints, yPoints, xPoints.length);

    state.x += dx;
    state.y += dy;

    state.xLeft = x2;
    state.yLeft = y2;

    state.xRight = x3;
    state.yRight = y3;
  }

}
