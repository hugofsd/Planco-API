package com.planco.plancoapi.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.model.Pessoa;
import com.planco.plancoapi.repository.LancamentoRepository;
import com.planco.plancoapi.repository.PessoaRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	/* 
	 * 
	 public Lancamento salvar(Lancamento lancamento) {
		
		//verificar se a pessoa existe
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());

        if(pessoa == null || pessoa.isPresent()) {
        	 throw new EmptyResultDataAccessException(); //erro
        }
        return lancamentoRepository.save(lancamento);
		
	}
	 * 
	 * */
	
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalva = buscarLancamentoCodigo(codigo);
			// Tirando o codigo
			  BeanUtils.copyProperties(lancamento, lancamentoSalva, "codigo");
		
			return lancamentoRepository.save(lancamentoSalva); 
	}
	
	
	//Metodo refatorado : Refector > Extratec Metodh
		public Lancamento buscarLancamentoCodigo(Long codigo) {
			Lancamento lancamentoSalva = this.lancamentoRepository.findById(codigo)
				      .orElseThrow(() -> new EmptyResultDataAccessException(1));

			  if (lancamentoSalva.getDescricao() == null) {
				  throw new EmptyResultDataAccessException(1);
				}
			return lancamentoSalva;
		}

	

}
