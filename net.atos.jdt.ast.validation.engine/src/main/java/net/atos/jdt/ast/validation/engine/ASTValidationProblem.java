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
package net.atos.jdt.ast.validation.engine;

import net.atos.jdt.ast.validation.engine.internal.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.CategorizedProblem;

/**
 * Class that identifies a problem raised by the AST validation
 * 
 * @author mvanbesien
 * @since 1.1
 * 
 */
public class ASTValidationProblem extends CategorizedProblem {

	/**
	 * Problem message
	 */
	private String message;

	/**
	 * true if warning, false otherwise
	 */
	private boolean isWarning;

	/**
	 * true if error, false otherwise
	 */
	private boolean isError;

	/**
	 * Name of the file that holds the error
	 */
	private String fileName;

	/**
	 * Marker identifier
	 */
	private String markerId;

	/**
	 * Line where the problem happens
	 */
	private int lineNumber;

	/**
	 * Position in the source where the problems occurs
	 */
	private int startChar;

	/**
	 * Position in the source, identifing the end of the problem.
	 */
	private int endChar;

	/**
	 * Creates a new Validation Problem instance
	 * 
	 * @param message
	 *            Problem message
	 * @param isWarning
	 *            true if warning, false otherwise
	 * @param isError
	 *            true if error, false otherwise
	 * @param fileName
	 *            name of the file that holds the problem
	 * @param markerId
	 *            identifier of the problem
	 * @param lineNumber
	 *            line in the source, where the problem happens
	 * @param startChar
	 *            position in the source where starts the problem
	 * @param endChar
	 *            position in the source where ends the problem
	 */
	public ASTValidationProblem(String message, boolean isWarning, boolean isError, String fileName, String markerId,
			int lineNumber, int startChar, int endChar) {
		this.message = message;
		this.isWarning = isWarning;
		this.isError = isError;
		this.fileName = fileName;
		this.markerId = markerId;
		this.lineNumber = lineNumber;
		this.startChar = startChar;
		this.endChar = endChar;
	}

	/**
	 * Returns arguments of problem. Default empty
	 */
	@Override
	public String[] getArguments() {
		return new String[0];
	}

	/**
	 * Returns ID of the problem. default O,
	 */
	@Override
	public int getID() {
		return 0;
	}

	/**
	 * Returns the message of the problem
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/**
	 * Returns the name of the file that holds the problem
	 */
	@Override
	public char[] getOriginatingFileName() {
		return this.fileName.toCharArray();
	}

	/**
	 * Returns the position in the source where the problem ends
	 */
	@Override
	public int getSourceEnd() {
		return this.endChar;
	}

	/**
	 * Returns the line in the source, where the problem happens
	 */
	@Override
	public int getSourceLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Returns the position in the source where the problems starts
	 */
	@Override
	public int getSourceStart() {
		return this.startChar;
	}

	/**
	 * Returns true if problem is an error, false otherwise
	 */
	@Override
	public boolean isError() {
		return this.isError;
	}

	/**
	 * Returns true if the problem is a warning, false otherwise
	 */
	@Override
	public boolean isWarning() {
		return this.isWarning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.compiler.IProblem#setSourceEnd(int)
	 */
	@Override
	public void setSourceEnd(int sourceEnd) {
		throw new UnsupportedOperationException("ASTValidationProblem instances are unmodifiable");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.compiler.IProblem#setSourceLineNumber(int)
	 */
	@Override
	public void setSourceLineNumber(int lineNumber) {
		throw new UnsupportedOperationException("ASTValidationProblem instances are unmodifiable");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.compiler.IProblem#setSourceStart(int)
	 */
	@Override
	public void setSourceStart(int sourceStart) {
		throw new UnsupportedOperationException("ASTValidationProblem instances are unmodifiable");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.compiler.CategorizedProblem#getCategoryID()
	 */
	@Override
	public int getCategoryID() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.compiler.CategorizedProblem#getMarkerType()
	 */
	@Override
	public String getMarkerType() {
		return this.markerId;
	}

	/**
	 * Applies the current problem to the resource passed as a parameter. (it is
	 * internally turned into a marker)
	 * 
	 * @param resource
	 */
	public void toMarker(IResource resource) {

		try {
			final IMarker createdMarker = resource.createMarker(this.getMarkerType());
			createdMarker.setAttribute(IMarker.LINE_NUMBER, this.getSourceLineNumber());
			createdMarker.setAttribute(IMarker.LOCATION, "line " + this.getSourceLineNumber());
			createdMarker.setAttribute(IMarker.MESSAGE, this.getMessage());
			createdMarker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);

			int severity = IMarker.SEVERITY_INFO;
			if (this.isError())
				severity = IMarker.SEVERITY_ERROR;
			else if (this.isWarning())
				severity = IMarker.SEVERITY_WARNING;

			createdMarker.setAttribute(IMarker.SEVERITY, severity);
			createdMarker.setAttribute(IMarker.TRANSIENT, false);
			createdMarker.setAttribute(IMarker.CHAR_START, this.getSourceStart());
			createdMarker.setAttribute(IMarker.CHAR_END, this.getSourceEnd());
		} catch (final CoreException e) {
			Activator.logException(e);
		}
	}

}
