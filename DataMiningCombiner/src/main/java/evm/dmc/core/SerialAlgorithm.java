package evm.dmc.core;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.DataSrcDstModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.FrameworksRepository;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;
import evm.dmc.core.api.exceptions.StoreDataException;

/**
 * The Class SerialAlgorithm. Serial Implementation of interface, means serial
 * execution of contained commands
 */
public class SerialAlgorithm implements Algorithm {
	private static final String NAME = "serialAlgorithm";
	private AlgorithmModel model = new AlgorithmModel();
	
	private DMCDataLoader dataSource = null;
	private List<DMCFunction<?>> algChain = new LinkedList<>();
	private DMCDataSaver dataDestination = null;
	
	private FrameworksRepository repository = null;
	private boolean modifyModel = true;

	public SerialAlgorithm() {
		model.setName(NAME);
	};

	public SerialAlgorithm(FrameworksRepository repo) {
		setFrameworksRepository(repo);
		model.setName(NAME);
	};

	public SerialAlgorithm(FrameworksRepository repo, AlgorithmModel model) {
		setFrameworksRepository(repo);
		setModel(model);
	}

	@Override
	public Algorithm setFrameworksRepository(FrameworksRepository repo) {
		this.repository = repo;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Algorithm#addCommand(evm.dmc.core.Command)
	 */
	@Override
	public void addCommand(DMCFunction<?> dMCFunction) {
		algChain.add(dMCFunction);
		if (modifyModel)
			model.addFunction(dMCFunction.getFunctionModel());

	}

	@Override
	public void addCommand(String descriptor) {
		addCommand(repository.getFunction(descriptor));

	}

	@Override
	public void addCommand(FunctionModel functionModel) {
		addCommand(functionModel.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Algorithm#delCommand(evm.dmc.core.Command)
	 */
	@Override
	public boolean delCommand(DMCFunction<?> dMCFunction) {
		if (dMCFunction == null)
			return false;
		return modifyModel ? algChain.remove(dMCFunction) && model.getFunctions().remove(dMCFunction.getFunctionModel())
				: algChain.remove(dMCFunction);
	}

	@Override
	public boolean delCommand(String descriptor) {
		return delCommand(algChain.stream().filter(function -> function.getName() == descriptor).findFirst().get());

	}

	@Override
	public boolean delCommand(FunctionModel functionModel) {
		return delCommand(functionModel.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Algorithm#execute()
	 */
	@Override
	public void execute() throws IOException {
		Data data = null;
		data = dataSource.get();
		for(DMCFunction<?> function : algChain){
//			if(data != null)
			function.setArgs(data);
			function.execute();
			data = function.getResult();
		}
		if(dataDestination != null)
			dataDestination.save(data);

	}

	@Override
	public void insertCommand(DMCFunction<?> dMCFunction, DMCFunction<?> after) throws NoSuchFunctionException {
		int index = algChain.indexOf(after);
		if (index == -1)
			throw new NoSuchFunctionException("Function " + after.getName() + "not present in algorithm");
		algChain.add(index + 1, dMCFunction);
		if (modifyModel)
			model.getFunctions().add(index + 1, dMCFunction.getFunctionModel());

	}

	@Override
	public void insertCommand(String descriptor, DMCFunction<?> after) {
		insertCommand(repository.getFunction(descriptor), after);

	}

	@Override
	public void insertCommand(FunctionModel functionModel, DMCFunction<?> after) {
		insertCommand(functionModel.getName(), after);

	}

	@Override
	public Algorithm setModel(AlgorithmModel model) {
		this.model = model;
		model.setName(NAME);
		modifyModel = false; // deprecate modifications in model in function
								// addCoomand()

		algChain = new LinkedList<>();
		addDataSource(model.getDataSource());
		for (FunctionModel funcModel : model.getFunctions())
			addCommand(funcModel.getName());

		if (model.getDataDestination() != null)
			addDataDestination(model.getDataDestination());

		modifyModel = true;	// back to default behavior
		return this;
	}

	@Override
	public AlgorithmModel getModel() {

		return model;
	}

	@Override
	public void addDataSource(DMCDataLoader dataSource) {
		this.dataSource = dataSource;
		if (modifyModel){
//			String src = dataSource.getSrcModel().getSource();
//			model.getDataSource().setSource(src);
			model.setDataSource(dataSource.getSrcModel());
		}
		
	}

	@Override
	public void addDataSource(String descriptor, String source) {
		DMCDataLoader dataLoader = (DMCDataLoader)repository.getFunction(descriptor);
		dataLoader.setSource(source);
		addDataSource(dataLoader);
	}
	
	@Override
	public void addDataSource(FunctionModel functionModel) {		
		DMCDataLoader dataLoader = (DMCDataLoader)repository.getFunction(functionModel.getName());
		if(functionModel instanceof FunctionSrcModel)
			dataLoader.setSrcModel((FunctionSrcModel)functionModel);
		else
			dataLoader.setSrcModel(new FunctionSrcModel(functionModel));
		addDataSource(dataLoader);
		
	}

	@Override
	public void addDataDestination(DMCDataSaver dataDest) {
		this.dataDestination = dataDest;
		if (modifyModel){
//			model.getDataDestination().setDestination(dataDest.getDstModel().getDestination());
			model.setDataDestination(dataDest.getDstModel());
		}
		
	}
	
	@Override
	public void addDataDestination(String descriptor, String destination) {
//		addDataDestination((DMCDataSaver)repository.getFunction(descriptor));
		DMCDataSaver dataSaver = (DMCDataSaver)repository.getFunction(descriptor);
		dataSaver.setDestination(destination);
		addDataDestination(dataSaver);
	}

	@Override
	public void addDataDestination(FunctionModel functionModel) {
//		addDataDestination(functionModel.getName(), null);
		DMCDataSaver dataSaver = (DMCDataSaver)repository.getFunction(functionModel.getName());
		if(functionModel instanceof FunctionDstModel)
			dataSaver.setDstModel((FunctionDstModel) functionModel);
		else
			dataSaver.setDstModel(new FunctionDstModel(functionModel));
		addDataDestination(dataSaver);
	}

	@Override
	public List<DMCFunction<?>> getFunctionsList() {
		return Collections.unmodifiableList(algChain);
	}

}
