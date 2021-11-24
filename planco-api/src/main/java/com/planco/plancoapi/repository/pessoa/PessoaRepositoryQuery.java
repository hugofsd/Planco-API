package com.planco.plancoapi.repository.pessoa;

import com.planco.plancoapi.repository.filter.PessoaFilter;

import java.util.List;

import com.planco.plancoapi.model.Pessoa;
public interface PessoaRepositoryQuery {

	public List<Pessoa> filtrar(PessoaFilter pessoaFilter);

}
