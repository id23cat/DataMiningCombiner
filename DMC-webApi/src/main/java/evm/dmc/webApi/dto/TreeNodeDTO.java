package evm.dmc.webApi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents composite pattern of data mining tree nodes used on the frontend side.
 * Tree node can be Algorithm, Function, Frameworks or whatever stuff that can be
 * visualized on the frontend side.
 */
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
