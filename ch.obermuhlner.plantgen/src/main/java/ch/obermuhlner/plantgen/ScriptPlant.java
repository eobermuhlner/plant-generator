package ch.obermuhlner.plantgen;

import java.util.Random;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;

public class ScriptPlant extends AbstractPlant {

	private String script;

	public ScriptPlant(Random random, String script) {
		super(random);
		
		this.script = script;
	}

	protected String initialize(LindenmayerSystem lindenmayerSystem) {
		lindenmayerSystem.setScript(script);
		
		return "S";
	}

}
