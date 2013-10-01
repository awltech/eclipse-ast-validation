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
package net.atos.jdt.ast.validation.engine.internal.extpt;

import net.atos.jdt.ast.validation.engine.AbstractASTRuleFactory;
import net.atos.jdt.ast.validation.engine.internal.Activator;
import net.atos.jdt.ast.validation.engine.rules.AbstractASTRule;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Rule Factory when the rule comes from an extension point.
 * 
 * @author mvanbesien
 * @since 1.1
 * 
 */
public class ASTExtensionPointRulesFactory extends AbstractASTRuleFactory {

	/**
	 * Extension attribute ID
	 */
	private String extensionPointKey;

	/**
	 * Extension element
	 */
	private IConfigurationElement element;

	/**
	 * Creates new factory with Extension point information, so we use extension
	 * point to create new rules instances. This is better then
	 * class.newInstance() as here, we won't face any classloading issue.
	 * 
	 * @param element
	 * @param extensionPointKey
	 */
	public ASTExtensionPointRulesFactory(IConfigurationElement element, String extensionPointKey) {
		this.element = element;
		this.extensionPointKey = extensionPointKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.atos.jdt.ast.validation.engine.AbstractASTRuleFactory#create()
	 */
	@Override
	public AbstractASTRule create() {
		try {
			if (element != null || extensionPointKey != null) {
				Object createdExecutableExtension = this.element.createExecutableExtension(this.extensionPointKey);
				if (createdExecutableExtension instanceof AbstractASTRule)
					return (AbstractASTRule) createdExecutableExtension;
				else {
					Activator
							.getDefault()
							.getLog()
							.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Created element is not a rule ! "
									+ createdExecutableExtension == null ? null : createdExecutableExtension.getClass()
									.getName()));
				}
			}
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
		return null;
	}

}
