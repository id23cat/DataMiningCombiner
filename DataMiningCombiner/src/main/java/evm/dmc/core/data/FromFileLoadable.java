package evm.dmc.core.data;

import java.io.IOException;

public interface FromFileLoadable {
	Data load(String fileNmae) throws IOException;

}
