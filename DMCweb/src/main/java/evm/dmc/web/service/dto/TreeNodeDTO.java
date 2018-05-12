package evm.dmc.web.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"nodes"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeNodeDTO {
	private Long id;
	private String text;
	private String tooltip;
	
	@Builder.Default
	private Boolean selectable = false;
	
	@Builder.Default
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<TreeNodeDTO> nodes = null;

}
