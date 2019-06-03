package evm.dmc.rest.controllers;

import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.rest.annotations.HateoasRelation;
import evm.dmc.webApi.dto.AlgorithmDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(RestAlgorithmController.BASE_URL)
@CrossOrigin(origins = "*")
@Slf4j
public class RestAlgorithmController extends AbstractRestCrudController<AlgorithmDto> {

    final static String BASE_URL = "/rest/{accountId}/project/{projectId}/algorithm";

    private ModelMapper modelMapper;

    @Autowired
    public RestAlgorithmController(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

    @Override
    @PostMapping
    @HateoasRelation("addAlgorithm")
    public AlgorithmDto addInstance(AlgorithmDto dto, @PathVariable Long accountId, @PathVariable Long projectId) {

        AlgorithmDto resultDto = new AlgorithmDto();
        resultDto.setAlgorithmId(1L);
        return resultDto;
    }

    @Override
    @PutMapping
    @HateoasRelation("updateAlgorithm")
    public AlgorithmDto updateInstance(AlgorithmDto dto, @PathVariable Long accountId, @PathVariable Long projectId) {

        AlgorithmDto resultDto = new AlgorithmDto();
        resultDto.setAlgorithmId(1L);
        return resultDto;
    }

    @Override
    @DeleteMapping("/{entityId}")
    @HateoasRelation("deleteAlgorithm")
    public AlgorithmDto deleteInstance(@PathVariable Long accountId, @PathVariable Long projectId, @PathVariable Long entityId) {

        AlgorithmDto resultDto = new AlgorithmDto();
        resultDto.setAlgorithmId(1L);
        return resultDto;
    }

    @Override
    @GetMapping("/{entityId}")
    @HateoasRelation("getAlgorithm")
    public AlgorithmDto getInstance(@PathVariable Long accountId, @PathVariable Long projectId, @PathVariable Long entityId) {

        AlgorithmDto resultDto = new AlgorithmDto();
        resultDto.setAlgorithmId(1L);
        return resultDto;
    }

    @Override
    @GetMapping("/all")
    @HateoasRelation("getAlgorithmList")
    public List<AlgorithmDto> getInstanceList(@PathVariable Long accountId, @PathVariable Long projectId) {

        AlgorithmDto resultDto = new AlgorithmDto();
        resultDto.setAlgorithmId(1L);

        List<AlgorithmDto> list = new ArrayList<>();
        list.add(resultDto);

        return list;
    }

    private AlgorithmDto convertToDto(Algorithm algorithm) {

        return modelMapper.map(algorithm, AlgorithmDto.class);
    }

    private Algorithm convertToEntity(AlgorithmDto dto) {

        return modelMapper.map(dto, Algorithm.class);
    }
}
