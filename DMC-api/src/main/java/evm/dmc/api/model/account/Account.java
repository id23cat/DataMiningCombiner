package evm.dmc.api.model.account;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import evm.dmc.api.model.ProjectModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Table(name="ACCOUNT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
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
	protected Role role = Role.USER;
	
	@Setter(AccessLevel.NONE) 
	private Instant created;
	
	@OneToMany(mappedBy="account", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProjectModel> projects = new HashSet<>();

    public Account() {
    	this.created = Instant.now();
    }

	public Account(String username, String password, String email, 
			String firstName, String lastName) {
		super();
		this.email = email;
		this.userName = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.created = Instant.now();
	}
	
	public Account(Account acc) {
//		this.id = acc.id;
		this.userName = acc.userName;
		this.created = acc.created;
		this.email = acc.email;
		this.firstName = acc.firstName;
		this.lastName = acc.lastName;
		this.password = acc.password;
		this.role = acc.role;
	}
	
	public Account addProject(ProjectModel project) {
		projects.add(project);
		project.setAccount(this);
		return this;
	}
	
	public Account removeProject(ProjectModel project) {
		projects.remove(project);
		project.setAccount(null);
		return this;
	}

}
