package com.planco.plancoapi.repository.categoria;

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

import com.planco.plancoapi.model.Categoria;
import com.planco.plancoapi.model.Pessoa;
import com.planco.plancoapi.repository.filter.CategoriaFilter;
import com.planco.plancoapi.repository.filter.PessoaFilter;

public class CategoriasRepositoryImpl implements CategoriasRepositoryQuery {

	
	@PersistenceContext
	private EntityManager manager; // para trabalhar com a consulta
	
	@Override
	public List<Categoria> filtrar(CategoriaFilter categoriaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
	
		CriteriaQuery<Categoria> criteria = builder.createQuery(Categoria.class);
		
		Root<Categoria> root = criteria.from(Categoria.class);
		
		Predicate[] predicates = criarRestricoes(categoriaFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Categoria> typedQuery = manager
				.createQuery(criteria);
		
		
		return typedQuery.getResultList();
	}
	
	
	private Predicate[] criarRestricoes(CategoriaFilter categoriaFilter, CriteriaBuilder builder,
			Root<Categoria> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (categoriaFilter.getCodigo() != null) {
			predicates.add(builder.equal(root.get("codigo"), categoriaFilter.getCodigo()));
		}
		
		if (!StringUtils.isEmpty(categoriaFilter.getName())) {
		predicates.add(builder.like(
				builder.lower(root.get("name")), "%" + categoriaFilter.getName().toLowerCase() + "%"));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
