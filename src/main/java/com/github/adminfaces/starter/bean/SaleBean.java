package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.BilldetailFacade;
import com.github.adminfaces.starter.ejb.SaleFacade;
import com.github.adminfaces.starter.entities.Billdetail;
import com.github.adminfaces.starter.entities.Sale;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "saleBean")
@SessionScoped
public class SaleBean implements Serializable {

    private Sale sale = new Sale();
    private List<Billdetail> selectedBillDetails;
    private int selectedSaleID;
    

    @EJB
    private BilldetailFacade billdetailFacade;
    @EJB
    private SaleFacade saleFacade;
    private List<Sale> sales;
    @Inject
    private CartBean cartBean;
    private Sale selectedSale;
    @Inject
    private BilldetailFacade billDetailFacade;

    @PostConstruct
    public void init() {
        sales = saleFacade.findAll();
        
    }
    
    
    public void makeSale() {
    sale.setUserID(1);  //cambiarlo para tomar el ID del usuario logueado
    sale.setTotalPrice(cartBean.getTotal());

    saleFacade.create(sale);

    // Guardar cada item del carrito en la tabla BillDetail
    for (CartItem item : cartBean.getCartItems()) {
        Billdetail billDetail = new Billdetail();
        billDetail.setSale(sale);
        billDetail.setProduct(item.getProduct());
        billDetail.setQty(item.getQuantity());
        billDetail.setTotalPrice(item.getProduct().getPrice() * item.getQuantity());

        // Crear el bill detail
        billDetailFacade.create(billDetail);
    }

    cartBean.clearCart();

    sales = saleFacade.findAll();

    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Sale saved"));
}

    public List<Sale> getSales() {
        if (sales == null) {
            sales = saleFacade.findAll();
            if (sales == null) {
                sales = new ArrayList<>();
            }
        }
        return sales;
    }

    public Sale getSelectedSale() {
        return selectedSale;
    }

    public void setSelectedSale(Sale selectedSale) {
        this.selectedSale = selectedSale;
    }

        public void selectSale(Sale sale) {
    this.selectedSale = sale;

    }


    public List<Billdetail> getSelectedBillDetails() {
        return selectedBillDetails;
    }

    public int getSelectedSaleID() {
        return selectedSaleID;
    }
}
