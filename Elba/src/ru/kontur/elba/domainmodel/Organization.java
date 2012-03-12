package ru.kontur.elba.domainmodel;

import com.google.gson.annotations.SerializedName;

public class Organization {
	@SerializedName("OrganizationShortName")
	public String organizationShortName;

	@SerializedName("OrganizationFullName")
	public String organizationFullName;

	@SerializedName("Name")
	public String name;

	@SerializedName("Patronymic")
	public String patronymic;

	@SerializedName("Surname")
	public String surname;

	@SerializedName("LegalForm")
	public int legalForm;

	@SerializedName("Address")
	public String address;

	@SerializedName("BankName")
	public String bankName;

	@SerializedName("BankBik")
	public String bankBik;

	@SerializedName("BankCity")
	public String bankCity;

	@SerializedName("BankCorrAccount")
	public String bankCorrAccount;

	@SerializedName("Phone")
	public String phone;

	@SerializedName("AccountNumber")
	public String accountNumber;

	@SerializedName("INN")
	public String inn;

	@SerializedName("KPP")
	public String kpp;

	@SerializedName("OKPO")
	public String okpo;

	public String getLegalName() {
		return legalForm == 0
				? "ИП " + surname + " " + name + " " + patronymic
				: organizationShortName;
	}
}
