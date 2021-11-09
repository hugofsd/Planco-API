package com.planco.plancoapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity //habilitando segurança
@EnableGlobalMethodSecurity(prePostEnabled = true) // habilitar a segurança nos metodos
public class ResourceServerConfig  extends WebSecurityConfigurerAdapter  {

	//Para este caso, podemos passar entre chaves o ID do Encoder que desejamos utilizar, como a senha não está criptografada, vamos utilizar o {noop}.
    //Caso nossa senha estivesse criptografa com BCrypt (por exemplo), poderíamos utilizar {bcrypt}

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	 // ler criptografia
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		  .antMatchers("/categorias").permitAll() // qualquer um pode acessar
		  .antMatchers("/lancamentos").permitAll() // qualquer um pode acessar
		  .antMatchers("/pessoas").permitAll() // qualquer um pode acessar
	//	 .anyRequest().authenticated() // o usuario precisa estar validado para fazer qualquer requisição
		  .and().httpBasic().
  and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		  .and().csrf().disable();


	}
	
}
