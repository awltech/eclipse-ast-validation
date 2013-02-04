package net.atos.jdt.ast.validation.engine.internal;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Class containing messages for AST Validation runtime
 * 
 * @author mvanbesien
 * @since 1.0
 */
public enum ValidationEngineMessages {
	RETRIEVING_CU, VALIDATING_SOURCE, VALIDATING_CU, RULE_LOADING_EXCEPTION,CONTEXT_LOADING_EXCEPTION, EXECUTION_EXECPTION;

	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationEngineMessages");

	/*
	 * Returns value of the message
	 */
	public String value() {
		if (ValidationEngineMessages.resourceBundle == null || !ValidationEngineMessages.resourceBundle.containsKey(this.name())) {
			return "!!" + this.name() + "!!";
		}

		return ValidationEngineMessages.resourceBundle.getString(this.name());
	}

	/*
	 * Returns value of the formatted message
	 */
	public String value(final Object... args) {
		if (ValidationEngineMessages.resourceBundle == null || !ValidationEngineMessages.resourceBundle.containsKey(this.name())) {
			return "!!" + this.name() + "!!";
		}

		return MessageFormat.format(ValidationEngineMessages.resourceBundle.getString(this.name()), args);
	}

}
