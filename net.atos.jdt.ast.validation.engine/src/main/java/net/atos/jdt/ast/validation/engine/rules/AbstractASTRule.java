package net.atos.jdt.ast.validation.engine.rules;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;
import net.atos.jdt.ast.validation.engine.internal.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
 * @author Maxence Vanb�sien
 * @since 1.0
 */
public abstract class AbstractASTRule extends ASTVisitor {

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
		this.addMarker(node, message, IMarker.SEVERITY_ERROR);
	}

	/**
	 * Creates a Warning marker for the AST node provided, with message
	 * 
	 * @param node
	 * @param message
	 */
	protected void addWarningMarker(final ASTNode node, final String message) {
		this.addMarker(node, message, IMarker.SEVERITY_WARNING);
	}

	/**
	 * Creates an Info marker for the AST node provided, with message
	 * 
	 * @param node
	 * @param message
	 */
	protected void addInfoMarker(final ASTNode node, final String message) {
		this.addMarker(node, message, IMarker.SEVERITY_INFO);
	}

	/**
	 * Creates an marker for the AST node provided, with message and severity
	 * 
	 * @param node
	 * @param message
	 * @param severity
	 *            (from IMarker.SEVERITY_xxx)
	 */
	protected void addMarker(final ASTNode node, final String message, final int severity) {
		final IResource resource = this.compilationUnit.getResource();
		try {
			final IMarker createdMarker = resource.createMarker(this.ruleDescriptor.getRepository().getMarkerId());
			createdMarker.setAttribute(IMarker.LINE_NUMBER, this.getLineNumber(node));
			createdMarker.setAttribute(IMarker.LOCATION, "line " + this.getLineNumber(node));
			createdMarker.setAttribute(IMarker.MESSAGE, message);
			createdMarker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			createdMarker.setAttribute(IMarker.SEVERITY, severity);
			createdMarker.setAttribute(IMarker.TRANSIENT, false);
			createdMarker.setAttribute(IMarker.LINE_NUMBER, this.getLineNumber(node));
			createdMarker.setAttribute(IMarker.CHAR_START, node.getStartPosition());
			createdMarker.setAttribute(IMarker.CHAR_END, node.getStartPosition() + node.getLength());
		} catch (final CoreException e) {
			Activator.logException(e);
		}
	}

	/**
	 * Returns the position of the object in the code
	 * 
	 * @param node
	 * @return
	 */
	private Object getLineNumber(final ASTNode node) {
		final int startPosition = node.getStartPosition();
		return this.domCompilationUnit.getLineNumber(startPosition);
	}

}
