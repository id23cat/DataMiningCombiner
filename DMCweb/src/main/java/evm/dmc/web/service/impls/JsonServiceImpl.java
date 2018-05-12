package evm.dmc.web.service.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.web.service.JsonService;
import evm.dmc.web.service.dto.TreeNodeDTO;

@Service
public class JsonServiceImpl implements JsonService {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public String frameworksListToTreeView(List<?> list) throws JsonProcessingException {
		return mapper.writeValueAsString(list);
	}

}
