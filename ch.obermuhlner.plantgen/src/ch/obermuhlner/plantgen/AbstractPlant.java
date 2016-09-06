package ch.obermuhlner.plantgen;

import java.util.Random;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.command.AngleCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.ColorCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.CompositeCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.LeafCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.LengthFactorCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.PopCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.PushCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.RandomChangeAngleCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.RandomForwardCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.RandomTurnCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.ThicknessFactorCommand;
import javafx.scene.paint.Color;

public abstract class AbstractPlant {

	protected Random random;

	private String description;

	public AbstractPlant(Random random) {
		this.random = random;
		this.description = createDescription();
	}

	private String createDescription() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(random);
		
		String description = initialize(lindenmayerSystem);
		for (int i = 0; i < random.nextInt(5) + 5; i++) {
			description = lindenmayerSystem.expand(description);
		}
		
		return description;
	}

	protected abstract String initialize(LindenmayerSystem lindenmayerSystem);

	public void initialize(TurtleGraphic turtleGraphic) {
		double turnAngle = Math.toRadians(random.nextDouble() * 60 + 10);
		double standardDeviation = random.nextDouble() * 0.0 + 0.1;
		double initialThickness = random.nextDouble() * 10 + 10;
		double initialLength = random.nextDouble() * 30 + 20;
		double lengthFactor = 1.0;
		double leafFactor = random.nextDouble() * 2.0 + 1.0;
		double leafThicknessFactor = random.nextDouble() * 4.0;
		
		turtleGraphic.getState().thickness = initialThickness;
		turtleGraphic.getState().length = initialLength;
				
		turtleGraphic.addCommand('[', new PushCommand());
		turtleGraphic.addCommand(']', new PopCommand());
		turtleGraphic.addCommand('u', new AngleCommand(Math.toRadians(270)));
		turtleGraphic.addCommand('d', new AngleCommand(Math.toRadians(90)));
		turtleGraphic.addCommand('l', new AngleCommand(Math.toRadians(180)));
		turtleGraphic.addCommand('r', new AngleCommand(Math.toRadians(0)));
		turtleGraphic.addCommand('-', new RandomTurnCommand(random, -turnAngle, standardDeviation));
		turtleGraphic.addCommand('+', new RandomTurnCommand(random, turnAngle, standardDeviation));
		turtleGraphic.addCommand('~', new RandomChangeAngleCommand(random, turnAngle, standardDeviation));
		turtleGraphic.addCommand('T', new CompositeCommand(
				new ThicknessFactorCommand(0.9),
				new LengthFactorCommand(0.95),
				new ColorCommand(Color.BROWN),
				new RandomForwardCommand(random, lengthFactor, standardDeviation)));
		turtleGraphic.addCommand('t', new CompositeCommand(
				new ThicknessFactorCommand(0.5),
				new LengthFactorCommand(0.9),
				new ColorCommand(Color.BROWN),
				new RandomForwardCommand(random, lengthFactor, standardDeviation)));
		turtleGraphic.addCommand('L', new CompositeCommand(
				new ColorCommand(Color.rgb(0, 255, 0, 0.5)), 
				new LeafCommand(leafFactor, leafThicknessFactor)));
	}
	
	public String getDescription() {
		System.out.println(description);
		return description;
	}
}
