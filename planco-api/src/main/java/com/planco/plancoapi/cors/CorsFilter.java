package com.planco.plancoapi.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component //componente do spring 
@Order(Ordered.HIGHEST_PRECEDENCE) // ordem de alta prioridade
public class CorsFilter implements Filter {
	
	// Configuravel
	private String originPermitida = "http://localhost:8000"; 

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		//convertendo 
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse  response = (HttpServletResponse) resp;
		
		if ("OPTIONS".equals(request.getMethod()) && originPermitida.equals(request.getHeader("Origin"))) {
		   //setar metodos http
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			
			response.setHeader("Access-Control-Max-Age", "3600");
			// resposta de ok
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}
	}
	
	@Override
	public void destroy() {
	}


}
