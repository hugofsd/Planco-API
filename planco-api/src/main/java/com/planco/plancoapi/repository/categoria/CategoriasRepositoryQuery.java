package com.planco.plancoapi.repository.categoria;

import java.util.List;

import com.planco.plancoapi.model.Categoria;
import com.planco.plancoapi.repository.filter.CategoriaFilter;

public interface CategoriasRepositoryQuery {

	public List<Categoria> filtrar(CategoriaFilter categoriaFilter );
	
}
