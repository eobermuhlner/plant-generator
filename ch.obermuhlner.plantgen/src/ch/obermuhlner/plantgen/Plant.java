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

public class Plant {

	private Random random;

	private String description;

	/**
	 * Seed rules
	 */
	private static final String[] S_RULES = {
			"P",
			"[P][rTuT[P]][lTuT[P]]",
			"TTTTTP",
		};

	/**
	 * Point rules
	 */
	private static final String[] P_RULES = {
		"T[-PL][PL][+PL]",
		"TT[-P][P[L]][+P]",
		"[-P][+P][-TP][+TP]L",
		"TT[P][-TPL][+TPL]",
		"PT[-Pt[-L]][+Pt[+L]]", // cool tree
		"[P]~PT[-L]~PT[+L]", // vines
		"P~T[-L]~T[+L]", // single vine
		"T[-PT[-L]][+PT[+L]]",
		"T[--Pt[-L][+L]t+t[-L][+L]][++PP[-L][+L]t-t[-L][+L]]", // dense tree
		"TT[-BL][+BL]TT[-BL][+BL]P", // christmas tree
		"[-PTTTL][PTTTtttL][+PTTTL]", // two layer bush
		"[-TTTPL][TTTPL][+TTTPL]",
	};

	/**
	 * Branch rules
	 */
	private static final String[] B_RULES = {
//		"tB[dL]",
		"t"
	};
	
	/**
	 * Node rules
	 */
	private static final String[] N_RULES = {
//		"[-L][--L][---L][----L][L][+L][++L][+++L][++++L]",
		"[-L][+L]",
	};
	
	public Plant(Random random) {
		this.random = random;
		init();
	}
	
	private void init() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		lindenmayerSystem.addRule("S", RandomUtil.next(random, S_RULES));
		lindenmayerSystem.addRule("P", RandomUtil.next(random, P_RULES));
		lindenmayerSystem.addRule("B", RandomUtil.next(random, B_RULES));
		lindenmayerSystem.addRule("N", RandomUtil.next(random, N_RULES));
		
		description = "S";
		for (int i = 0; i < random.nextInt(5) + 3; i++) {
			description = lindenmayerSystem.expand(description);
		}
		
		System.out.println(description);
	}
	
	public void initialize(TurtleGraphic turtleGraphic) {
		double turnAngle = Math.toRadians(random.nextDouble() * 60 + 10);
		double standardDeviation = random.nextDouble() * 0.0 + 0.1;
		double initialThickness = random.nextDouble() * 10 + 10;
		double initialLength = random.nextDouble() * 30 + 20;
		double lengthFactor = 1.0;
		double leafFactor = random.nextDouble() * 2.0;
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
				new ColorCommand(Color.GREEN), 
				new LeafCommand(leafFactor, leafThicknessFactor)));
	}
	
	public String getDescription() {
		return description;
	}
}
