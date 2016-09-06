package ch.obermuhlner.plantgen.lsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LindenmayerSystem {

	private static final Pattern ASSIGNMENT_RULE_PATTERN = Pattern.compile("^(.+)=(.*)");
	private static final Pattern STOCHASTIC_RULE_PATTERN = Pattern.compile("^([0-9]+):(.*)");
	
	private final Map<String, List<StochasticRule>> rules = new TreeMap<>();

	private final Random random;

	public LindenmayerSystem() {
		this(new Random());
	}
	
	public LindenmayerSystem(Random random) {
		this.random = random;
	}
	
	public void setScript(String script) {
		String[] splitScript = script.split(Pattern.quote(";"));
		for (String assignment : splitScript) {
			Matcher matcher = ASSIGNMENT_RULE_PATTERN.matcher(assignment);
			if (matcher.matches()) {
				String key = matcher.group(1);
				String rule = matcher.group(2);
				addRule(key, rule);
			} else {
				throw new IllegalArgumentException("Invalid assignment: " + assignment);
			}
		}
	}
	
	/**
	 * Adds one or multiple expansion rules for the specified key.
	 * 
	 * <p>Multiple rules are separated by ','.</p>
	 * <p>Each rule may be prefixed with a probability weight separated by ':'.
	 * If no probability weight is specified, a default value of 1.0 will be used.</p>
	 * 
	 * <p>Examples:</p>
	 * <ul>
	 * <li>"AAA"</li>
	 * <li>"AAA,BBB"</li>
	 * <li>"10:AAA,90:BBB"</li>
	 * </ul>
	 * 
	 * @param key the key
	 * @param rule the expanded rule
	 */
	public void addRule(String key, String rule) {
		String[] splitRules = rule.split(Pattern.quote(","));
		for (String splitRule : splitRules) {
			double probability = 1.0;
		
			Matcher matcher = STOCHASTIC_RULE_PATTERN.matcher(splitRule);
			if (matcher.matches()) {
				probability = Double.parseDouble(matcher.group(1));
				splitRule = matcher.group(2);
			} 
			addRule(key, probability, splitRule);
		}
	}
	
	/**
	 * Adds an expansion rule for the specified key with the specified probability.
	 * 
	 * <p>The probability weights for all rules with the same key will be added up,
	 * this sum will be 100% probability.</p>
	 * 
	 * @param key the key
	 * @param probabilityWeight the probability weight that this rule will be executed
	 * @param rule the expanded rule
	 */
	public void addRule(String key, double probabilityWeight, String rule) {
		rules.computeIfAbsent(key, k -> new ArrayList<>()).add(new StochasticRule(probabilityWeight, rule));
	}
	
	public String expand(String in) {
		StringBuilder out = new StringBuilder();

		for (int i = 0; i < in.length(); i++) {
			String inKey = in.substring(i, i+1);
			
			List<StochasticRule> stochasticRules = rules.get(inKey);
			if (stochasticRules != null) {
				String rule = pickRule(stochasticRules);
				out.append(rule);
			} else {
				out.append(inKey);
			}
		}

		return out.toString();
	}

	private String pickRule(List<StochasticRule> stochasticRules) {
		double sum = 0;
		
		for (StochasticRule stochasticRule : stochasticRules) {
			sum += stochasticRule.probabilityWeight;
		}
		
		double r = random.nextDouble() * sum;

		sum = 0;
		for (StochasticRule stochasticRule : stochasticRules) {
			sum += stochasticRule.probabilityWeight;
			if (r <= sum) {
				return stochasticRule.rule;
			}
		}
		
		throw new IllegalStateException("No matching rule found");
	}
	
	private class StochasticRule {
		public final double probabilityWeight;
		public final String rule;

		public StochasticRule(double probabilityWeight, String rule) {
			this.probabilityWeight = probabilityWeight;
			this.rule = rule;
		}
	}
}
