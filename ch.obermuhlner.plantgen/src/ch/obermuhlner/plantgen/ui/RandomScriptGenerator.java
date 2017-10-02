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
		"T[-PL][PL][+PL]f",
		"TT[-P][PL][+P]f",
		"TT[-TTP][+TTP]LPf",
		"TT[-TTP][+TTP][~-tL][~+tL]P",
		"[-P][+P][-TP][+TP]L",
		"TT[P][-TPL][+TPL]f",
		"5:PT[-Pt[-L]][+Pt[+L]]f,1:PT[-Pt[-L]],1:PT[+Pt[+L]]", // cool tree
		"3:TTT~[-P~ttL]~[+P~ttL]f,2:TT~[-P~tL]~[+P~tL],1:T~P~tL", // another tree
		"[P]~PT[-L]~PT[+L]f", // vines
		"P~T[-L]~T[+L]f", // single vine
		"T[-PT[-L]][+PT[+L]]f",
		"[PtL]~PT[-ttL]~PT[+ttL]TTPf",
		"T[--Pt[-L][+L]t+t[-L][+L]][++PP[-L][+L]t-t[-L][+L]]", // dense tree
		"T[lt+tLPf][rt-tLPf]P", // upwards turning 
		"TT[-BL][+BL]Pf", // christmas tree (gegenstaendig)
		"T[-BL]T[+BL]Pf", // christmas tree (wechselstaendig)
		"[-PTTTL][PTTTtttL][+PTTTL]", // two layer bush
		"[-TTTPL][TTTPL][+TTTPL]",
		"T[-PtL]T[+PtL]Pt",
		"T[-Pt~t~t~L][+Pt~t~t~L]Ptf",
		"T[-Pt~t~t~t~L]T[+Pt~t~t~t~L]Ptf",
		"T[l+B][r-B]f",
		"[lB][rB]f", // horizontal branches
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

	private static final String[] F_RULES = {
			"1:F,4:",
			"F",
			"[tF]",
			"[~tF][~tF][~tF]",
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
		if (script.toString().contains("f")) {
			script.append("f=" + RandomUtil.next(random, F_RULES) + ";\n");
		}
		
		return script.toString();
	}

}
