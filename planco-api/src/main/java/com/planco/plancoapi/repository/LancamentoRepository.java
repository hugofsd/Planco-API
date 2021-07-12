package com.planco.plancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planco.plancoapi.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
