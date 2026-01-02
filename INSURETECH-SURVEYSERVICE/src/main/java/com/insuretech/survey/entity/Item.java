package com.insuretech.survey.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "item", schema = "insuredb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})


public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String hsn;
    private int qty;
    private double price;
    private double taxable;
    private String gst;
    private double total;

//    @ManyToOne
//    @JoinColumn(name = "invoice_id")
//    private Invoice invoice;
    
    @Column(name = "invoice_id")
     private Long invoiceId;
    // Getters and Setters
}
