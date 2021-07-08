package com.planco.plancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planco.plancoapi.model.Categoria;

// JpaRepository : interface
public interface CategoriasRepository extends JpaRepository<Categoria, Long>{

}
