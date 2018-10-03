package evm.dmc.core.api.back.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * The container of {@link evm.dmc.core.api.Data} model.
 *
 * @see     evm.dmc.core.api.Data
 */
@Data
@EqualsAndHashCode
@ToString
public class DataModel {
	// TODO: using JSON
	public static final int DEFAULT_ROWS_COUNT = 10;
	private Map<String, String> titleTypeMap = new HashMap<>();
	private int rowsCount = DEFAULT_ROWS_COUNT;
	private String[][] preview;
}
