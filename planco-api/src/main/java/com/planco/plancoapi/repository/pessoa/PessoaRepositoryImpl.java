package com.planco.plancoapi.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.planco.plancoapi.dto.LancamentoEstatisticaDia;
import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.model.Pessoa;
import com.planco.plancoapi.repository.filter.LancamentoFilter;
import com.planco.plancoapi.repository.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	
	
	@PersistenceContext
		private EntityManager manager; // para trabalhar com a consulta
		
	@Override
	public List<Pessoa> filtrar(PessoaFilter pessoaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
		
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		
		Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Pessoa> typedQuery = manager
				.createQuery(criteria);
		
	
		
		//page 3  
		
		return typedQuery.getResultList();
	}
	
	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder,
			Root<Pessoa> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (pessoaFilter.getCodigo() != null) {
			predicates.add(builder.equal(root.get("codigo"), pessoaFilter.getCodigo()));
		}
		
		if (!StringUtils.isEmpty(pessoaFilter.getNome())) {
		predicates.add(builder.like(
				builder.lower(root.get("nome")), "%" + pessoaFilter.getNome().toLowerCase() + "%"));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	

	
	
	

	
}
