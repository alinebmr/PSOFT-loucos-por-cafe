package com.ufcg.psoft.commerce.model;

import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import org.springframework.data.jpa.domain.Specification;

public class CafeSpecification {

    public static Specification<Cafe> hasTipo(TipoGraoCafe tipo) {
        return (root, query, criteriaBuilder) ->
                tipo == null ? null : criteriaBuilder.equal(root.get("tipo"), tipo);
    }

    public static Specification<Cafe> hasOrigem(String origem) {
        return (root, query, criteriaBuilder) ->
                origem == null || origem.isBlank() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("origem")), "%" + origem.toLowerCase() + "%");
    }

    public static Specification<Cafe> hasPerfil(String perfil) {
        return (root, query, criteriaBuilder) ->
                perfil == null || perfil.isBlank() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("perfil")), "%" + perfil.toLowerCase() + "%");
    }

    public static Specification<Cafe> hasQualidade(QualidadeCafe qualidade) {
        return (root, query, criteriaBuilder) ->
                qualidade == null ? null : criteriaBuilder.equal(root.get("qualidade"), qualidade);
    }
}