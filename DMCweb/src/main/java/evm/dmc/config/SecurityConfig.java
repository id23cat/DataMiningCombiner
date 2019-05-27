package evm.dmc.config;

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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import evm.dmc.api.model.account.Role;
import evm.dmc.web.security.CustomAccessDeniedHandler;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.RequestPath;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Profile({"devH2", "devMySQL", "test"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] PUBLIC_MATCHERS = {
            "/css/**",
            "/images/**",
            "/webjars/**",
            "/js/**",
            "layouts/**",
            "fragments/**",
            RequestPath.root,
            RequestPath.home,
            RequestPath.signin,
            RequestPath.about,

            // Only for development period
            "/test",
            "/h2-console/**"
    };

    private static final String[] PUBLIC_ACTUATOR = {
            "/management/**"
    };

    private static final String[] ADMIN_MATCHERS = {
            RequestPath.adminHome,
            RequestPath.register
    };

    private static final String[] USER_MATCHERS = {
            RequestPath.userHome,
    };

    private static final String[] REST_MATCHERS = {
            "/webjars/**",
            "/*",
            "/v2/api-docs",
            "/rest/**",
            "/**"
    };

    @Autowired
    private AccountService accountService;

    @Autowired
    private Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
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
    protected void configure(HttpSecurity http) throws Exception {
        // http://www.baeldung.com/spring-security-session
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers(PUBLIC_ACTUATOR).permitAll()
                .antMatchers(REST_MATCHERS).permitAll()
                .antMatchers(ADMIN_MATCHERS).hasAuthority(Role.ADMIN.getName())
                .antMatchers(USER_MATCHERS).hasAuthority(Role.USER.getName())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(RequestPath.signin)
                .permitAll()
                .failureUrl(RequestPath.signin + "?error=1")
                .loginProcessingUrl(RequestPath.auth)
                .defaultSuccessUrl(RequestPath.project)
                .and()
                .logout()
                .logoutUrl(RequestPath.logout)
                .logoutSuccessUrl(RequestPath.home)
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .rememberMe()
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
