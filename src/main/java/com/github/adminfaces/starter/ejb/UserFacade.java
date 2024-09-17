/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.adminfaces.starter.ejb;

import com.github.adminfaces.starter.entities.User;
import com.github.adminfaces.starter.infra.model.SortOrder;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author EJimenezA
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "com.github.adminfaces_admin-starter_war_0.1-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        try {
            TypedQuery<User> usuarioQuery = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class);
            usuarioQuery.setParameter("email", email);
            usuarioQuery.setParameter("password", password);
            return Optional.ofNullable(usuarioQuery.getSingleResult());
        } catch (Exception e) {
            System.out.println("error:" + e);
            return null;
        }

    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public void edit(User entity) {
        em.merge(entity);
    }

  public List<User> findUsers(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<User> cq = cb.createQuery(User.class);
    Root<User> user = cq.from(User.class);
    
    // Agregar filtros dinámicamente
    List<Predicate> predicates = new ArrayList<>();
    for (Map.Entry<String, Object> filter : filters.entrySet()) {
        String attributeName = filter.getKey();
        Object filterValue = filter.getValue();
        predicates.add(cb.like(cb.lower(user.get(attributeName)), "%" + filterValue.toString().toLowerCase() + "%"));
    }
    cq.where(predicates.toArray(new Predicate[0]));
    
    // Manejo de ordenamiento
    if (sortField != null) {
        if (ascending) {
            cq.orderBy(cb.asc(user.get(sortField)));
        } else {
            cq.orderBy(cb.desc(user.get(sortField)));
        }
    }
    
    // Ejecución de la consulta
    return em.createQuery(cq)
            .setFirstResult(first)
            .setMaxResults(pageSize)
            .getResultList();
}

public int count(Map<String, Object> filters) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<User> user = cq.from(User.class);
    cq.select(cb.count(user));

    List<Predicate> predicates = new ArrayList<>();
    for (Map.Entry<String, Object> filter : filters.entrySet()) {
        String attributeName = filter.getKey();
        Object filterValue = filter.getValue();
        predicates.add(cb.like(cb.lower(user.get(attributeName)), "%" + filterValue.toString().toLowerCase() + "%"));
    }
    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).getSingleResult().intValue();
}

    
private void buildPredicates(Map<String, FilterMeta> filters, CriteriaBuilder cb, Root<User> user, List<Predicate> predicates) {
    if (filters != null) {
        filters.forEach((field, filterMeta) -> {
            if (field != null && filterMeta != null && filterMeta.getFilterValue() != null) {
                Object filterValue = filterMeta.getFilterValue();
                try {
                    if (user.get(field).getJavaType().equals(String.class)) {
                        predicates.add(cb.like(cb.lower(user.get(field)), "%" + filterValue.toString().toLowerCase() + "%"));
                    } else if (user.get(field).getJavaType().equals(Integer.class)) {
                        predicates.add(cb.equal(user.get(field), Integer.parseInt(filterValue.toString())));
                    } else if (user.get(field).getJavaType().equals(Double.class)) {
                        predicates.add(cb.equal(user.get(field), Double.parseDouble(filterValue.toString())));
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al procesar el campo '" + field + "': " + e.getMessage());
                }
            } else {
                System.err.println("Se encontró un filtro con clave o valor nulo: " + field);
            }
        });
    }
}



}


