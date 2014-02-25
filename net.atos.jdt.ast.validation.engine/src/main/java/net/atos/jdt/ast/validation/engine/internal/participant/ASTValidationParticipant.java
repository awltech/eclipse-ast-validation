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
package net.atos.jdt.ast.validation.engine.internal.participant;

import java.util.ArrayList;
import java.util.Collection;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;
import net.atos.jdt.ast.validation.engine.ASTRulesPreferences;
import net.atos.jdt.ast.validation.engine.ASTRulesRepository;
import net.atos.jdt.ast.validation.engine.ASTValidationEngine;
import net.atos.jdt.ast.validation.engine.ASTValidationProblem;
import net.atos.jdt.ast.validation.engine.IASTRulesDataSource;
import net.atos.jdt.ast.validation.engine.internal.Activator;
import net.atos.jdt.ast.validation.engine.internal.extpt.ASTRulesExtensionPoint;
import net.atos.jdt.ast.validation.engine.rules.AbstractASTRule;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Compilation Participant, enabled through Preferences, that triggers the
 * validation on the processed sources..
 * 
 * @author mvanbesien
 * @since 1.1
 * 
 */
public class ASTValidationParticipant extends CompilationParticipant {

	/**
	 * Data Source instance. Compilation Participant should only rely on
	 * Extension point, as there is no way to provide him specific input rules.
	 */
	private final IASTRulesDataSource dataSource = ASTRulesExtensionPoint.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.compiler.CompilationParticipant#isActive(org.eclipse
	 * .jdt.core.IJavaProject)
	 */
	@Override
	public boolean isActive(final IJavaProject project) {
		return ASTRulesPreferences.isValidationParticipantEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.compiler.CompilationParticipant#isAnnotationProcessor
	 * ()
	 */
	@Override
	public boolean isAnnotationProcessor() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.compiler.CompilationParticipant#reconcile(org.eclipse
	 * .jdt.core.compiler.ReconcileContext)
	 */
	@Override
	public void reconcile(final ReconcileContext context) {

		// We check if we can execute...
		if (!ASTRulesPreferences.isValidationParticipantEnabled()) {
			return;
		}

		// At first, let's do some validation.
		CompilationUnit domCU = null;
		try {
			domCU = context.getAST4();
		} catch (final JavaModelException e) {
			Activator.logException(e);
			return;
		}
		if (domCU == null) {
			return;
		}

		final IJavaElement javaElement = domCU.getJavaElement();
		if (!(javaElement instanceof ICompilationUnit)) {
			return;
		}

		// Now we perform the process
		ICompilationUnit iCompilationUnit = (ICompilationUnit) javaElement;

		if (iCompilationUnit.getResource() != null && iCompilationUnit.getResource().exists()) {
			for (final ASTRulesRepository repository : this.dataSource.getRepositories()) {
				try {
					iCompilationUnit.getResource().deleteMarkers(repository.getMarkerId(), true, IResource.DEPTH_ZERO);
				} catch (final CoreException e) {
					Activator.logException(e);
				}
				if (repository.isEnabled(iCompilationUnit)) {
					for (final ASTRuleDescriptor ruleDescriptor : repository.getRules(iCompilationUnit)) {
						final AbstractASTRule rule = ruleDescriptor.getRule();
						domCU.accept(rule);
						for (final ASTValidationProblem problem : rule.getProblems()) {
							problem.toMarker(iCompilationUnit.getResource());
						}
					}
				}
			}
		}
	}

	@Override
	public void buildStarting(BuildContext[] files, boolean isBatch) {

		// Only if enabled, we start checking the source...
		if (ASTRulesPreferences.isValidationParticipantEnabled()) {

			// If enabled, we collect all the compilation units raised by the
			// context
			final Collection<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>();
			for (final BuildContext file : files) {
				final IFile iFile = file.getFile();
				if (iFile != null && iFile.exists()) {
					final IJavaElement javaElement = JavaCore.create(iFile);
					if (javaElement instanceof ICompilationUnit) {
						compilationUnits.add((ICompilationUnit) javaElement);
					}
				}
			}
			try {

				// For all collected units, we validate them.
				new ASTValidationEngine(compilationUnits).execute(new NullProgressMonitor());
			} catch (CoreException e) {
				Activator.logException(e);
			}
		}

		// Resume the process...
		super.buildStarting(files, isBatch);
	}

}
