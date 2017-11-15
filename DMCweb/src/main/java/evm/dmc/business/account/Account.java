package evm.dmc.business.account;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table(name="ACCOUNT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Account implements Serializable {
	@Transient
	public static final String NOT_BLANK_MESSAGE = "{error.emptyField}";
	@Transient
	public static final String EMAIL_MESSAGE = "{error.email}";
	
	private static final long serialVersionUID = 4198630702609693622L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@NotBlank(message = Account.NOT_BLANK_MESSAGE)
//	@Column(unique  = true, nullable = false)
	private String userName;
	
	@JsonIgnore
	@NotBlank(message = Account.NOT_BLANK_MESSAGE)
	private String password;
	
//	@Column(unique = true, nullable = false)
//	@NotBlank(message = Account.NOT_BLANK_MESSAGE)
//	@Email(message = Account.EMAIL_MESSAGE)
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	protected String role = "ROLE_USER";
	
	private Instant created;

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
//		this.role = role;
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the username
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param username the username to set
	 */
	public void setUserName(String username) {
		this.userName = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the created
	 */
	public Instant getCreated() {
		return created;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Account)) {
			return false;
		}
		Account castOther = (Account) other;
		return new EqualsBuilder().append(id, castOther.id).append(userName, castOther.userName)
				.append(password, castOther.password).append(email, castOther.email)
				.append(firstName, castOther.firstName).append(lastName, castOther.lastName)
				.append(role, castOther.role).append(created, castOther.created).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(userName).append(password).append(email).append(firstName)
				.append(lastName).append(role).append(created).toHashCode();
	}
    
	
    
}
