package ru.kontur.elba.domainmodel;

import ru.kontur.elba.datalayer.Entity;
import ru.kontur.elba.datalayer.LocaleService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Bill implements Entity {
	public Bill() {
		billItems = new ArrayList<BillItem>();
	}

	public int id;
    public String number;
    public BigDecimal sum;
    public Date date;
    public String contractorName;
	public ArrayList<BillItem> billItems;

//    public HashMap<String, String> toHashMap(){
//		HashMap<String, String> result = new HashMap<String, String>();
//		result.put("id", Integer.toString(id));
//		result.put("caption", number);
//		result.put("sum", Double.toString(sum));
//		return result;
//    }

	public String getFormattedDate() {
		return LocaleService.getInstance().formatDate(date);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}
