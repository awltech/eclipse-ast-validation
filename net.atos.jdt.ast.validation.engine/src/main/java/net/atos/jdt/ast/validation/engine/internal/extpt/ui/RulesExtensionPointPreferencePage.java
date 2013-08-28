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

import java.util.HashMap;
import java.util.Map;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;
import net.atos.jdt.ast.validation.engine.ASTRulesPreferences;
import net.atos.jdt.ast.validation.engine.internal.extpt.ASTRulesExtensionPoint;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page that contains the list of rules, to enable/disable them
 * @author mvanbesien
 *
 */
public class RulesExtensionPointPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Map containing temp states
	 */
	private Map<ASTRuleDescriptor, Boolean> states = new HashMap<ASTRuleDescriptor, Boolean>();

	/**
	 * Button temp state
	 */
	private boolean participantButtonEnabled;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		// Does nothing. Preferences Management performed thanks to
		// ASTRulesPreferences class
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {

		// Builds graphical Structure
		Composite background = new Composite(parent, SWT.NONE);
		background.setLayout(new FormLayout());

		Label message = new Label(background, SWT.WRAP);
		message.setText(RulesPreferencePagesMessages.PREFERENCE_LABEL.value());
		new FormDataBuilder().horizontal().top().height(30).apply(message);

		Button participantButton = new Button(background, SWT.CHECK);
		participantButton.setText(RulesPreferencePagesMessages.ENABLE_CUP.value());
		participantButton.setSelection(ASTRulesPreferences.isValidationParticipantEnabled());
		participantButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				participantButtonEnabled = ((Button) e.widget).getSelection();
			}
		});
		new FormDataBuilder().left().right().top(message, 15).apply(participantButton);
		
		Label comboLabel = new Label(background, SWT.NONE);
		comboLabel.setText(RulesPreferencePagesMessages.REPOSITORY_LABEL.value());
		new FormDataBuilder().left().top(participantButton, 17).width(100).apply(comboLabel);

		Combo combo = new Combo(background, SWT.READ_ONLY);
		new FormDataBuilder().left(comboLabel).top(participantButton, 15).right().apply(combo);

		ComboViewer comboViewer = new ComboViewer(combo);
		comboViewer.setContentProvider(new RulesRepositoriesContentProvider());
		comboViewer.setLabelProvider(new RulesRepositoriesLabelProvider());

		Group group = new Group(background, SWT.NONE);
		group.setLayout(new FormLayout());
		new FormDataBuilder().horizontal().top(combo).bottom().apply(group);
		group.setText(RulesPreferencePagesMessages.GROUP_LABEL.value());

		Tree tree = new Tree(group, SWT.CHECK | SWT.BORDER);
		new FormDataBuilder().fill().apply(tree);
		CheckboxTreeViewer treeViewer = new CheckboxTreeViewer(tree);
		treeViewer.setLabelProvider(new RulesLabelProvider());
		treeViewer.setContentProvider(new RulesContentProvider());
		treeViewer.setCheckStateProvider(new RulesCheckProvider(states));
		treeViewer.addCheckStateListener(new RulesCheckListener(states));

		// Add inputs
		comboViewer.addSelectionChangedListener(new RulesListRefreshmentListener(treeViewer));
		comboViewer.setInput(ASTRulesExtensionPoint.getInstance());

		return background;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		for (ASTRuleDescriptor descriptor : this.states.keySet())
			ASTRulesPreferences.setEnabled(descriptor, this.states.get(descriptor));
		
		if (this.participantButtonEnabled)
			ASTRulesPreferences.enableValidationParticipant();
		else
			ASTRulesPreferences.disableValidationParticipant();			
		
		return super.performOk();
	}

}
