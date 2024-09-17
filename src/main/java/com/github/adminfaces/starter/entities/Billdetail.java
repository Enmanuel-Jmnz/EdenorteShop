package com.github.adminfaces.starter.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "billdetail")
@NamedQueries({
    @NamedQuery(name = "Billdetail.findAll", query = "SELECT b FROM Billdetail b"),
    @NamedQuery(name = "Billdetail.findByBilldetailID", query = "SELECT b FROM Billdetail b WHERE b.billdetailID = :billdetailID"),
    @NamedQuery(name = "Billdetail.findBySaleID", query = "SELECT b FROM Billdetail b WHERE b.sale.saleID = :saleID"),
    @NamedQuery(name = "Billdetail.findByProductID", query = "SELECT b FROM Billdetail b WHERE b.product.productID = :productID"),
    @NamedQuery(name = "Billdetail.findByQty", query = "SELECT b FROM Billdetail b WHERE b.qty = :qty"),
    @NamedQuery(name = "Billdetail.findByTotalPrice", query = "SELECT b FROM Billdetail b WHERE b.totalPrice = :totalPrice")})
public class Billdetail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "billdetailID")
    private Integer billdetailID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "saleID", referencedColumnName = "saleID")
    private Sale sale;

    @ManyToOne(optional = false)
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product product;

    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private int qty;

    @Basic(optional = false)
    @NotNull
    @Column(name = "totalPrice")
    private double totalPrice;

    // Getters y Setters
    
    public Integer getBilldetailID() {
        return billdetailID;
    }

    public void setBilldetailID(Integer billdetailID) {
        this.billdetailID = billdetailID;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billdetailID != null ? billdetailID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Billdetail)) {
            return false;
        }
        Billdetail other = (Billdetail) object;
        if ((this.billdetailID == null && other.billdetailID != null) || (this.billdetailID != null && !this.billdetailID.equals(other.billdetailID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.github.adminfaces.starter.entities.Billdetail[ billdetailID=" + billdetailID + " ]";
    }
}