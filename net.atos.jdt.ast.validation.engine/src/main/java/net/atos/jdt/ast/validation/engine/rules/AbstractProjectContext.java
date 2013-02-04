package net.atos.jdt.ast.validation.engine.rules;

import org.eclipse.jdt.core.ICompilationUnit;

/**
 * Class used as context for rules. Aim is to allow the user to provide custom
 * implementation to filter the execution of rules, according to project
 * properties.
 * 
 * @author mvanbesien
 * @since 1.0
 */
public abstract class AbstractProjectContext {

	/**
	 * Returns true if matching rule should be executed within this project.
	 * 
	 * @param project
	 * @return
	 */
	public abstract boolean validate(ICompilationUnit compilationUnit);

}
