package ch.obermuhlner.plantgen;

import java.util.Random;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;

public class TestPlant extends AbstractPlant {

	public TestPlant(Random random) {
		super(random);
	}

	@Override
	protected String createDescription() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();

		lindenmayerSystem.addRule("S", "TTTP");
		lindenmayerSystem.addRule("P", "T[-tLP]P");
		lindenmayerSystem.addRule("P", "T[+tLP]P");
		//lindenmayerSystem.addRule("P", "TP");
		
		String description = "S";
		for (int i = 0; i < random.nextInt(3) + 5; i++) {
			description = lindenmayerSystem.expand(random, description);
		}
		
		return description;
	}

}
