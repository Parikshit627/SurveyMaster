package com.insuretech.survey.model;


import lombok.Data;

@Data
public class Item {
//	private long id;
	    private String description;
	    private String hsn;
	    private int qty;
	    private double price;
	    private double taxable;
	    private String gst;
	    private double total;
}
