package com.planco.plancoapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.planco.plancoapi.model.Categoria;
import com.planco.plancoapi.repository.CategoriasRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired //procurar uma imprementação
	private CategoriasRepository categoriasRepository;
	
	@GetMapping
	public List<Categoria> listar(){
		return categoriasRepository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) //ao terminar, trazer status de criação
	public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriasRepository.save(categoria);
		
		// para indicar o location da categoria cadastrada
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(categoriaSalva.getCodigo()).toUri();
			response.setHeader("Location", uri.toASCIIString());
	
	
			return ResponseEntity.created(uri).body(categoriaSalva);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
	
		  Optional<Categoria> categoria = this.categoriasRepository.findById(codigo);
		    if (categoria.isPresent())  //isPresent, que nada mais é que uma comparação “obj != null
				return ResponseEntity.ok(categoria.get());
			else
				return ResponseEntity.notFound().build();
	
	}

}
