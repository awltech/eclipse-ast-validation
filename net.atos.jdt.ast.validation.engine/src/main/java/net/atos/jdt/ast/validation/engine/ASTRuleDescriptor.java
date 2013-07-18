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

import java.util.UUID;

import net.atos.jdt.ast.validation.engine.rules.AbstractASTRule;

/**
 * Rules Descriptor Contains rule implementation & some information about it...
 * 
 * @author mvanbesien
 * 
 */
public class ASTRuleDescriptor {

	/**
	 * Rule ID
	 */
	private String id;

	/**
	 * Rule Description
	 */
	private String description;

	/**
	 * Rule implementation
	 */
	private AbstractASTRule rule;

	/**
	 * Rule implementation
	 */
	private ASTRulesRepository repository;

	/**
	 * True is the rule cannot be disabled/bypassed. false otherwise
	 */
	private boolean mandatory;

	/**
	 * Create Rule Descriptor
	 * 
	 * @param id
	 * @param description
	 * @param rule
	 * @param mandatory
	 */
	public ASTRuleDescriptor(String description, AbstractASTRule rule, boolean mandatory) {
		this.id = UUID.randomUUID().toString();
		this.description = description != null ? description : "";
		this.rule = rule;
		this.rule.setRuleDescriptor(this);
		this.mandatory = mandatory;
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return rule
	 */
	public AbstractASTRule getRule() {
		return rule;
	}

	/**
	 * @return repository
	 */
	public ASTRulesRepository getRepository() {
		return repository;
	}

	/**
	 * Sets contained repository.
	 * 
	 * @param repository
	 */
	public void setRuleRepository(ASTRulesRepository repository) {
		this.repository = repository;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ASTRuleDescriptor))
			return false;
		return this.id.equals(((ASTRuleDescriptor) obj).id);
	}
	
	/**
	 * Returns whether this rule is a rule that can be disabled.
	 * @return
	 */
	public boolean isMandatory() {
		return mandatory;
	}

}
