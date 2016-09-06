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
	public void testSetScript() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		
		lindenmayerSystem.setScript(""
				+ "A=B;"
				+ "C=D;");

		assertEquals("B", lindenmayerSystem.expand("A"));
		assertEquals("D", lindenmayerSystem.expand("C"));
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
		
		assertEquals("{<BA>}", lindenmayerSystem.expand("{A}"));
		assertEquals("{<[C]<BA>>}", lindenmayerSystem.expand(lindenmayerSystem.expand("{A}")));
	}


	@Test
	public void testStochastic() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		
		lindenmayerSystem.addRule("A", 1, "1");
		lindenmayerSystem.addRule("A", 9, "2");
		
		int n = 10000;
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < n; i++) {
			String result = lindenmayerSystem.expand("A");
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
