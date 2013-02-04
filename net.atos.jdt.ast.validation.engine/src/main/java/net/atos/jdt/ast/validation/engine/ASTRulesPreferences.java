package net.atos.jdt.ast.validation.engine;

import net.atos.jdt.ast.validation.engine.internal.Activator;

/**
 * API to handle rules enablement through preferences
 * 
 * @author mvanbesien
 * 
 */
public class ASTRulesPreferences {

	/**
	 * Enable rule in preferences
	 */
	public static void enable(ASTRuleDescriptor descriptor) {
		ASTRulesPreferences.setEnabled(descriptor, false);
	}

	/**
	 * Disable rule in preferences
	 */
	public static void disable(ASTRuleDescriptor descriptor) {
		ASTRulesPreferences.setEnabled(descriptor, true);
	}

	/**
	 * enable/disable rule in preferences
	 */
	public static void setEnabled(ASTRuleDescriptor descriptor, boolean value) {
		String key = ASTRulesPreferences.buildKey(descriptor);
		Activator.getDefault().getPreferenceStore().setValue(key, value);
	}

	/**
	 * Checks rule's enablement in preferences
	 */
	public static boolean isEnabled(ASTRuleDescriptor descriptor) {
		String key = ASTRulesPreferences.buildKey(descriptor);
		return !Activator.getDefault().getPreferenceStore().getBoolean(key);
	}

	/**
	 * Returns the key for the current rule.
	 * 
	 * @param descriptor
	 * @return
	 */
	private static String buildKey(ASTRuleDescriptor descriptor) {
		return "rule.disabled." + descriptor.getRule().getClass().getName();
	}

}
