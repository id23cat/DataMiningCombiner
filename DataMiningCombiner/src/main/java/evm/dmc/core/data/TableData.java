package evm.dmc.core.data;

import java.io.IOException;

import com.github.lwhite1.tablesaw.api.Table;

public class TableData extends InMemoryData<Table> implements FromFileLoadable, ToFileStorable {

	@Override
	public Data load(String fileName) throws IOException {
		// if (fileName.endsWith(".csv"))
		this.setData(Table.createFromCsv(fileName));
		// else
		// throw new UnsupportedOperationException("Only CSV files are supported
		// currently");

		return this;
	}

	@Override
	public void store(String fileName) {
		super.data.save(fileName);

	}

}
