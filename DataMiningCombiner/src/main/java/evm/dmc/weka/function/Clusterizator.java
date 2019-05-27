package evm.dmc.weka.function;

import evm.dmc.core.api.Data;

public interface Clusterizator {

    Clusterizator train(Data trainSet);
}
