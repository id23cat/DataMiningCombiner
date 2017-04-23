package evm.dmc.core.data;

import java.io.IOException;

public interface FromCSVFileLoadable extends FromFileLoadable {

	Data load(String fileNmae, int classIndex) throws IOException;

	Data load(String fileNmae, int classIndex, java.lang.String separator) throws IOException;

}
