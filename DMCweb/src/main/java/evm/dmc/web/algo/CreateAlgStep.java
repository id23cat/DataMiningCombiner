package evm.dmc.web.algo;

public enum CreateAlgStep {
	DATASOURCE("datasource"),
	FUNCTION("function"),
	DATADEST("datadestinaton"),
	VIEWDATAPART("viewdatapart"),
	VIEWCHART("viewchart");
	
	
	private final String name;
	
	public static CreateAlgStep forName(final String name) {
		 if (name == null) {
	            throw new IllegalArgumentException("Name cannot be null for step");
	        }
	        if (name.toLowerCase().equals("datasource")) {
	            return DATASOURCE;
	        } else if (name.toLowerCase().equals("function")) {
	            return FUNCTION;
	        } else if (name.toLowerCase().equals("datadestinaton")) {
	            return DATADEST;
	        } else if (name.toLowerCase().equals("viewdatapart")) {
	            return VIEWDATAPART;
	        } else if (name.toLowerCase().equals("viewchart")) {
	            return VIEWCHART;
	        }
	        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any CreateAlgStep");
	}

	private CreateAlgStep(final String name) {
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
