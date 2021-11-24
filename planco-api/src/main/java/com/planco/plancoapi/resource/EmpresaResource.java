package com.planco.plancoapi.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

import com.planco.plancoapi.event.RecursoCriadoEvent;
import com.planco.plancoapi.model.Empresa;
import com.planco.plancoapi.model.Pessoa;
import com.planco.plancoapi.repository.EmpresaRepository;
import com.planco.plancoapi.repository.filter.EmpresaFilter;
import com.planco.plancoapi.service.EmpresaService;


@RestController
@RequestMapping("/empresas")
public class EmpresaResource {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Empresa> listar(EmpresaFilter empresaFilter){
		return empresaRepository.filtrar(empresaFilter);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity <Empresa> criar (@RequestBody Empresa empresa,  HttpServletResponse response){
		Empresa empresaSalva = empresaRepository.save(empresa);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, empresaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
	}
	
	
	@GetMapping("/{codigo}")
	public ResponseEntity <Empresa> buscareEmpresaPeloCodigo(@PathVariable Long codigo) {
		
      Optional<Empresa> empresa =this.empresaRepository.findById(codigo);
      if(empresa.isPresent())
    	  return ResponseEntity.ok(empresa.get());
      else
    	  return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo ) {
		this.empresaRepository.deleteById(codigo);
	}
	
	
	@PutMapping("/{codigo}") //ATAULIZAÇÃO COMPLETA
	public Empresa atualizar(@PathVariable Long codigo, @RequestBody Empresa empresa	) {

		Empresa empresaSalva = empresaService.atualizar(codigo, empresa);
		
				//salva no bd
			  return this.empresaRepository.save(empresaSalva);
    }
	
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)// precisa retornar nd quando atualizar
	public void atualizarativo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		
		empresaService.atualizarPropriedadeAtivo (codigo, ativo);
	}
	
}
