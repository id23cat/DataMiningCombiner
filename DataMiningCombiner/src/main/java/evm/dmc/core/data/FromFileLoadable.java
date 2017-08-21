package evm.dmc.core.data;

import evm.dmc.core.api.Data;

public interface FromFileLoadable {
	Data load(String fileName) throws Exception;

}
