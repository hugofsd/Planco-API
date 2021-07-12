package com.planco.plancoapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.planco.plancoapi.event.RecursoCriadoEvent;
import com.planco.plancoapi.model.Pessoa;

import com.planco.plancoapi.repository.PessoaRepository;
import com.planco.plancoapi.service.PessoaService;


@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Pessoa> listar(){
		return pessoaRepository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity <Pessoa> criar (@RequestBody Pessoa pessoa,  HttpServletResponse response){
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity <Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
		
      Optional<Pessoa> pessoa =this.pessoaRepository.findById(codigo);
      if(pessoa.isPresent())
    	  return ResponseEntity.ok(pessoa.get());
      else
    	  return ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo ) {
		this.pessoaRepository.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}") //ATAULIZAÇÃO COMPLETA
	public Pessoa atualizar(@PathVariable Long codigo, @RequestBody Pessoa pessoa	) {

		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
		
				//salva no bd
			  return this.pessoaRepository.save(pessoaSalva);
    }
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)// precisa retornar nd quando atualizar
	public void atualizarativo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		
		pessoaService.atualizarPropriedadeAtivo (codigo, ativo);
	}
	
	
	
	
}
