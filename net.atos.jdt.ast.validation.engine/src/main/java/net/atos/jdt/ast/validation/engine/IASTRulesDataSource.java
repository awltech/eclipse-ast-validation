package net.atos.jdt.ast.validation.engine;

import java.util.List;


/**
 * Interface that implementations that produce rules have to implement.
 * 
 * @author mvanbesien
 * 
 */
public interface IASTRulesDataSource {

	/**
	 * Return the list of repositories, filtered by IDs. If none is provided,
	 * returns all repositories.
	 * 
	 * @param repositories
	 * @return
	 */
	public List<ASTRulesRepository> getRepositories(final String... repositories);

}
