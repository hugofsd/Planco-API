package com.planco.plancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planco.plancoapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
