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
package net.atos.jdt.ast.validation.engine.internal.extpt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.atos.jdt.ast.validation.engine.ASTRuleDescriptor;
import net.atos.jdt.ast.validation.engine.ASTRulesRepository;
import net.atos.jdt.ast.validation.engine.IASTRulesDataSource;
import net.atos.jdt.ast.validation.engine.internal.Activator;
import net.atos.jdt.ast.validation.engine.internal.ValidationEngineMessages;
import net.atos.jdt.ast.validation.engine.rules.AbstractProjectContext;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * Singleton class in charge of reading Validation rules set through Extension
 * points
 * 
 * @author mvanbesien
 * @since 1.0
 * 
 */
public class ASTRulesExtensionPoint implements IASTRulesDataSource {

	// Constants of Extension point
	private static final String MANDATORY = "mandatory";
	private static final String RULE = "rule";
	private static final String DESCRIPTION = "description";
	private static final String IMPLEMENTATION = "implementation";
	private static final String CONTEXT = "context";
	private static final String DEFAULT_MARKER = "net.atos.jdt.ast.validation.engine.diagnostic";
	private static final String MARKER_ID = "markerId";
	private static final String ID = "id";
	private static final String REPOSITORY = "repository";
	private static final String REPOSITORY_NAME = "name";
	private static final String EXTPT_NAME = "ASTValidationRules";

	// End of such constants.
	/**
	 * Static internal class, in charge of holding the Singleton instance.
	 * 
	 * @generated Singleton Generator on 2012-12-18 12:15:12 CET
	 */
	private static class SingletonHolder {
		static ASTRulesExtensionPoint instance = new ASTRulesExtensionPoint();
	}

	/**
	 * Returns the Singleton instance of this class.
	 * 
	 * @generated Singleton Generator on 2012-12-18 12:15:12 CET
	 */
	public static ASTRulesExtensionPoint getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Default constructor. Generated because used in singleton instanciation &
	 * needs to be private
	 * 
	 * @generated Singleton Generator on 2012-12-18 12:15:12 CET
	 */
	private ASTRulesExtensionPoint() {
		this.repositories = new HashSet<ASTRulesRepository>();
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID,
				ASTRulesExtensionPoint.EXTPT_NAME);
		final IExtension[] extensions = extensionPoint.getExtensions();
		for (final IExtension extension : extensions) {
			for (final IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				if (ASTRulesExtensionPoint.REPOSITORY.equals(configurationElement.getName())) {
					final String repositoryID = configurationElement.getAttribute(ASTRulesExtensionPoint.ID);
					String repositoryMarkerId = configurationElement.getAttribute(ASTRulesExtensionPoint.MARKER_ID);
					if ((repositoryMarkerId == null) || (repositoryMarkerId.trim().length() == 0)) {
						repositoryMarkerId = ASTRulesExtensionPoint.DEFAULT_MARKER;
					}
					
					String repositoryName = configurationElement.getAttribute(ASTRulesExtensionPoint.REPOSITORY_NAME);
					if (repositoryName == null || repositoryName.length() == 0)
						repositoryName = repositoryID;
					final ASTRulesRepository repository = new ASTRulesRepository(repositoryID, repositoryMarkerId);
					repository.setName(repositoryName);
					
					for (final IConfigurationElement contextElement : configurationElement
							.getChildren(ASTRulesExtensionPoint.CONTEXT)) {
						try {
							final AbstractProjectContext context = (AbstractProjectContext) contextElement
									.createExecutableExtension(ASTRulesExtensionPoint.IMPLEMENTATION);
							repository.setContext(context);
						} catch (final Exception e) {
							Activator
									.getDefault()
									.getLog()
									.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
											ValidationEngineMessages.CONTEXT_LOADING_EXCEPTION.value(contextElement
													.getAttribute(ASTRulesExtensionPoint.IMPLEMENTATION)), e));
						}
					}
					for (final IConfigurationElement contextElement : configurationElement
							.getChildren(ASTRulesExtensionPoint.RULE)) {
						try {
							final String description = contextElement.getAttribute(ASTRulesExtensionPoint.DESCRIPTION);
							final boolean mandatory = Boolean.valueOf(contextElement
									.getAttribute(ASTRulesExtensionPoint.MANDATORY));
							final ASTExtensionPointRulesFactory factory = new ASTExtensionPointRulesFactory(
									contextElement, ASTRulesExtensionPoint.IMPLEMENTATION);
							repository.registerRule(new ASTRuleDescriptor(description, factory, mandatory));
						} catch (final Exception e) {
							Activator
									.getDefault()
									.getLog()
									.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
											ValidationEngineMessages.RULE_LOADING_EXCEPTION.value(contextElement
													.getAttribute(ASTRulesExtensionPoint.IMPLEMENTATION)), e));
						}
					}
					this.repositories.add(repository);
				}
			}
		}
	}

	/**
	 * List of resolved repositories
	 */
	private Set<ASTRulesRepository> repositories = null;

	/**
	 * Returns true if ID provided is in array provided too.
	 * 
	 * @param repositoryId
	 * @param repositoriesId
	 * @return
	 */
	private boolean isValidRepositoryId(final String repositoryId, final String[] repositoriesId) {
		if (repositoriesId.length == 0) {
			return true;
		}
		for (final String repoId : repositoriesId) {
			if ((repoId != null) && repoId.equals(repositoryId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the list of repositories, filtered by IDs. If none is provided,
	 * returns all repositories.
	 * 
	 * @param repositories
	 * @return
	 */
	@Override
	public List<ASTRulesRepository> getRepositories(final String... repositories) {
		final List<ASTRulesRepository> repositoriesList = new ArrayList<ASTRulesRepository>();
		for (final ASTRulesRepository repository : this.repositories) {
			if ((repositories.length == 0) || this.isValidRepositoryId(repository.getId(), repositories)) {
				repositoriesList.add(repository);
			}
		}
		return repositoriesList;
	}

}
