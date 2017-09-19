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
		"T~T~T~T~T~P",
		"TTTTT[-TTTP][+TTTP]TTTTP",
		"TTT[-LP]T[+LP]TP",
	};

	/**
	 * Point rules
	 */
	private static final String[] P_RULES = {
		"T[-PL][PL][+PL]",
		"TT[-P][PL][+P]",
		"TT[-TTP][+TTP]LP",
		"TT[-TTP][+TTP][~-tL][~+tL]P",
		"[-P][+P][-TP][+TP]L",
		"TT[P][-TPL][+TPL]",
		"5:PT[-Pt[-L]][+Pt[+L]],1:PT[-Pt[-L]],1:PT[+Pt[+L]]", // cool tree
		"3:TTT~[-P~ttL]~[+P~ttL],2:TT~[-P~tL]~[+P~tL],1:T~P~tL", // another tree
		"[P]~PT[-L]~PT[+L]", // vines
		"P~T[-L]~T[+L]", // single vine
		"T[-PT[-L]][+PT[+L]]",
		"[PtL]~PT[-ttL]~PT[+ttL]TTP",
		"T[--Pt[-L][+L]t+t[-L][+L]][++PP[-L][+L]t-t[-L][+L]]", // dense tree
		"T[lt+tLP][rt-tLP]P", // upwards turning 
		"TT[-BL][+BL]P", // christmas tree (gegenstaendig)
		"T[-BL]T[+BL]P", // christmas tree (wechselstaendig)
		"[-PTTTL][PTTTtttL][+PTTTL]", // two layer bush
		"[-TTTPL][TTTPL][+TTTPL]",
		"T[-PtL]T[+PtL]Pt",
		"T[-Pt~t~t~L][+Pt~t~t~L]Pt",
		"T[-Pt~t~t~t~L]T[+Pt~t~t~t~L]Pt",
		"T[l+B][r-B]",
		"[lB][rB]", // horizontal branches
	};

	/**
	 * Branch rules
	 */
	private static final String[] B_RULES = {
		"T[L]B",
		"T[-L][+L]B",
		"T[-L]T[+L]B",
		"t[-L]t[+L]B",
		"t[L]B",
		"t[-L][+L]B",
		"t[-BL][+BL]B",
		"t[ttu~tt~LB]B",
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
