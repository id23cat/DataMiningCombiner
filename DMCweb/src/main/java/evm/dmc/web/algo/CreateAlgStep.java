package evm.dmc.web.algo;

public enum CreateAlgStep {
	DATASOURCE("datasource"),
	FUNCTION("function"),
	DATADEST("datadest"),
	FUNCTION_KMENS("function_kmeans"),
	FUNCTION_PCA("function_pca"),
	FUNCTION_DATPREV("function_datapreview"),
	FUNCTION_DATPREV2("function_datapreview2");
	
	
	private final String name;
	
	public static CreateAlgStep forName(final String name) {
		 if (name == null) {
	            throw new IllegalArgumentException("Name cannot be null for step");
	        }
		 switch(name){
		 	case "datasource":
		 		return DATASOURCE;
		 	case "function":
		 		return FUNCTION;
		 	case "datadest":
		 		return DATADEST;
		 	case "function_kmeans":
		 		return FUNCTION_KMENS;
		 	case "function_pca":
		 		return FUNCTION_PCA;
		 	case "function_datapreview":
		 		return FUNCTION_DATPREV;
		 	case "function_datapreview2":
		 		return FUNCTION_DATPREV2;
		 	default:
		 		throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any CreateAlgStep");
		 }
		 		
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
