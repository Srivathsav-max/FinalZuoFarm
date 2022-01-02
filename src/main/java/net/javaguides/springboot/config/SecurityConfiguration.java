package net.javaguides.springboot.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import net.javaguides.springboot.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				 .antMatchers("/**").permitAll()
				 .anyRequest().authenticated()
				 
		.and()
		.addFilterBefore(beforeAuthenticationFilter,BeforeAuthenticationFilter.class)
		.addFilterBefore(getCustomLoginFilter(), CustomLoginFilter.class)
		.formLogin()
		.loginPage("/login")
		.usernameParameter("email")
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)
		.permitAll()
		.and()
		.logout()
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login?logout")
		.permitAll();
	}
	private CustomLoginFilter getCustomLoginFilter() throws Exception {
		CustomLoginFilter filter = new CustomLoginFilter("/login","POST");
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
						if(!response.isCommitted()){
							response.sendRedirect("/login?error");
						}
				
			}
		});
		return filter;
	}
	@Bean (name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Autowired
	private BeforeAuthenticationFilter beforeAuthenticationFilter;
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailureHandler;

}
