package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.BilldetailFacade;
import com.github.adminfaces.starter.entities.Billdetail;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "billDetailBean")
@SessionScoped
public class BilldetailBean implements Serializable {

    private List<Billdetail> billdetails;
    private int selectedSaleID;

    @EJB
    private BilldetailFacade billdetailFacade;

    @PostConstruct
    public void init() {
        // Cargar todos los detalles de las facturas al iniciar
//        billdetails = billdetailFacade.findAll();
    }

    public void filterBillDetailsBySaleID(int saleID) {
    billdetails = billdetailFacade.findBySaleID(saleID);
}

    public List<Billdetail> getBilldetails() {
        return billdetails;
    }

    public void setBilldetails(List<Billdetail> billdetails) {
        this.billdetails = billdetails;
    }

    public int getSelectedSaleID() {
        return selectedSaleID;
    }

    public void setSelectedSaleID(int selectedSaleID) {
        this.selectedSaleID = selectedSaleID;
    }
}
