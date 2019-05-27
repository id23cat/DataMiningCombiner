package evm.dmc.api.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;

import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PROJECT"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"account_id", "name"})}
)
@EqualsAndHashCode(exclude = {"algorithms", "dataSources"})
@ToString(exclude = {"algorithms", "dataSources"})
public class ProjectModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5045386144743151365L;

    @Transient
    private static final String NOT_BLANK_MESSAGE = "{error.emptyField}";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)    //(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Builder.Default
    private ProjectType projectType = ProjectType.SIMPLEST_PROJECT;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)

    @Builder.Default
    private Set<Algorithm> algorithms = new HashSet<>();

    @NotBlank(message = ProjectModel.NOT_BLANK_MESSAGE)
    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    @Setter(AccessLevel.NONE)
    private java.sql.Timestamp created;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Singular
    private Set<MetaData> dataSources;

    public synchronized void addMetaData(MetaData meta) {
        meta.setProject(this);
        this.dataSources.add(meta);
    }

}
