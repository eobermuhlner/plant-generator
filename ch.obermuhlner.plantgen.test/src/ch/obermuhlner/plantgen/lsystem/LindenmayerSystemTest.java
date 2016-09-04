package ch.obermuhlner.plantgen.lsystem;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class LindenmayerSystemTest {

	@Test
	public void testBasics() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		Random random = new Random(1);
		
		lindenmayerSystem.addRule("A", "B");

		assertEquals("B", lindenmayerSystem.expand(random, "A"));
		assertEquals("X", lindenmayerSystem.expand(random, "X"));
	}
	
	@Test
	public void testRecursion1() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		Random random = new Random(1);
		
		lindenmayerSystem.addRule("A", "<BA>");
		
		assertEquals("{<BA>}", lindenmayerSystem.expand(random, "{A}"));
		assertEquals("{<B<BA>>}", lindenmayerSystem.expand(random, lindenmayerSystem.expand(random, "{A}")));
	}

	@Test
	public void testRecursion2() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		Random random = new Random(1);
		
		lindenmayerSystem.addRule("A", "<BA>");
		lindenmayerSystem.addRule("B", "[C]");
		
		assertEquals("{<[C]A>}", lindenmayerSystem.expand(random, "{A}"));
	}

}
