/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.adminfaces.starter.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author EJimenezA
 */
@Entity
@Table(name = "sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s"),
    @NamedQuery(name = "Sale.findBySaleID", query = "SELECT s FROM Sale s WHERE s.saleID = :saleID"),
    @NamedQuery(name = "Sale.findByUserID", query = "SELECT s FROM Sale s WHERE s.userID = :userID"),
    @NamedQuery(name = "Sale.findByTotalPrice", query = "SELECT s FROM Sale s WHERE s.totalPrice = :totalPrice"),
    @NamedQuery(name = "Sale.findByDate", query = "SELECT s FROM Sale s WHERE s.date = :date")})
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "saleID")
    private Integer saleID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userID")
    private int userID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalPrice")
    private double totalPrice;
    @Column(name = "date")
    
    private LocalDateTime date;

    @PrePersist
    public void prePersist() {
        date = LocalDateTime.now();
        
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleID != null ? saleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sale)) {
            return false;
        }
        Sale other = (Sale) object;
        if ((this.saleID == null && other.saleID != null) || (this.saleID != null && !this.saleID.equals(other.saleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.github.adminfaces.starter.entities.Sale[ saleID=" + saleID + " ]";
    }

    public void setUserID(Integer userID) {
    this.userID = userID;
}

public void setTotalPrice(double total) {
    this.totalPrice = total;
}
    
}
