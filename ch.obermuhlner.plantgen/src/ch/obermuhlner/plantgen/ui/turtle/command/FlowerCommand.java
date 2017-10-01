package ch.obermuhlner.plantgen.ui.turtle.command;

import java.util.Deque;

import ch.obermuhlner.plantgen.ui.turtle.TurtleCommand;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class FlowerCommand implements TurtleCommand {

	private final Color petal1Color;
	private final Color petal2Color;
	private final int petalCount;
	private final LeafCommand petalCommand;
	private final TurnCommand turnCommand;
	private final double centerSize;
	private final Color centerColor;

	public FlowerCommand(Color petal1Color, Color petal2Color, int petalCount, LeafCommand petalCommand, double centerSize, Color centerColor) {
		this.petal1Color = petal1Color;
		this.petal2Color = petal2Color;
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
		
		double length = petalCommand.getLeafLength(state);
		RadialGradient radialGradient = new RadialGradient(0, 0, state.x2d, state.y2d, length, false, CycleMethod.NO_CYCLE, new Stop(0.5, petal1Color), new Stop(1.0, petal2Color));
		gc.setFill(radialGradient);
		
		new PushCommand().execute(turtleStates, gc, world);
		
		for (int i = 0; i < petalCount; i++) {
			petalCommand.execute(turtleStates, gc, world);
			turnCommand.execute(turtleStates, gc, world);
		}
		
		new PopCommand().execute(turtleStates, gc, world);
		
		double centerRadius = centerSize * petalCommand.getLeafLength(state);
		gc.setFill(centerColor);
		double centerOffset = centerRadius / 2;
		gc.fillOval(state.x2d - centerOffset, state.y2d - centerOffset, centerRadius, centerRadius);
	}
}
