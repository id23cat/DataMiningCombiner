package evm.dmc.python.data;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Enumeration;

import evm.dmc.core.api.Data;
import evm.dmc.core.api.Statistics;
import evm.dmc.core.api.exceptions.DataOperationException;
import evm.dmc.core.api.exceptions.IndexOutOfRange;

@Service("Python_DataFrame")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PandasDataFrame extends JepVariable {

	public PandasDataFrame() {
		super();
	}

	public PandasDataFrame(String variable) {
		super(variable);
	}

	@Override
	public int getAttributesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Data<?> getAttribute(int index) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data<?> getAttribute(String name) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data<?> getAttributes(int... indexes) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueAsString(int row, int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getValue(int row, int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNominal(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isString(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDate(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNumeric(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getIndexByName(String name) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAttributeName(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Object> enumerateValues(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Object> enumerateValues(String name) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statistics getAttributeStatistics(int column) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statistics getAttributeStatistics(int column, int bins) throws IndexOutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toNominal(int column) throws IndexOutOfRange, DataOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAllAsString() {
		// TODO Auto-generated method stub
		return null;
	}

}
