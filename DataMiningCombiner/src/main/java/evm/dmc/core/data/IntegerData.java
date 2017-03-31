package evm.dmc.core.data;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class IntegerData extends InMemoryData<Integer> {
	public IntegerData() {
		super();
	}

	public IntegerData(Integer data) {
		super(data);
	}

	public IntegerData(IntegerData data) {
		super(data);
	}

}
