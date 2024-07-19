package com.formacionbdi.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
// import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

// Filtro aplicable a una ruta especifica
// AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> hace referencia a los parametros que va a recibir este filtro personalizado que estamos creando
@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion>{

	private final Logger logger = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);
	
	// Ejemplo es el nombre del filtro y GatewayFilterFactory es un sufijo que se usa para que se identifique que es un filto
	// Estos filtros personalizados se pueden agregar especificamente a cada ruta.

	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
	}

	@Override
	public GatewayFilter apply(Configuracion config) {
		return (exchange, chain) -> {
			// Todo lo que este acá va a ser el PRE (Antes que se envie al micro)
			logger.info("ejecutando pre gateway filter factory: " + config.mensaje);

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// Todo lo que este acá va a ser el POST (Depues de que se envia al micro)
				Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				
				logger.info("ejecutando post gateway filter factory: " + config.mensaje);
				
			}));
		};
	}
	

	// De esta forma le podemos sobreescribir el nombre al filtro y cuando se llamae desde el archivo de propiedades se debe llamar con el nombre que se retorne acá
	@Override
	public String name() {
		return "EjemploCookie";
	}

	// Este va a ser el orden en que se van a recibir los parametros de los filtros cuando se llame desde el archivo de propiedades
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje", "cookieNombre", "cookieValor");
	}

	public static class Configuracion {
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
	}

}
