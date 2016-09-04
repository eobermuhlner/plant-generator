package ch.obermuhlner.plantgen.lsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class LindenmayerSystem {

	private Map<String, List<StochasticRule>> rules = new TreeMap<>();
	
	public void addRule(String key, String rule) {
		addRule(key, 1, rule);
	}
	
	public void addRule(String key, double probability, String rule) {
		rules.computeIfAbsent(key, k -> new ArrayList<>()).add(new StochasticRule(probability, rule));
	}
	
	public String expand(Random random, String in) {
		String out = in;
		for (Entry<String, List<StochasticRule>> stochasticRules : rules.entrySet()) {
			String key = stochasticRules.getKey();
			
			int pos = out.length();
			while (pos >= 0) {
				pos = out.lastIndexOf(key, pos-1);
				if (pos >= 0) {
					String rule = pickRule(random, stochasticRules.getValue());
					out = out.substring(0, pos) + rule + out.substring(pos + key.length()); 
				}
			}
		}
		return out;
	}

	private String pickRule(Random random, List<StochasticRule> stochasticRules) {
		double sum = 0;
		
		for (StochasticRule stochasticRule : stochasticRules) {
			sum += stochasticRule.probability;
		}
		
		double r = random.nextDouble() * sum;

		sum = 0;
		for (StochasticRule stochasticRule : stochasticRules) {
			sum += stochasticRule.probability;
			if (r <= sum) {
				return stochasticRule.rule;
			}
		}
		
		throw new IllegalStateException("No matching rule found");
	}
	
	private class StochasticRule {
		public final double probability;
		public final String rule;

		public StochasticRule(double probability, String rule) {
			this.probability = probability;
			this.rule = rule;
		}
	}
}
