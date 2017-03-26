package evm.dmc.core.data;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public interface Data<T> {
	default String getDescription() { return "Unknown data";}
	
	List<T> getArgs();
	File getFile();
	Stream<T> getStream();
	String getFileName();
	
//	String getTableName();
//	RDD getRDD();
	
	void setArgs(List<T> args);
	void setFile(File file);
	void setStream(Stream<T> stream);
	void setFileName(String fname);

}
