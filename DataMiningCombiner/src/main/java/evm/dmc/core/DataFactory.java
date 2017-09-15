package evm.dmc.core;

import evm.dmc.core.api.Data;

public interface DataFactory {

	default Data getData(String file) {
		throw new UnsupportedOperationException("Loading from a file not supported");
	};

	default Data getData(Object rawData) {
		throw new UnsupportedOperationException("Convertion from unknown type not supported");
	}

	default Data getData(Number num) {
		throw new UnsupportedOperationException("Convertion from number type not supported");
	}

	default Data getData(Data otherFormat) {
		throw new UnsupportedOperationException("Convertion from other Data type not supported");
	}

	Data getData(Class<?> type);

	// default Data getData(Class type) {
	// throw new UnsupportedOperationException("Creating Data object for type" +
	// type + "is not avaliable");
	// }

	Data castToNativeData(Data data) throws ClassCastException;

}
