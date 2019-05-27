package evm.dmc.api.model.algorithm;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.converters.MapAttributesToJson;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"srcAttributes"})
@Entity
//@EntityListeners(AlgorithmEntityListener.class)
@Table(name = "ALGORITHM"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "name"})}
)
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 386821974686652567L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Length(max = 100)
    private String name;

    @Length(max = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @NotNull
    private ProjectModel project;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, optional = true)
    private MetaData dataSource;

    @Column(length = 100000)
    @Convert(converter = MapAttributesToJson.class)
    @Singular
    private Map<String, DataAttribute> srcAttributes;

    @Column(nullable = true)
    private MetaData dataDestination;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "method_id")
    private PatternMethod method;

    public Map<String, DataAttribute> getSrcAttributes() {
        if (dataSource == null)
            return null;
        return srcAttributes.isEmpty() ? dataSource.getAttributes() : srcAttributes;
    }

    public MetaData getDataSource() {
        if (dataSource == null)
            return null;
        MetaData meta = dataSource.toBuilder().build();
        if (!srcAttributes.isEmpty()) {
            meta.setAttributes(srcAttributes);
        }
        return meta;
    }
}
