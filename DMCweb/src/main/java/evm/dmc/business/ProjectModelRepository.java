package evm.dmc.business;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;

public interface ProjectModelRepository extends CrudRepository<ProjectModel, Long> {
	Set<ProjectModel> findByAccountId(Long id);
}
