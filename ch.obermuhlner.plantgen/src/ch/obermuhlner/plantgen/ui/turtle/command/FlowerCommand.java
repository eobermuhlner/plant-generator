package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FlowerCommand implements TurtleCommand {

	private final int petulaCount;
	private final LeafCommand petulaCommand;
	private final TurnCommand turnCommand;
	private double centerSize = 5;

	public FlowerCommand(int petulaCount, int circleCount, LeafCommand petulaCommand) {
		this.petulaCount = petulaCount;
		this.petulaCommand = petulaCommand;
		
		double angle = 2.0 * Math.PI * circleCount / petulaCount;
		turnCommand = new TurnCommand(angle);
	}

	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		new PushCommand().execute(turtleStates, gc, world);
		
		for (int i = 0; i < petulaCount; i++) {
			petulaCommand.execute(turtleStates, gc, world);
			turnCommand.execute(turtleStates, gc, world);
		}
		
		new PopCommand().execute(turtleStates, gc, world);
		
		TurtleState state = turtleStates.peek();
		double centerRadius = centerSize;
		gc.setFill(Color.YELLOW);
		double centerOffset = centerRadius / 2;
		gc.fillOval(state.x2d - centerOffset, state.y2d - centerOffset, centerRadius, centerRadius);
	}
}
