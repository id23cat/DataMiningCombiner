package evm.dmc.core.api.back.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Properties;

@Data
@EqualsAndHashCode
public class DataStatistics extends DataInfo {
	private Properties statValues = new Properties();
}
