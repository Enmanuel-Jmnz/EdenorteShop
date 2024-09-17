/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.adminfaces.starter.ejb;

import com.github.adminfaces.starter.entities.Billdetail;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author EJimenezA
 */
@Stateless
public class BilldetailFacade extends AbstractFacade<Billdetail> {

    @PersistenceContext(unitName = "com.github.adminfaces_admin-starter_war_0.1-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BilldetailFacade() {
        super(Billdetail.class);
    }

    public List<Billdetail> findBySaleID(int saleID) {
        return em.createQuery("SELECT b FROM Billdetail b WHERE b.sale.saleID = :saleID", Billdetail.class)
                .setParameter("saleID", saleID)
                .getResultList();
    }

    public List<Object[]> findTopSellingProducts(int limit) {
    // Asegúrate de que 'Billdetail' es el nombre correcto de la entidad y 'productID' es el campo correcto
    String jpql = "SELECT bd.product.productID, SUM(bd.qty) as totalQty "
                + "FROM Billdetail bd " // Usa el nombre de la entidad correcto aquí
                + "GROUP BY bd.product.productID "
                + "ORDER BY totalQty DESC";

    // Crea la consulta TypedQuery con el tipo de resultado correcto
    TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
    
    // Limita el número de resultados devueltos
    query.setMaxResults(limit);

    // Ejecuta la consulta y retorna la lista de resultados
    return query.getResultList();
}
}
