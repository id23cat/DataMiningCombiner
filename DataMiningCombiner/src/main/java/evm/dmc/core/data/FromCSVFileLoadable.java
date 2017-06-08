package evm.dmc.core.data;

public interface FromCSVFileLoadable extends FromFileLoadable {

	Data load(String fileNmae, int classIndex) throws Exception;

	Data load(String fileNmae, int classIndex, java.lang.String separator) throws Exception;

}
