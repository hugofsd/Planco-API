package com.planco.plancoapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.planco.plancoapi.model.Empresa;
import com.planco.plancoapi.repository.EmpresaRepository;

@Service
public class EmpresaService {

	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public Empresa atualizar(Long codigo, Empresa empresa) {
		Empresa empresaSalva = buscareEmpresaPeloCodigo(codigo);
			// Tirando o codigo
			  BeanUtils.copyProperties(empresa, empresaSalva, "codigo");
		
			return empresaRepository.save(empresaSalva); 
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Empresa empresaSalva = buscareEmpresaPeloCodigo(codigo);
		empresaSalva.setAtivo(ativo);
		empresaRepository.save(empresaSalva);
	}
	
	//Metodo refatorado : Refector > Extratec Metodh
	public Empresa buscareEmpresaPeloCodigo(Long codigo) {
		Empresa empresaSalva = this.empresaRepository.findById(codigo)
			      .orElseThrow(() -> new EmptyResultDataAccessException(1));

		  if (empresaSalva.getNome() == null) {
			  throw new EmptyResultDataAccessException(1);
			}
		return empresaSalva;
	}
	
	
}
