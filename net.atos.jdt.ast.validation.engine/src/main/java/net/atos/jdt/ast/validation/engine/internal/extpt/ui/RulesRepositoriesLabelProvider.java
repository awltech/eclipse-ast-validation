package net.atos.jdt.ast.validation.engine.internal.extpt.ui;

import net.atos.jdt.ast.validation.engine.ASTRulesRepository;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Rules repository label provider
 * 
 * @author mvanbesien
 * 
 */
public class RulesRepositoriesLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ASTRulesRepository)
			return ((ASTRulesRepository) element).getId();
		return "";
	}

}
