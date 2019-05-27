package evm.dmc.api.model;

import evm.dmc.core.api.back.data.DataSrcDstType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Builder(builderMethodName = "srcBuilder")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class FunctionSrcModel extends FunctionModel {

    private static final long serialVersionUID = -2433829450685223249L;

    public final static String SRC_PROPERTY_NAME = "source";

    private String description;

    private String source = null;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Builder.Default
    private DataSrcDstType typeSrcDst = DataSrcDstType.LOCAL_FS;

    public FunctionSrcModel(FunctionModel funmodel) {
        super(funmodel.getId(),
                funmodel.getName(),
                funmodel.getFramework(),
                funmodel.getType(),
                funmodel.getProperties(),
                funmodel.getDescription());
        if (funmodel instanceof FunctionSrcModel) {
            this.source = ((FunctionSrcModel) funmodel).source;
            this.typeSrcDst = ((FunctionSrcModel) funmodel).typeSrcDst;
        }
    }
}
