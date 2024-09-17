package com.github.adminfaces.starter.ejb;

import com.github.adminfaces.starter.entities.Product;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author EJimenezA
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "com.github.adminfaces_admin-starter_war_0.1-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }

    
    public List<Product> findProducts(int first, int pageSize, String sortField, boolean ascending, Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        // Construir los predicados para los filtros
        List<Predicate> predicates = new ArrayList<>();
        buildPredicates(filters, cb, product, predicates);

        // Establecer los predicados en la consulta
        cq.where(predicates.toArray(new Predicate[0]));

        // Aplicar ordenamiento
        if (sortField != null) {
            if (ascending) {
                cq.orderBy(cb.asc(product.get(sortField)));
            } else {
                cq.orderBy(cb.desc(product.get(sortField)));
            }
        }

        TypedQuery<Product> query = em.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    
    public int count(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.count(product));

        // Construir los predicados para los filtros
        List<Predicate> predicates = new ArrayList<>();
        buildPredicates(filters, cb, product, predicates);

        // Establecer los predicados en la consulta
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult().intValue();
    }

    // Método auxiliar para construir predicados de filtros
    private void buildPredicates(Map<String, FilterMeta> filters, CriteriaBuilder cb, Root<Product> product, List<Predicate> predicates) {
    if (filters != null) {
        filters.forEach((field, filterMeta) -> {
            Object filterValue = filterMeta.getFilterValue();
            if (filterValue != null) {
                Class<?> fieldType = product.get(field).getJavaType();
                
                if (fieldType.equals(String.class)) {
                    predicates.add(cb.like(cb.lower(product.get(field)), "%" + filterValue.toString().toLowerCase() + "%"));
                } else if (fieldType.equals(Integer.class)) {
                    predicates.add(cb.equal(product.get(field), Integer.parseInt(filterValue.toString())));
                } else if (fieldType.equals(Double.class)) {
                    predicates.add(cb.equal(product.get(field), Double.parseDouble(filterValue.toString())));
                } else if (fieldType.equals(Boolean.class)) {
                    predicates.add(cb.equal(product.get(field), Boolean.parseBoolean(filterValue.toString())));
                } else if (fieldType.equals(LocalDate.class)) {
                    predicates.add(cb.equal(product.get(field), LocalDate.parse(filterValue.toString())));
                }
                // Añadir más casos según sea necesario
            }
        });
    }
}
  @PersistenceContext
    private EntityManager entityManager;
public List<Product> getRandomProductsWith20Percent() {
        long totalProducts = countTotalProducts();
        int limit = (int) Math.ceil(totalProducts * 0.8); // Calcular el 20%

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        cq.orderBy(cb.asc(cb.function("RAND", Double.class))); // Usar RAND() para MySQL

        TypedQuery<Product> query = entityManager.createQuery(cq);
        query.setMaxResults(limit); // Limitar al 20%

        return query.getResultList();
    }

    private long countTotalProducts() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> root = cq.from(Product.class);

        cq.select(cb.count(root));

        return entityManager.createQuery(cq).getSingleResult();
    }


    
}
