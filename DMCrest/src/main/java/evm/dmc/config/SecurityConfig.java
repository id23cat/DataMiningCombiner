package evm.dmc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import evm.dmc.api.model.account.Role;
import evm.dmc.model.service.AccountService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableScheduling	// We use scheduler for evicting EhCache tokens
@Profile({"devH2", "devMySQL", "test"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String[] PUBLIC_MATCHERS = {
			// Only for development period
			
	};
	
	private static final String[] H2_MATCHER = {
			"/h2-console/**"
	};
	
	private static final String[] PUBLIC_ACTUATOR = {
			"/management/**"
	};
	
	private static final String[] SWAGGER_MATCHERS = {
			"/webjars/**",
			"/*",
			"/v2/api-docs",
			"/rest/**",
			"/**"
	};
	
	@Autowired
	AccountService accountService;
	
	@Autowired
    Environment environment;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.eraseCredentials(true)
			.userDetailsService(accountService)
			.passwordEncoder(passwordEncoder())
		;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http//.csrf().disable()
			.authorizeRequests()
				.antMatchers(PUBLIC_MATCHERS).permitAll()
				.antMatchers(H2_MATCHER).permitAll()
				.antMatchers(PUBLIC_ACTUATOR).permitAll()
				.antMatchers(SWAGGER_MATCHERS).permitAll()
//				.antMatchers(ADMIN_MATCHERS).hasAuthority(Role.ADMIN.getName())
//				.antMatchers(USER_MATCHERS).hasAuthority(Role.USER.getName())
				.anyRequest().authenticated()
			.and()
			.httpBasic()
			;
		// # for H2 console frame
        http.csrf().disable();
        http.headers().frameOptions().disable();
	}
	
	
	
	@Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
}
