package evm.dmc.web.service.impls;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.web.controllers.SignInController;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.AccountService;


@Service
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountServiceImpl implements AccountService {
	private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${dmc.security.admin.username}")
	private String admuser;
	
	@Value("${dmc.security.admin.password}")
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
	
	@Override
	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		return account;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return createUser(getAccountByName(username));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Account getAccountByName(String username) throws UsernameNotFoundException {
		return  accountRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
	
	@Override
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	@Override
	@Transactional
	public void delete(Account account) {
		accountRepository.delete(account);
	}
	
	@Override
	public void refresh(Account account) {
		em.refresh(account);
	}
	
	@Override
	@Transactional
	public Account merge(Account account) {
		return em.merge(account);
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));
	}
	
	@Transactional
	private User createUser(Account account) {
		return new User(account.getUserName(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole().toString());
	}

}
