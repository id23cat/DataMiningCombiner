package evm.dmc.business.account;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.account.AccountExt;
import evm.dmc.api.model.account.Role;
import evm.dmc.web.SignInController;
import evm.dmc.web.exceptions.UserExistsException;


@Service
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${dmc.security.account.username}")
	private String admuser;
	
	@Value("${dmc.security.account.password}")
	private String admpass;
	
	private static final String ADMIN_EMAIL = "admin@admin";
	private static final String ADMIN_FIRSTNAME = "Admin";
	private static final String ADMIN_LASTNAME = "Admin";
	
	
	@PostConstruct
	protected void init() {
//		save(new Account("id23cat", "password", "id23cat@tut.by", "Alex", "Demidchuk"));
		logger.debug("Account service: create admin in DB");
		save(new AccountExt(admuser, admpass, ADMIN_EMAIL, ADMIN_FIRSTNAME, ADMIN_LASTNAME, Role.ADMIN));
	}
	
	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		return account;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUserName(username).get();
		return createUser(account);
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));
	}
	
	private User createUser(Account account) {
		return new User(account.getUserName(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole().toString());
	}

}
