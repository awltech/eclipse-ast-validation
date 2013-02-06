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
