package com.planco.plancoapi.repository.lancamento;

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

import com.planco.plancoapi.model.Lancamento;
import com.planco.plancoapi.repository.filter.LancamentoFilter;

// implementa o lançamento personalizado
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager; // para trabalhar com a consulta
	
	@Override
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
		
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
		return query.getResultList();
		
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
	
		//8)
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
