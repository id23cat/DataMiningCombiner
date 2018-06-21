package evm.dmc.model.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import evm.dmc.api.model.FrameworkModel;

public interface JsonService {
	String frameworksListToTreeView(List<?> list) throws JsonProcessingException;

}
