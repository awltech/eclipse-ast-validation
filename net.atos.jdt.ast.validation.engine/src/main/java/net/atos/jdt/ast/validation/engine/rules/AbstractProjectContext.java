/*
 *     Eclipse AST Validation, lite framework to validate java code
 *     
 *     Copyright (C) 2013 Atos Worldline or third-party contributors as
 *     indicated by the @author tags or express copyright attribution
 *     statements applied by the authors.
 *     
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License.
 *     
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *     Lesser General Public License for more details.
 *     
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
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
