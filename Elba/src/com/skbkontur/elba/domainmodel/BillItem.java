package com.skbkontur.elba.domainmodel;

import com.skbkontur.elba.datalayer.Entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillItem implements Serializable, Entity {
	public int id;
	public int documentId;
	public String name;
	public BigDecimal quantity;
	public String unit;
	public BigDecimal price;
	public BigDecimal sum;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}
