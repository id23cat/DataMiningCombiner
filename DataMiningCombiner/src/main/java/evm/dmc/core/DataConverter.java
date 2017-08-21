package evm.dmc.core;

import evm.dmc.core.api.Data;

public interface DataConverter {
	default Object convert(Data data, Class type) {
		throw new UnsupportedOperationException("Converting Data object to type" + type + "is not avaliable");
	}

	// default StringData convert(Data data) {
	// throw new UnsupportedOperationException("Converting Data object to String
	// is not avaliable");
	// }

}
