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


	@Test
	public void testStochastic() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		Random random = new Random(1);
		
		lindenmayerSystem.addRule("A", 1, "1");
		lindenmayerSystem.addRule("A", 9, "2");
		
		int n = 10000;
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < n; i++) {
			String result = lindenmayerSystem.expand(random, "A");
			if (result.equals("1")) {
				count1++;
			}
			if (result.equals("2")) {
				count2++;
			}
		}
		
		assertEquals(0.1, (double)count1 / n, 0.01);
		assertEquals(0.9, (double)count2 / n, 0.01);
	}
}
