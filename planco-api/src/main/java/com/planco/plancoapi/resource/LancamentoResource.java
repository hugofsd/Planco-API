package com.planco.plancoapi.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.repository.LancamentoRepository;
import com.planco.plancoapi.repository.filter.LancamentoFilter;
import com.planco.plancoapi.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter){
		return lancamentoRepository.filtrar(lancamentoFilter);
	}
	
	@GetMapping	("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo (@PathVariable Long codigo){
		
		Optional<Lancamento> lancamento = this.lancamentoRepository.findById(codigo);
		if(lancamento.isPresent()) //isPresent é um "obj != null"
			return ResponseEntity.ok(lancamento.get());
		else
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  ResponseEntity<Lancamento> criar(@RequestBody Lancamento lancamento){
		
		//colocar uma regra no front de n salvar pessoa inativa
		Lancamento lancamentoSalva = lancamentoRepository.save(lancamento);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalva);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		this.lancamentoRepository.deleteById(codigo);
	}
	
	

}
