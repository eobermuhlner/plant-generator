package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;
import java.util.Random;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class RandomForwardCommand implements TurtleCommand {

	private Random random;
	
	private double stepFactor;

	private double standardDeviation;

	public RandomForwardCommand(Random random, double stepFactor, double standardDeviation) {
		this.random = random;
		this.stepFactor = stepFactor;
		this.standardDeviation = standardDeviation;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();
		
		double randomStep = (random.nextGaussian() * standardDeviation + 1.0) * stepFactor * state.length;
		double dx = randomStep * Math.cos(state.angle);
		double dy = randomStep * Math.sin(state.angle);
		
		gc.setLineWidth(state.thickness);
		gc.strokeLine(state.x, state.y, state.x+dx, state.y+dy);

		state.x += dx;
		state.y += dy;
	}

}
