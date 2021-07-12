package com.planco.plancoapi.repository.lancamento;

import java.util.List;

import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
