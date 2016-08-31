package ch.obermuhlner.plantgen.lsystem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LindenmayerSystemTest {

	@Test
	public void testBasics() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		
		lindenmayerSystem.addRule("A", "B");

		assertEquals("B", lindenmayerSystem.expand("A"));
		assertEquals("X", lindenmayerSystem.expand("X"));
	}
	
	@Test
	public void testRecursion1() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		
		lindenmayerSystem.addRule("A", "<BA>");
		
		assertEquals("{<BA>}", lindenmayerSystem.expand("{A}"));
		assertEquals("{<B<BA>>}", lindenmayerSystem.expand(lindenmayerSystem.expand("{A}")));
	}

	@Test
	public void testRecursion2() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		
		lindenmayerSystem.addRule("A", "<BA>");
		lindenmayerSystem.addRule("B", "[C]");
		
		assertEquals("{<[C]A>}", lindenmayerSystem.expand("{A}"));
	}

}
