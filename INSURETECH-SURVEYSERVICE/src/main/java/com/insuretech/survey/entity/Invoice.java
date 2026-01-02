package com.insuretech.survey.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "invoice", schema = "insuredb")

public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String companyGenId;
    private String invoiceNo;
    private String date;
    private String referenceNumber;
    private String billToOption;
    private String shippedToOption;
    private String subjectMatter;

    private Double estimate;
    private Double assessment;

    private Double subtotal;
    private Double sgst;
    private Double cgst;
    private Double igst;
    private Double totalValue;
    private Double roundoff;

    private String totalValueWords;

//    @Lob
    private String paymentInfo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Consigner consigner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Receiver receiver;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Consignee consignee;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private InsuranceInfo insuranceInfo;
    
    @Transient
    private List<Item> items;

//    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Item> items;

    // Getters and Setters
}
