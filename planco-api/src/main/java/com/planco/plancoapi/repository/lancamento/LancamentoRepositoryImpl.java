package com.planco.plancoapi.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.planco.plancoapi.dto.LancamentoEstatisticaCategoria;
import com.planco.plancoapi.dto.LancamentoEstatisticaDia;
import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.repository.filter.LancamentoFilter;

// implementa o lançamento personalizado
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager; // para trabalhar com a consulta
	
	
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaDia.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDia.class, 
				root.get("tipo"),
				root.get("dataVencimento"),
				criteriaBuilder.sum(root.get("valor"))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), 
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), 
						ultimoDia));
		
		criteriaQuery.groupBy(root.get("tipo"), 
				root.get("dataVencimento"));
		
		TypedQuery<LancamentoEstatisticaDia> typedQuery = manager
				.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
	
		CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaCategoria.class);
		
    Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
    criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class, 
			root.get("categoria"),
			criteriaBuilder.sum(root.get("valor"))));
	
	LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
	LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
	
	criteriaQuery.where(
			criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), 
					primeiroDia),
			criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), 
					ultimoDia));
	
	criteriaQuery.groupBy(root.get("categoria"));
	
	TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager
			.createQuery(criteriaQuery);
	
	return typedQuery.getResultList();
	}
	
	@Override                                                           //page 1               
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		//1)
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		//builder.createQuery construir critério de consulta                                     
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		//3)
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//4) criando restrições
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		//2)
		//criar critérios de consulta
		TypedQuery<Lancamento> query = manager.createQuery(criteria); 
		
		criteria.orderBy(builder.desc(root.get("codigo")));
		
		//page 3  
		adicionarRestricoesDePaginacao(query, pageable);
		
		//page 2  
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter)) ;
		
	}

	

	


	// 5) lista de predicates, para retornar o array
	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		//7)
        List<Predicate> predicates = new ArrayList<>();
        
        //9) //se n foir vazio o pradicate será utilizado
        if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
        	predicates.add(builder.like(
        			//builder.lower: passar para letras minusculas
        			builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

		//6)
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(
					// builder.greaterThanOrEqualTo: Regrade de "Maior que ou igual a"
					// (> || ==)
					builder.greaterThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe()));
		}
		
		//7)
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(
					// builder.lessThanOrEqualTo: Regra de "menos que ou igual a"
					// ( < || ==)
					builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()));
		}
		
		if (lancamentoFilter.getCodigo() != null) {
			predicates.add(builder.equal(root.get("codigo"), lancamentoFilter.getCodigo()));
		}
	
		//8)
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	//page 4
	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	
	}
	
	//page 5
	private Long  total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
		
	}


}
