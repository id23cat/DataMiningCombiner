package evm.dmc.api.model.account;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
//@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ACCOUNT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(exclude={"projects"})
@ToString(exclude="projects")
public class Account implements Serializable {
	@Transient
	private static final String NOT_BLANK_MESSAGE = "{error.emptyField}";
	@Transient
	private static final String EMAIL_MESSAGE = "{error.email}";
	@Transient
	private static final String USERNAME_SIZE_MESSAGE = "{error.username.size}";

	private static final long serialVersionUID = 4198630702609693622L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Column(unique = true, nullable = false)
	@Size(min = 4, max = 16, message = USERNAME_SIZE_MESSAGE)
	private String userName;

	@JsonIgnore
	@NotBlank(message = Account.NOT_BLANK_MESSAGE)
	@Size(min = 5, max = 16)
	private String password;

	@Column(unique = true, nullable = false)
	@NotBlank(message = Account.NOT_BLANK_MESSAGE)
	@Email(message = Account.EMAIL_MESSAGE)
	private String email;

	private String firstName;

	private String lastName;
	
	@Enumerated(EnumType.STRING)
	@Setter(AccessLevel.PROTECTED)
	@NotNull
	@Builder.Default
	protected Role role = Role.USER;
	
	@CreationTimestamp
	@Column(insertable = true, updatable = false)
	@Setter(AccessLevel.NONE)
//	@Builder.Default
//	private Instant created = Instant.now();
	private java.sql.Timestamp created;
	
	@OneToMany(mappedBy="account", fetch = FetchType.LAZY,
			orphanRemoval = true, cascade = CascadeType.ALL) //{CascadeType.REMOVE, CascadeType.PERSIST},
	@Singular
	private Set<ProjectModel> projects;

	public Account(String username, String password, String email, 
			String firstName, String lastName) {
		super();
		this.email = email;
		this.userName = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
//		this.created = Instant.now();
	}
	
	public Account(String username, String password, String email, 
			String firstName, String lastName, Role role) {
		super();
		this.email = email;
		this.userName = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
//		this.created = Instant.now();
	}
	

}
