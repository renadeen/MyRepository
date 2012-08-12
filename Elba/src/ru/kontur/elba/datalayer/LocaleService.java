package ru.kontur.elba.datalayer;

import java.math.BigDecimal;
import java.text.*;
import java.util.Date;
import java.util.Locale;

public class LocaleService {
	private final Locale russianLocale = new Locale("ru", "RU");
	private final NumberFormat currencyFormat;
	private final DateFormat russianDateFormat;
	private final String[] russianGenetiveMonths = new String[]{
			"января",
			"февраля",
			"марта",
			"апреля",
			"мая",
			"июня",
			"июля",
			"августа",
			"сентября",
			"октября",
			"ноября",
			"декабря"
	};
	private static LocaleService instance;

	public static LocaleService getInstance() {
		return instance != null ? instance : new LocaleService();
	}

	public LocaleService() {
		currencyFormat = NumberFormat.getInstance(russianLocale);
		currencyFormat.setMinimumFractionDigits(2);
		DateFormatSymbols russSymbol = new DateFormatSymbols(russianLocale);
		russSymbol.setMonths(russianGenetiveMonths);
		russianDateFormat = new SimpleDateFormat("d MMMM yyyy", russSymbol);
	}

	public String formatDate(Date date) {
		return date != null ? russianDateFormat.format(date) : "";
	}

	public String formatCurrency(BigDecimal amount) {
		return currencyFormat.format(amount == null ? BigDecimal.ZERO : amount);
	}

	public BigDecimal parseCurrency(String source) {
		return source.equals("") ? BigDecimal.ZERO : new BigDecimal(source.replace(',', '.').replace(" ",""));
	}
}
