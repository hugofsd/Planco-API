package com.planco.plancoapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity //habilitando segurança
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	//Para este caso, podemos passar entre chaves o ID do Encoder que desejamos utilizar, como a senha não está criptografada, vamos utilizar o {noop}.
    //Caso nossa senha estivesse criptografa com BCrypt (por exemplo), poderíamos utilizar {bcrypt}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		//user, senha, roles(permissão)
		 .withUser("admin").password("{noop}admin").roles("ROLE");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		  .antMatchers("/categorias").permitAll() // qualquer um pode acessar
		  .anyRequest().authenticated() // o usuario precisa estar validado para fazer qualquer requisição
		  .and()
		.httpBasic().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.csrf().disable();
		
		
	}
	
}
