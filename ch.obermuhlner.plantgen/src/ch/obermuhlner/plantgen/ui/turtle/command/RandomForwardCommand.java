package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;
import java.util.Random;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

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
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();
		
		double randomStep = (random.nextGaussian() * standardDeviation + 1.0) * stepFactor * state.length;

		if (gc != null) {
			double dx = randomStep * Math.cos(state.angle);
			double dy = randomStep * Math.sin(state.angle);

			gc.setLineWidth(state.thickness);
			gc.strokeLine(state.x2d, state.y2d, state.x2d+dx, state.y2d+dy);

			state.x2d += dx;
			state.y2d += dy;
		}
		
		if (world != null) {
			double factor = 1;
			
			double dx = randomStep * factor * Math.cos(state.angle);
			double dy = randomStep * factor * Math.sin(state.angle);

	        Box box = new Box(1, 1, randomStep * factor);
	        box.setMaterial(new PhongMaterial(Color.BROWN));
	        box.setTranslateX(state.x3d * factor);
	        box.setTranslateY(state.y3d * factor);
	        box.setTranslateZ(state.z3d * factor);
	        world.getChildren().add(box);			

	        state.x3d += dx;
			state.y3d -= dy;
		}

	}

}
