package evm.dmc.web.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.stereotype.Service;

@Service
public interface JsonService {
    String frameworksListToTreeView(List<?> list) throws JsonProcessingException;

}
