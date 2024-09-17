package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.ProductFacade;
import com.github.adminfaces.starter.entities.User;
import com.github.adminfaces.starter.ejb.UserFacade;
import com.github.adminfaces.starter.entities.Product;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;

import java.util.List;
import java.util.Map;

public class ProductLazyDataModel extends LazyDataModel<Product> {

    private final ProductFacade productFacade;

    public ProductLazyDataModel(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Override
    public List<Product> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        // manejo de paginacion
        String sortField = null;
        boolean ascending = true;

        // Obtener el campo 
        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta sortMeta = sortBy.values().iterator().next(); 
            sortField = sortMeta.getField();
            ascending = sortMeta.getOrder().isAscending();
        }

        // Llamo a facade para obtener la paginacion
        List<Product> products = productFacade.findProducts(first, pageSize, sortField, ascending, filterBy);

        return products;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        
        return productFacade.count(filterBy);
    }
}
