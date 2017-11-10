package evm.dmc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import evm.dmc.web.RegisterSignInController;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Profile("devel")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(RegisterSignInController.class);
	private static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/images/**",
			"/webjars/**",
			"/js/**",
			"/webjars/**",
			"layouts/**",
			"fragments/**",
			"/",
			"/home"
	};
	
	@Autowired
    Environment environment;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		logger.debug("Active profile: {}", (Object[])environment.getActiveProfiles());
		// http://www.baeldung.com/spring-security-session
//		http
//			.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.NEVER);
		http.csrf().disable()
			.authorizeRequests()
				.antMatchers(PUBLIC_MATCHERS).permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.eraseCredentials(true)
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER")
				.and()
				.withUser("admin").password("password").roles("USER", "ADMIN");
	}
	
	@Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests()
//				.antMatchers(RequestPath.HOME,
//							RequestPath.ROOT,
////							RequestPath.ABOUT,
//							RequestPath.REGISTER,
//							"/favicon.ico")
//				.permitAll()
//				.anyRequest().authenticated()
//			.and()
//			.formLogin()
//				.loginPage(RequestPath.SIGNIN)
//				.permitAll()
////				.failureUrl("/signin?error=1")
////				.loginProcessingUrl("/authenticate")
//            .and()
//            .logout()
////                .logoutUrl("/logout")
//                .permitAll();
////                .logoutSuccessUrl("/signin?logout");
////                .and()
////            .rememberMe()
////                .rememberMeServices(rememberMeServices())
////                .key("remember-me-key");
//	}
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.
//			inMemoryAuthentication()
//				.withUser("user").password("password").roles("USER")
//				.and()
//				.withUser("admin").password("password").roles("USER", "ADMIN");
//	}
//	
//	@Bean(name = "authenticationManager")
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
}
