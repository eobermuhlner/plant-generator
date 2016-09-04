package ch.obermuhlner.plantgen;

import java.util.Random;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;

public class RandomStandardPlant extends AbstractPlant {

	/**
	 * Seed rules
	 */
	private static final String[] S_RULES = {
			"P",
			"[-LP][+LP][P]",
			"[P][rTuSP][lTuSP]",
			"TTTTTP",
			"TTT[-LP]T[+LP]TP",
		};

	/**
	 * Point rules
	 */
	private static final String[] P_RULES = {
		"T[-PL][PL][+PL]",
		"TT[-P][PL][+P]",
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
		"T[-tpL]T[+tpL]Pt",
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
	
	
	public RandomStandardPlant(Random random) {
		super(random);
	}


	@Override
	protected String createDescription() {
		LindenmayerSystem lindenmayerSystem = new LindenmayerSystem();
		lindenmayerSystem.addRule("S", RandomUtil.next(random, S_RULES));
		lindenmayerSystem.addRule("P", RandomUtil.next(random, P_RULES));
		lindenmayerSystem.addRule("B", RandomUtil.next(random, B_RULES));
		lindenmayerSystem.addRule("N", RandomUtil.next(random, N_RULES));
		
		String description = "S";
		for (int i = 0; i < random.nextInt(5) + 3; i++) {
			description = lindenmayerSystem.expand(random, description);
		}
		
		return description;
	}
}
