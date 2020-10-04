package ch.obermuhlner.plantgen;

import java.util.Random;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;

public class TestPlant extends AbstractPlant {

	public TestPlant(Random random) {
		super(random);
	}

	protected String initialize(LindenmayerSystem lindenmayerSystem) {
		lindenmayerSystem.addRule("S", "TTTP");
//		lindenmayerSystem.addRule("P", 2, "T[-TLP]P");
//		lindenmayerSystem.addRule("P", 2, "T[+TLP]P");
//		lindenmayerSystem.addRule("P", 3, "T[-TLP][+TLP]P");
//		lindenmayerSystem.addRule("P", 1, "TP");
		lindenmayerSystem.addRule("P", ""
				+ "2:T[-TLP]P,"
				+ "2:T[TLP]P,"
				+ "3:T[-TLP][+TLP]P,"
				+ "1:TP");
		
		return "S";
	}

}
