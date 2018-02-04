package evm.dmc.core.data;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.data.DataModel;

@Service
@Scope("prototype")
public abstract class InMemoryData<T> implements Data<T>, Cloneable {
	protected T data = null;

	public InMemoryData() {

	}

	public InMemoryData(T data) {
		super();
		this.data = data;
	}

	public InMemoryData(Data<T> data) {
		super();
		this.data = data.getData();
	}

	/**
	 * @return the data
	 */
	@Override
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	@Override
	public void setData(T data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InMemoryData))
			return false;
		InMemoryData<?> other = (InMemoryData<?>) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

}
