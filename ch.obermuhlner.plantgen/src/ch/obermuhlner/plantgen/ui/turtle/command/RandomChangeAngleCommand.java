package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;
import java.util.Random;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

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
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();

		double randomAngle = (random.nextGaussian() * standardDeviation) * angle;

		state.angle += randomAngle;
	}

}
