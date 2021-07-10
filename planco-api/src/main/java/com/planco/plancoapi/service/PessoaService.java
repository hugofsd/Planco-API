package com.planco.plancoapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.planco.plancoapi.model.Pessoa;
import com.planco.plancoapi.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(codigo)
			      .orElseThrow(() -> new EmptyResultDataAccessException(1));

		  if (pessoaSalva.getNome() == null) {
			  throw new EmptyResultDataAccessException(1);
			}
		  
			// Tirando o codigo
			  BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
			  
			
			return pessoaRepository.save(pessoaSalva); 
	}
	
}
