package ch.obermuhlner.plantgen;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;

public class Plant {

	private String description;

	public Plant() {
		init();
	}
	
	private void init() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		lindenmayerSystem.addRule("S", "TTTL");
		lindenmayerSystem.addRule("L", "B[-BL][+BL]");
		
		description = "S";
		for (int i = 0; i < 3; i++) {
			description = lindenmayerSystem.expand(description);
		}
		
		System.out.println(description);
	}
	
	public String getDescription() {
		return description;
	}
}
