package com.userfront.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.userfront.Service.UserServiceImpl.UserSecurityService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private UserSecurityService userSecurityService;

	/*
	 * Use to remove the implicit operation of saving password * (password strength)
	 */
	private static final String SALT = "salt"; // Salt should be protected carefully

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http

				/* any request matches to above matchers */
				.authorizeRequests().
				// antMatchers("/**").
				antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

		http

				.csrf().disable().cors().disable().formLogin().failureUrl("/index?error")
				.defaultSuccessUrl("/userFront").loginPage("/index").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout")
				.deleteCookies("remember-me").permitAll().and().rememberMe();

		DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
		http.authenticationProvider(daoAuthenticationProvider());
		return defaultSecurityFilterChain;

	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}

	/* Use to show public without spring security */
	private static final String[] PUBLIC_MATCHERS = { "/webjars/**", "/css/**", "/js/**", "/images/**", "/",
			"/about/**", "/contact/**", "/error/**/*", "/console/**", "/signup" };

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http
	//
	// /* any request matches to above matchers */
	// .authorizeRequests().
	//// antMatchers("/**").
	// antMatchers(PUBLIC_MATCHERS).
	// permitAll().anyRequest().authenticated();
	//
	// http
	//
	// .csrf().disable().cors().disable()
	// .formLogin().failureUrl("/index?error").defaultSuccessUrl("/userFront").loginPage("/index").permitAll()
	// .and()
	// .logout().logoutRequestMatcher(new
	// AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()
	// .and()
	// .rememberMe();
	// }

	//
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// //
	// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
	// //This is in-memory authentication
	// auth.
	// }
	//
	@Bean
	protected AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration)
			throws Exception {
		return configuration.getAuthenticationManager();

	}

	@Bean
	protected DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userSecurityService);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;

	}

}