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

import java.util.HashSet;
import java.util.Set;

import net.atos.jdt.ast.validation.engine.rules.AbstractProjectContext;

import org.eclipse.jdt.core.ICompilationUnit;

/**
 * AST Rule Repository. Contains rules and information about context & markers.
 * 
 * @author mvanbesien
 * @since 1.0
 * 
 */
public class ASTRulesRepository {

	/**
	 * Rule Repository ID
	 */
	private final String id;

	/**
	 * Rule Repository name (used in preferences display)
	 */
	private String name;
	
	/**
	 * Context instance. In case user wants more complicated implementation on
	 * specific project.
	 */
	private AbstractProjectContext context = new AbstractProjectContext() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * net.atos.jdt.ast.validation.engine.rules.AbstractProjectContext#validate
		 * (org.eclipse.jdt.core.ICompilationUnit)
		 */
		@Override
		public boolean validate(final ICompilationUnit compilationUnit) {
			return true;
		}

	};

	/**
	 * List of read rules.
	 */
	private final Set<ASTRuleDescriptor> rules = new HashSet<ASTRuleDescriptor>();

	/**
	 * Eclipse Core Problem Marker ID
	 */
	private final String markerId;

	/**
	 * Creates new repository with ID & Associated Marker
	 * 
	 * @param id
	 * @param markerId
	 */
	public ASTRulesRepository(final String id, final String markerId) {
		this.id = id;
		this.markerId = markerId;
	}

	/**
	 * Repository's ID
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Repository's name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Repository's name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Sets context to this repository.
	 * 
	 * @param context
	 */
	public void setContext(final AbstractProjectContext context) {
		this.context = context;
	}

	/**
	 * Returns the rules within this repository, that match the compilation unit
	 * passed as parameter (filtered by context)
	 * 
	 * @param compilationUnit
	 * @return
	 */
	public Set<ASTRuleDescriptor> getRules(final ICompilationUnit compilationUnit) {
		final Set<ASTRuleDescriptor> rules = new HashSet<ASTRuleDescriptor>();
		for (final ASTRuleDescriptor rule : this.rules) {
			final boolean ruleEnabled = rule.isMandatory() || ASTRulesPreferences.isEnabled(rule);
			if (ruleEnabled && this.context.validate(compilationUnit)) {
				rules.add(rule);
			}
		}
		return rules;
	}

	/**
	 * Returns all the rules located within this repository. (Unfiltered at all)
	 * 
	 * @return
	 */
	public Set<ASTRuleDescriptor> getRules() {
		return new HashSet<ASTRuleDescriptor>(this.rules);
	}

	/**
	 * Sets all rules provided as rules from repository.
	 * 
	 * @param ruleDescriptors
	 */
	public void registerRules(final Set<ASTRuleDescriptor> ruleDescriptors) {
		this.rules.clear();
		for (final ASTRuleDescriptor rule : ruleDescriptors) {
			this.rules.add(rule);
			rule.setRuleRepository(this);
		}

	}

	/**
	 * Set provided rule as rule from repository.
	 * 
	 * @param rule
	 */
	public void registerRule(final ASTRuleDescriptor rule) {
		this.rules.add(rule);
		rule.setRuleRepository(this);
	}

	/**
	 * Returns marker ID for this repository.
	 * 
	 * @return
	 */
	public String getMarkerId() {
		return this.markerId;
	}

	/**
	 * Checks if enabled for {@link CompilationUnit}
	 * 
	 * @param javaProject
	 * @return
	 */
	public boolean isEnabled(final ICompilationUnit compilationUnit) {
		return this.context.validate(compilationUnit);
	}

}
