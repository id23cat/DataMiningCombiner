package evm.dmc.model.service.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import evm.dmc.model.service.JsonService;

@Service
public class JsonServiceImpl implements JsonService {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public String frameworksListToTreeView(List<?> list) throws JsonProcessingException {
		return mapper.writeValueAsString(list);
	}

}
