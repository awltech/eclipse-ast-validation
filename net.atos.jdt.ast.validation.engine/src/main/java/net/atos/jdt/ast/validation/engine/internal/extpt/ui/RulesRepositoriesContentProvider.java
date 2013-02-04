package net.atos.jdt.ast.validation.engine.internal.extpt.ui;

import java.util.ArrayList;
import java.util.List;

import net.atos.jdt.ast.validation.engine.IASTRulesDataSource;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Rules Repositories content Provider
 * @author mvanbesien
 *
 */
public class RulesRepositoriesContentProvider implements IStructuredContentProvider {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		
		if (inputElement instanceof IASTRulesDataSource) {
			List<Object> objects = new ArrayList<Object>();
			objects.addAll(((IASTRulesDataSource) inputElement).getRepositories());
			return objects.toArray();
		}
		return new Object[0];
	}

}