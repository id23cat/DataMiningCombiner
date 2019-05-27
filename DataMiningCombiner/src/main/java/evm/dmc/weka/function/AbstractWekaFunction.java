package evm.dmc.weka.function;

import evm.dmc.core.api.Data;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.weka.WekaFunction;
import evm.dmc.weka.data.WekaData;
import org.springframework.stereotype.Service;
import weka.core.Instances;

import java.util.Optional;

@Service
public abstract class AbstractWekaFunction extends AbstractDMCFunction<Instances> implements WekaFunction {

    protected Data<Instances> result = null;

    @Override
    public Data<Instances> getResult() {
        return result;
    }

    @Override
    public Optional<Data<Instances>> getOptionalResult() {
        return Optional.ofNullable(result);
    }

    protected void setResult(WekaData res) {
        result = res;
    }

}
