package ch.obermuhlner.plantgen.lsystem;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class LindenmayerSystem {

	private Map<String, String> rules = new TreeMap<>();
	
	public void addRule(String key, String rule) {
		rules.put(key, rule);
	}
	
	public String expand(String in) {
		String out = in;
		for (Entry<String, String> rule : rules.entrySet()) {
			String key = rule.getKey();
			
			int pos = out.length();
			while (pos >= 0) {
				pos = out.lastIndexOf(key, pos-1);
				if (pos >= 0) {
					out = out.substring(0, pos) + rule.getValue() + out.substring(pos + key.length()); 
				}
			}
		}
		return out;
	}
}
