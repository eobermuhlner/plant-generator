package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FlowerCommand implements TurtleCommand {

	private final int petalCount;
	private final LeafCommand petalCommand;
	private final TurnCommand turnCommand;
	private final double centerSize;
	private final Color centerColor;

	public FlowerCommand(int petalCount, LeafCommand petalCommand, double centerSize, Color centerColor) {
		this.petalCount = petalCount;
		this.petalCommand = petalCommand;
		this.centerSize = centerSize;
		this.centerColor = centerColor;
		
		double angle = 2.0 * Math.PI / petalCount;
		turnCommand = new TurnCommand(angle);
	}

	@Override
	public void execute(Deque<TurtleState> turtleStates, GraphicsContext gc, Group world) {
		TurtleState state = turtleStates.peek();
		
		new PushCommand().execute(turtleStates, gc, world);
		
		for (int i = 0; i < petalCount; i++) {
			petalCommand.execute(turtleStates, gc, world);
			turnCommand.execute(turtleStates, gc, world);
		}
		
		new PopCommand().execute(turtleStates, gc, world);
		
		double centerRadius = centerSize * petalCommand.getLeafLength(state);
		gc.setFill(centerColor);
		double centerOffset = centerRadius / 2;
		gc.fillOval(state.x - centerOffset, state.y - centerOffset, centerRadius, centerRadius);
	}
}
