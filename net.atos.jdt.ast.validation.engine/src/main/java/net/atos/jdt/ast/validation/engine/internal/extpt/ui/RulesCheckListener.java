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

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;

/**
 * Check State Listener implementation for rules enablement
 * 
 * @author mvanbesien
 * 
 */
public class RulesCheckListener implements ICheckStateListener {

	/**
	 * List of temp states
	 */
	private Map<ASTRuleDescriptor, Boolean> states;

	/**
	 * New Check Listener instance
	 * 
	 * @param states
	 *            , map to be populated with temp states
	 */
	public RulesCheckListener(Map<ASTRuleDescriptor, Boolean> states) {
		this.states = states;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse
	 * .jface.viewers.CheckStateChangedEvent)
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		Object element = event.getElement();
		if (element instanceof ASTRuleDescriptor) {
			ASTRuleDescriptor descriptor = (ASTRuleDescriptor) element;

			// If element is mandatory, it should always remain checked !
			if (descriptor.isMandatory()) {
				event.getCheckable().setChecked(event.getElement(), true);
				return;
			}

			// Now, if not mandatory, we save the state.
			if (states.containsKey(descriptor))
				states.remove(descriptor);
			states.put(descriptor, !event.getChecked());
		}
	}
}
