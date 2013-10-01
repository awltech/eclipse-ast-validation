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
package net.atos.jdt.ast.validation.engine.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.atos.jdt.ast.validation.engine.ASTValidationEngine;
import net.atos.jdt.ast.validation.engine.internal.ValidationEngineMessages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Incremental project builder. Can be applied on a project to trigger
 * validation once source file is applied
 * 
 * @author mvanbesien
 * @since 1.0
 */
public class ValidationProjectBuilder extends IncrementalProjectBuilder {

	/**
	 * Creates new Builder
	 */
	public ValidationProjectBuilder() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor monitor)
			throws CoreException {
		final Collection<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>();
		monitor.setTaskName(ValidationEngineMessages.VALIDATING_SOURCE.value());
		// If build kind is a full build, we get all the compilation units of
		// the project
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			final IProject project = this.getProject();
			final IJavaProject javaProject = JavaCore.create(project);
			if ((javaProject != null) && javaProject.exists()) {
				monitor.subTask(ValidationEngineMessages.RETRIEVING_CU.value());
				compilationUnits.addAll(this.retrieveCompilationUnits(javaProject));
			}
		} else if ((kind == IncrementalProjectBuilder.AUTO_BUILD)
				|| (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD)) {
			// If build is an incremental build, we retrieve the elements being
			// built and we gather the compilation units within
			final IResourceDelta delta = this.getDelta(this.getProject());
			monitor.subTask(ValidationEngineMessages.RETRIEVING_CU.value());
			delta.accept(new IResourceDeltaVisitor() {

				@Override
				public boolean visit(final IResourceDelta delta) throws CoreException {
					final IResource affectedResource = delta.getResource();
					final IJavaElement affectedJavaResource = JavaCore.create(affectedResource);
					if (affectedJavaResource instanceof ICompilationUnit) {
						compilationUnits.add((ICompilationUnit) affectedJavaResource);
					}
					return true;
				}
			});
		}
		new ASTValidationEngine(compilationUnits, this.getValidRepositories()).execute(monitor);
		return new IProject[0];
	}

	public String[] getValidRepositories() {
		return new String[0];
	}

	/**
	 * Retrieves the compilation units within the provided project
	 * 
	 * @param javaProject
	 * @return
	 * @throws JavaModelException
	 */
	private Collection<? extends ICompilationUnit> retrieveCompilationUnits(final IJavaProject javaProject)
			throws JavaModelException {
		final Collection<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>();
		for (final IPackageFragmentRoot packageFragmentRoot : javaProject.getPackageFragmentRoots()) {
			if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
				for (final IJavaElement element : packageFragmentRoot.getChildren()) {
					if (element instanceof IPackageFragment) {
						final IPackageFragment packageFragment = (IPackageFragment) element;
						for (final ICompilationUnit compilationUnit : packageFragment.getCompilationUnits()) {
							compilationUnits.add(compilationUnit);
						}
					} else if (element instanceof ICompilationUnit) {
						compilationUnits.add((ICompilationUnit) element);
					}
				}
			}
		}
		return compilationUnits;
	}
}
