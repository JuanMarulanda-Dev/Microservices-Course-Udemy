package com.formacionbdi.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService usuarioService;
	
	@Autowired
	private AuthenticationEventPublisher eventPublisher;

	// Especificamos como va a funcionar el encriptamiento de las contraseñas
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Acá es donde vamos a especificar como va a ser la autenticación de mi aplicación
	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService) // Esta es el servicio que vamos a utilziar para cargar la informacion del usuario en los claims del token
				.passwordEncoder(passwordEncoder()) // Acá se referencia el encriptador de contraseñas que vamos a usar para hacen encrypt y decrypt
				.and()
				.authenticationEventPublisher(eventPublisher); // Acá vamos a escuchar los eventos de login talse como: de fallo, de login, etc
	}

	// Estamos convirtiendo el AutenticationManager en un bean que va a poder ser inyectado en otras clases
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

}
