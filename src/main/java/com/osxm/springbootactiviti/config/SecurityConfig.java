/**
 * @Title: SecurityConfig.java
 * @Package com.osxm.springactiviti.config
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月2日
 * @version V1.0
 */
package com.osxm.springbootactiviti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName: SecurityConfig
 * @Description: TODO
 * @author oscarchen
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("oscar")
				.password(new BCryptPasswordEncoder().encode("1")).roles("ADMIN");

	}
	
}
