package com.planco.plancoapi.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.planco.plancoapi.dto.LancamentoEstatisticaCategoria;
import com.planco.plancoapi.dto.LancamentoEstatisticaDia;
import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	

	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
}
