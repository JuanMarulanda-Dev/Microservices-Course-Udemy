package com.formacionbdi.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered{

	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);

	// exchange -> Podemos ingresar al request y al response
	// chain -> La cadena de filtros se ejecutan los demás filtros
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		// Todo lo que este acá va a ser el PRE (Antes que se envie al micro)
		logger.info("ejecutando filtro pre");
		exchange.getRequest().mutate().headers(h -> h.add("token", "123456"));
		
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			// Todo lo que este acá va a ser el POST (Depues de que se envia al micro)
			logger.info("ejecutando filtro post");
			
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
				exchange.getResponse().getHeaders().add("token", valor); // Se agrega a los headers de la respuesta
			});
			
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			// exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	@Override
	public int getOrder() {
		// Con esto le damos el orden de ejecución de cada uno de los filtros
		// TODO Auto-generated method stub
		return 1;
	}

}
