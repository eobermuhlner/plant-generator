package ch.obermuhlner.plantgen;

import java.util.Random;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;

public class RandomStandardPlant extends AbstractPlant {

	/**
	 * Seed rules
	 */
	private static final String[] S_RULES = {
		"P",
//		"[-LP][+LP][P]",
//		"[P][rTuSP][lTuSP]",
//		"TTTTTP",
//		"TTT[-LP]T[+LP]TP",
	};

	/**
	 * Point rules
	 */
	private static final String[] P_RULES = {
		"T[-PL][PL][+PL]",
		"TT[-P][PL][+P]",
		"[-P][+P][-TP][+TP]L",
		"TT[P][-TPL][+TPL]",
		"5:PT[-Pt[-L]][+Pt[+L]],1:PT[-Pt[-L]],1:PT[+Pt[+L]]", // cool tree
		"[P]~PT[-L]~PT[+L]", // vines
		"P~T[-L]~T[+L]", // single vine
		"T[-PT[-L]][+PT[+L]]",
		"T[--Pt[-L][+L]t+t[-L][+L]][++PP[-L][+L]t-t[-L][+L]]", // dense tree
		"TT[-BL][+BL]P", // christmas tree (gegenstaendig)
		"T[-BL]T[+BL]P", // christmas tree (wechselstaendig)
		"[-PTTTL][PTTTtttL][+PTTTL]", // two layer bush
		"[-TTTPL][TTTPL][+TTTPL]",
		"T[-tpL]T[+tpL]Pt",
	};

	/**
	 * Branch rules
	 */
	private static final String[] B_RULES = {
		"T[L]B",
		"T[-L][+L]B",
		"T[-L]T[+L]B",
		"t[L]B",
		"t[-L][+L]B",
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

	protected String initialize(LindenmayerSystem lindenmayerSystem) {
		lindenmayerSystem.addRule("S", RandomUtil.next(random, S_RULES));
		lindenmayerSystem.addRule("P", RandomUtil.next(random, P_RULES));
		lindenmayerSystem.addRule("B", RandomUtil.next(random, B_RULES));
		lindenmayerSystem.addRule("N", RandomUtil.next(random, N_RULES));
		
		return "S";
	}
}
