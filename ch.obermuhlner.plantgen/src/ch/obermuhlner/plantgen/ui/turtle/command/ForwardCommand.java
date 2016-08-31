package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.canvas.GraphicsContext;

public class ForwardCommand implements TurtleCommand {

	private double step;

	public ForwardCommand(double step) {
		this.step = step;
	}
	
	@Override
	public void execute(GraphicsContext gc, Deque<TurtleState> turtleStates) {
		TurtleState state = turtleStates.peek();
		
		double dx = step * Math.cos(state.angle);
		double dy = step * Math.sin(state.angle);
		
		gc.setLineWidth(state.thickness);
		gc.strokeLine(state.x, state.y, state.x+dx, state.y+dy);
		
		state.x += dx;
		state.y += dy;
	}

}
