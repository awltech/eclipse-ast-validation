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
package net.atos.jdt.ast.validation.engine.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;
import net.atos.jdt.ast.validation.engine.ASTValidationProblem;
import net.atos.jdt.ast.validation.engine.internal.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Abstract AST Rule. this class overrides the AST Visitor one.
 * 
 * This means that all the methods for AST Visitor can be used, except the
 * visit(CompilationUnit) one.
 * 
 * Additionally, the abstract implementation contains helping methods
 * (addXXXMarker) to automatically create markers at the right place in the
 * code.
 * 
 * @author mvanbesien
 * @since 1.0
 */
public abstract class AbstractASTRule extends ASTVisitor {

	/**
	 * Key used for identifing the rule that raised the marker
	 */
	public static final String RULE_ID_KEY = Activator.PLUGIN_ID + ".ruleId";

	/**
	 * Java Compilation Unit processed
	 */
	private ICompilationUnit compilationUnit;

	/**
	 * DOM Compilation Unit processed
	 */
	private CompilationUnit domCompilationUnit;

	/**
	 * AST Rule Descriptor
	 */
	private ASTRuleDescriptor ruleDescriptor;

	/**
	 * Problems raised during visit.
	 */
	private List<ASTValidationProblem> problems = new ArrayList<ASTValidationProblem>();

	/**
	 * Session containing properties from caller
	 */
	private Map<String, Object> session = new HashMap<String, Object>();

	/**
	 * Sets the rule repository. should not be invoked by clients
	 * 
	 * @param ruleRepository
	 */
	public final void setRuleDescriptor(final ASTRuleDescriptor ruleDescriptor) {
		this.ruleDescriptor = ruleDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * CompilationUnit)
	 */
	@Override
	public final boolean visit(final CompilationUnit node) {
		this.problems.clear();
		this.domCompilationUnit = node;
		final IJavaElement javaElement = node.getJavaElement();
		if (javaElement instanceof ICompilationUnit) {
			this.compilationUnit = (ICompilationUnit) javaElement;
		}
		return this.visit2(node);
	}

	/**
	 * This method should be used to override the visit(CompilationUnit) method.
	 * Can we overriden by clients
	 * 
	 * @param node
	 * @return
	 */
	public boolean visit2(final CompilationUnit node) {
		return true;
	}

	/**
	 * Creates an Error marker for the AST node provided, with message
	 * 
	 * @param node
	 * @param message
	 */
	protected void addErrorMarker(final ASTNode node, final String message) {
		this.addMarker(node, message, false, true);
	}

	/**
	 * Creates a Warning marker for the AST node provided, with message
	 * 
	 * @param node
	 * @param message
	 */
	protected void addWarningMarker(final ASTNode node, final String message) {
		this.addMarker(node, message, true, false);
	}

	/**
	 * Creates an Info marker for the AST node provided, with message
	 * 
	 * @param node
	 * @param message
	 */
	protected void addInfoMarker(final ASTNode node, final String message) {
		this.addMarker(node, message, false, false);
	}

	/**
	 * Creates an marker for the AST node provided, with message and severity
	 * 
	 * @deprecated use addMarker(ASTNode, String, boolean, boolean)
	 * @param node
	 * @param message
	 * @param severity
	 *            (from IMarker.SEVERITY_xxx)
	 * 
	 */
	@Deprecated
	protected void addMarker(final ASTNode node, final String message, final int severity) {
		boolean isWarning = severity == IMarker.SEVERITY_WARNING;
		boolean isError = severity == IMarker.SEVERITY_ERROR;
		this.addMarker(node, message, isWarning, isError);
	}

	/**
	 * Creates and stores a Problem in the internal list of problems
	 * 
	 * @param node
	 * @param message
	 * @param isWarning
	 * @param isError
	 */
	protected void addMarker(final ASTNode node, final String message, final boolean isWarning, final boolean isError) {
		final IResource resource = this.compilationUnit.getResource();
		String markerId = this.ruleDescriptor.getRepository().getMarkerId();
		int lineNumber = this.getLineNumber(node);
		int startChar = node.getStartPosition();
		int endChar = node.getStartPosition() + node.getLength();
		this.problems.add(new ASTValidationProblem(message, isWarning, isError, resource.getName(), markerId,
				lineNumber, startChar, endChar));
	}

	/**
	 * Returns the position of the object in the code
	 * 
	 * @param node
	 * @return
	 */
	private int getLineNumber(final ASTNode node) {
		final int startPosition = node.getStartPosition();
		return this.domCompilationUnit.getLineNumber(startPosition);
	}

	/**
	 * @return list of problems raised by the last visit of this iterator
	 */
	public List<ASTValidationProblem> getProblems() {
		return problems;
	}

	/**
	 * Sets the current execution session
	 * @param session
	 */
	public void setSession(Map<String, Object> session) {
		this.session.clear();
		this.session.putAll(session);
	}
	
	/**
	 * Returns the current internal session
	 * @return
	 */
	public Map<String, Object> getSession() {
		return session;
	}

}
