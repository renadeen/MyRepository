package ru.kontur.elba;

import com.google.gson.Gson;
import ru.kontur.elba.domainmodel.Organization;

public class OrganizationRepository {
	public Organization Get() {
		String json = "{\"AbonentId\":\"8e55018a-24bb-4e21-bd44-1c45413265e3\",\"AccountNumber\":\"12345678901234567890\",\"AccountantName\":null,\"AccountantPatronymic\":null,"
		+ "\"AccountantSurname\":null,\"Address\":\"198188, г Санкт-Петербург, ул Васи Алексеева\","
		+ "\"BankBik\":\"044525593\",\"BankCity\":\"МОСКВА\",\"BankCorrAccount\":\"30101810200000000593\",\"BankName\":\"ОАО \\\"АЛЬФА-БАНК\\\"\","
		+ "\"BossName\":null,\"BossPatronymic\":null,\"BossSurname\":null,\"INN\":\"665902557523\",\"KPP\":null,\"LegalForm\":0,"
		+ "\"Name\":\"Ренат\",\"OKPO\":\"1234567890\",\"OrganizationFullName\":null,\"OrganizationShortName\":null,\"Patronymic\":\"Валиуллович\","
		+ "\"Phone\":\"2\",\"Surname\":\"Хайретиднов\"}";
		return new Gson().fromJson(json, Organization.class);
	}
}
