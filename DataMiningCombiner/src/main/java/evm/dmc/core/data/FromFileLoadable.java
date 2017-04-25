package evm.dmc.core.data;

public interface FromFileLoadable {
	Data load(String fileName) throws Exception;

}
