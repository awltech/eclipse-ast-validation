package net.atos.jdt.ast.validation.engine.internal.extpt.ui;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Rules Label Provider
 * @author mvanbesien
 *
 */
public class RulesLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {

		if (element instanceof ASTRuleDescriptor) {
			ASTRuleDescriptor descriptor = (ASTRuleDescriptor) element;
			return descriptor.getDescription();
		}
		return "";
	}

}
