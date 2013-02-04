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
		return false;
	}

}
