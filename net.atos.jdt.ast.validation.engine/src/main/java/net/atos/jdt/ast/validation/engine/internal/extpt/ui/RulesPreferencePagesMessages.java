package net.atos.jdt.ast.validation.engine.internal.extpt.ui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum RulesPreferencePagesMessages {
	PREFERENCE_LABEL, REPOSITORY_LABEL, GROUP_LABEL;

	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("RulesPreferencePagesMessages");

	/*
	 * Returns value of the message
	 */
	public String value() {
		if (RulesPreferencePagesMessages.resourceBundle == null
				|| !RulesPreferencePagesMessages.resourceBundle.containsKey(this.name()))
			return "!!" + this.name() + "!!";

		return RulesPreferencePagesMessages.resourceBundle.getString(this.name());
	}

	/*
	 * Returns value of the formatted message
	 */
	public String value(Object... args) {
		if (RulesPreferencePagesMessages.resourceBundle == null
				|| !RulesPreferencePagesMessages.resourceBundle.containsKey(this.name()))
			return "!!" + this.name() + "!!";

		return MessageFormat.format(RulesPreferencePagesMessages.resourceBundle.getString(this.name()), args);
	}

}
