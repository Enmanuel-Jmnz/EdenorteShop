package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.ProductFacade;
import com.github.adminfaces.starter.entities.Product;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class ProductLazyView implements Serializable {

    private LazyDataModel<Product> lazyModel;
    private Product selectedProduct;
    private List<Header> headers;  // Lista de encabezados
    private List<Product> randomProducts; 
    private boolean dataLoaded = false; 
    @EJB
    private ProductFacade productFacade;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<Product>() {

             @Override
            public List<Product> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                
                if (!dataLoaded) {
                    
                    randomProducts = productFacade.getRandomProductsWith20Percent();
                    dataLoaded = true; 
                }

                
                setRowCount(randomProducts.size());
                
                // datos aleatorios paginados
                int toIndex = first + pageSize;
                if (toIndex > randomProducts.size()) {
                    toIndex = randomProducts.size();
                }
                return randomProducts.subList(first, toIndex);
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return randomProducts != null ? randomProducts.size() : 0;
            }
        };

        lazyModel.setRowCount(productFacade.count(null));

        
        headers = new ArrayList<>();
        headers.add(new Header("ID", "productID", "productID", "productID"));
        headers.add(new Header("Name", "name", "name", "name"));
        headers.add(new Header("Description", "description", "description", "description"));
        headers.add(new Header("Price", "price", "price", "price"));
        headers.add(new Header("Stock", "stock", "stock", "stock"));
        headers.add(new Header("Category", "category", "category", "category"));
    }

 

     
    @Getter @Setter
    public static class Header {
         
        private String label;
        
        private String sortBy;
        
        private String filterBy;
      
        private String property;

        public Header(String label, String sortBy, String filterBy, String property) {
            this.label = label;
            this.sortBy = sortBy;
            this.filterBy = filterBy;
            this.property = property;
        }

       
    }
}
