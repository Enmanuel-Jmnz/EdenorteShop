package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.entities.Product;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 * EditView class for handling editing operations on Product objects in a PrimeFaces data table.
 */
@Named("dtEditView")
@ViewScoped
public class EditView implements Serializable {

    private List<Product> products3;  

    @Inject
    private ProductBean productBean;  

    @PostConstruct
    public void init() {
        
        products3 = productBean.getProducts();  
    }

    public List<Product> getProducts3() {
        return products3;
    }

    public void setProducts3(List<Product> products3) {
        this.products3 = products3;
    }

    /**
     * Handles the event when a row is edited.
     *
     * @param event RowEditEvent containing information about the edited row.
     */
    public void onRowEdit(RowEditEvent<Product> event) {
        Product editedProduct = event.getObject();  // Get the edited product from the event
        productBean.setSelectedProduct(editedProduct);  // Set the selected product in ProductBean
        productBean.updateProduct();  // Update the product using ProductBean's update method
        FacesMessage msg = new FacesMessage("Product Edited", String.valueOf(editedProduct.getProductID()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Handles the event when a row edit is cancelled.
     *
     * @param event RowEditEvent containing information about the cancelled row.
     */
    public void onRowCancel(RowEditEvent<Product> event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", String.valueOf(event.getObject().getProductID()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Handles the event when a cell is edited.
     *
     * @param event CellEditEvent containing information about the edited cell.
     */
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New: " + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
