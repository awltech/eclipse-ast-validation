package net.atos.jdt.ast.validation.engine.internal.extpt.ui;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * Selection listener that refreshes the rules list when users selects another
 * rule repository.
 * 
 * @author mvanbesien
 * 
 */
public class RulesListRefreshmentListener implements ISelectionChangedListener {

	/**
	 * viewer to be refreshed when event is caught
	 */
	private CheckboxTreeViewer treeViewer;

	/**
	 * Creates new listener
	 * 
	 * @param treeViewer
	 *            viewer to be refreshed when event is caught
	 */
	public RulesListRefreshmentListener(CheckboxTreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		this.treeViewer.setInput(((IStructuredSelection) selection).iterator().next());
		this.treeViewer.refresh();
	}

}
