package ch.obermuhlner.plantgen.ui.turtle;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;

public class TurtleGraphic {

	private Map<Character, TurtleCommand> commands = new HashMap<>();
	
	private TurtleCommand defaultCommand;

	private Deque<TurtleState> states = new ArrayDeque<>();

	
	public TurtleGraphic() {
		states.add(new TurtleState());
	}
	
	public TurtleGraphic(TurtleState initialState) {
		states.push(initialState);
	}
	
	public void addCommand(char c, TurtleCommand command) {
		commands.put(c, command);
	}

	public void setDefaultCommand(TurtleCommand defaultCommand) {
		this.defaultCommand = defaultCommand;
	}

	public void execute(GraphicsContext gc, String script) {
		for (int i = 0; i < script.length(); i++) {
			char c = script.charAt(i);
			TurtleCommand command = commands.get(c);
			if (command == null) {
				command = defaultCommand;
			}
			
			if (command != null) {
				execute(gc, command);
			}
		}
	}
	
	private void execute(GraphicsContext gc, TurtleCommand command) {
		command.execute(gc, states);
	}
	
	public TurtleState getState() {
		return states.peek();
	}

}
