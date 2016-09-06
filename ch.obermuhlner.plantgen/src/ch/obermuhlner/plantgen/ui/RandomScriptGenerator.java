package ch.obermuhlner.plantgen.ui;

import java.util.Random;

import ch.obermuhlner.plantgen.RandomUtil;

public class RandomScriptGenerator {

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
		"[lB][rB]", // horizontal branches
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

	public static String createRandomScript() {
		StringBuilder script = new StringBuilder();
		
		Random random = new Random();
		script.append("S=" + RandomUtil.next(random, S_RULES) + ";\n");
		
		if (script.toString().contains("P")) {
			script.append("P=" + RandomUtil.next(random, P_RULES) + ";\n");
		}
		if (script.toString().contains("B")) {
			script.append("B=" + RandomUtil.next(random, B_RULES) + ";\n");
		}
		if (script.toString().contains("N")) {
			script.append("N=" + RandomUtil.next(random, N_RULES) + ";\n");
		}
		
		return script.toString();
	}

}
