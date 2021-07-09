package com.planco.plancoapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.planco.plancoapi.event.RecursoCriadoEvent;
import com.planco.plancoapi.model.Usuario;

import com.planco.plancoapi.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Usuario> listar(){
		return usuarioRepository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity <Usuario> criar (@RequestBody Usuario usuario,  HttpServletResponse response){
		Usuario usuarioSalva = usuarioRepository.save(usuario);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalva);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity <Usuario> buscarPeloCodigo(@PathVariable Long codigo) {
		
      Optional<Usuario> usuario =this.usuarioRepository.findById(codigo);
      if(usuario.isPresent())
    	  return ResponseEntity.ok(usuario.get());
      else
    	  return ResponseEntity.notFound().build();
	}
}
