package com.planco.plancoapi.repository.empresa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.planco.plancoapi.model.Empresa;
import com.planco.plancoapi.repository.filter.EmpresaFilter;

public class EmpresaRepositoryImpl implements EmpresaRepositoryQuery{

	@PersistenceContext
	private EntityManager manager; // para trabalhar com a consulta
	
	
	@Override
	public List<Empresa> filtrar(EmpresaFilter empresaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		
		CriteriaQuery<Empresa> criteria = builder.createQuery(Empresa.class);
		
		Root<Empresa> root = criteria.from(Empresa.class);
		
		Predicate[] predicates = criarRestricoes(empresaFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Empresa> typedQuery = manager.createQuery(criteria);
		
		return typedQuery.getResultList();
	}
	
	private Predicate[] criarRestricoes(EmpresaFilter empresaFilter, CriteriaBuilder builder,
			Root<Empresa> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (empresaFilter.getCodigo() != null) {
			predicates.add(builder.equal(root.get("codigo"), empresaFilter.getCodigo()));
		}
		
		if (!StringUtils.isEmpty(empresaFilter.getNome())) {
		predicates.add(builder.like(
				builder.lower(root.get("nome")), "%" + empresaFilter.getNome().toLowerCase() + "%"));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	
}
