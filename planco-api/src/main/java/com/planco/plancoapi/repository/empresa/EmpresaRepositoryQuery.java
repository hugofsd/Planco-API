package com.planco.plancoapi.repository.empresa;

import java.util.List;

import com.planco.plancoapi.model.Empresa;
import com.planco.plancoapi.repository.filter.EmpresaFilter;

public interface EmpresaRepositoryQuery {

	public List<Empresa> filtrar(EmpresaFilter empresaFilter );
	
}
