package com.planco.plancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planco.plancoapi.model.Pessoa;


public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
