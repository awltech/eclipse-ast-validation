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
package net.atos.jdt.ast.validation.engine.internal.extpt.ui;

import java.util.Map;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;
import net.atos.jdt.ast.validation.engine.ASTRulesPreferences;

import org.eclipse.jface.viewers.ICheckStateProvider;

/**
 * Rules Check Provider : initializes checkboxes when invoked.
 * 
 * @author mvanbesien
 * 
 */
public class RulesCheckProvider implements ICheckStateProvider {

	/**
	 * Map with temp states
	 */
	private Map<ASTRuleDescriptor, Boolean> states;

	/**
	 * Creates instance with temp states map, to be read when initializing
	 * checkboxes
	 * 
	 * @param states
	 */
	public RulesCheckProvider(Map<ASTRuleDescriptor, Boolean> states) {
		this.states = states;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ICheckStateProvider#isChecked(java.lang.Object)
	 */
	@Override
	public boolean isChecked(Object element) {
		if (element instanceof ASTRuleDescriptor) {
			ASTRuleDescriptor descriptor = (ASTRuleDescriptor) element;
			if (descriptor.isMandatory())
				return true;
			if (states.containsKey(descriptor))
				return states.get(descriptor);
			return ASTRulesPreferences.isEnabled(descriptor);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ICheckStateProvider#isGrayed(java.lang.Object)
	 */
	@Override
	public boolean isGrayed(Object element) {
		if (element instanceof ASTRuleDescriptor) {
			ASTRuleDescriptor descriptor = (ASTRuleDescriptor) element;
			return descriptor.isMandatory();
		}
		return false;
	}

}
