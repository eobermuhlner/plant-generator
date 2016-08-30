package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;
import java.util.Random;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class RandomForwardCommand implements TurtleCommand {

	private Random random;
	
	private double step;

	private double standardDeviation;

	public RandomForwardCommand(Random random, double step, double standardDeviation) {
		this.random = random;
		this.step = step;
		this.standardDeviation = standardDeviation;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();
		
		double randomStep = (random.nextGaussian() * standardDeviation + 1.0) * step;
		double dx = randomStep * Math.cos(state.angle);
		double dy = randomStep * Math.sin(state.angle);
		
		gc.strokeLine(state.x, state.y, state.x+dx, state.y+dy);
		
		state.x += dx;
		state.y += dy;
	}

}
