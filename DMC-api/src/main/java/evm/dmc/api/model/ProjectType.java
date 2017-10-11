package evm.dmc.api.model;

public enum ProjectType {
	SIMPLEST_PROJECT("SIMPLEST_PROJECT");

	private final String name;

	private ProjectType(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
