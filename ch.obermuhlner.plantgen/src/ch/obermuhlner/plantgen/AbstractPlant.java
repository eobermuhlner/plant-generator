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

	private double turnAngle = Math.toRadians(45);
	private double standardDeviation = 0.0;
	private double initialThickness = 10.0;
	private double initialLength = 40.0;
	private double lengthFactor = 1.0;
	private double leafSize = 1.0;
	private double leafThicknessFactor = 2.0;
	private double leafLengthFactor = 2.0;
	private double leafWidthFactor = 2.0;
	private double leafWidthAngle = Math.PI / 4;
	private int steps = 5;

	private Color trunkColor = Color.BROWN.darker();
	private Color branchColor = Color.BROWN;
	private Color leafColor = Color.rgb(0, 255, 0, 0.5);
	
	public AbstractPlant(Random random) {
		this.random = random;
	}

	public void setTurnAngle(double turnAngle) {
		this.turnAngle = turnAngle;
	}
	
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public void setInitialThickness(double initialThickness) {
		this.initialThickness = initialThickness;
	}

	public void setInitialLength(double initialLength) {
		this.initialLength = initialLength;
	}

	public void setLengthFactor(double lengthFactor) {
		this.lengthFactor = lengthFactor;
	}

	public void setLeafFactor(double leafFactor) {
		this.leafSize = leafFactor;
	}

	public void setLeafThicknessFactor(double leafThicknessFactor) {
		this.leafThicknessFactor = leafThicknessFactor;
	}

	public void setLeafLengthFactor(double leafLengthFactor) {
		this.leafLengthFactor = leafLengthFactor;
	}

	public void setLeafWidthFactor(double leafWidthFactor) {
		this.leafWidthFactor = leafWidthFactor;
	}

	public void setLeafWidthAngle(double leafWidthAngle) {
		this.leafWidthAngle = leafWidthAngle;
	}
	
	public void setSteps(int steps) {
		this.steps = steps;
	}

	public void setTrunkColor(Color trunkColor) {
		this.trunkColor = trunkColor;
	}
	
	public void setBranchColor(Color branchColor) {
		this.branchColor = branchColor;
	}
	
	public void setLeafColor(Color leafColor) {
		this.leafColor = leafColor;
	}
	
	private String createDescription() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(random);
		
		String description = initialize(lindenmayerSystem);
		for (int i = 0; i < steps; i++) {
			description = lindenmayerSystem.expand(description);
		}
		
		return description;
	}

	protected abstract String initialize(LindenmayerSystem lindenmayerSystem);

	public void initialize(TurtleGraphic turtleGraphic) {
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
				new ColorCommand(trunkColor),
				new RandomForwardCommand(random, lengthFactor, standardDeviation)));
		turtleGraphic.addCommand('t', new CompositeCommand(
				new ThicknessFactorCommand(0.5),
				new LengthFactorCommand(0.9),
				new ColorCommand(branchColor),
				new RandomForwardCommand(random, lengthFactor, standardDeviation)));
		turtleGraphic.addCommand('L', new CompositeCommand(
				new ColorCommand(leafColor), 
				new LeafCommand(leafSize, leafThicknessFactor, leafLengthFactor, leafWidthFactor, leafWidthAngle)));
	}
	
	public String getDescription() {
		return createDescription();
	}
}
