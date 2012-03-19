package ru.kontur.elba.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberService {
	private static NumberService instance;
	public static NumberService getInstance(){
		return instance == null
				? instance = new NumberService()
				: instance;
	}

	private List<Integer> singularGenetiveUnits = Arrays.asList(2, 3, 4);
	private String[] unitsMap = {"ноль", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
	private String[] tenToTwentyMap = {"десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};
	private String[] decadesMap = {null, null, "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
	private String[] hundredsMap = {null, "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
	private String[] thousandsMap;
	private GenetiveForms roublesForms = new GenetiveForms("рубль", "рубля", "рублей");
	private GenetiveForms thousandForms = new GenetiveForms("тысяча", "тысячи", "тысяч");
	private GenetiveForms copecksForms = new GenetiveForms("копейка", "копейки", "копеек");

	public NumberService() {
		thousandsMap = unitsMap.clone();
		thousandsMap[1] = "одна";
		thousandsMap[2] = "две";
	}

	public String sumInWords(BigDecimal amount) {
		List<String> result = new ArrayList<String>();

		int number = amount.intValue();
		if (number < 1)
			result.add("ноль рублей");
		else{
			processGroup(number / 1000, thousandForms, thousandsMap, false, result);
			processGroup(number % 1000, roublesForms, unitsMap, true, result);
		}
		processCopecks(amount, result);
		return StringHelpers.join(" ", result);
	}

	private void processCopecks(BigDecimal amount, List<String> result) {
		int copecks = amount.remainder(BigDecimal.ONE).multiply(new BigDecimal(100)).intValue();
		result.add(Integer.toString(copecks));
		result.add(copecksForms.getForm(copecks));
	}

	private void processGroup(int number, GenetiveForms forms, String[] unitsMap, boolean addUnitsWord, List<String> result) {
		List<String> myResult = new ArrayList<String>();
		int hundreds = number / 100;
		int decades = number % 100 / 10;
		int units = number % 10;
		if (hundreds > 0) myResult.add(hundredsMap[hundreds]);
		if (decades == 1) {
			myResult.add(tenToTwentyMap[units]);
		} else {
			if (decades > 0) myResult.add(decadesMap[decades]);
			if (units > 0) myResult.add(unitsMap[units]);
		}
		if (!myResult.isEmpty() || addUnitsWord) myResult.add(forms.getForm(number));
		result.addAll(myResult);
	}

	private class GenetiveForms {
		private GenetiveForms(String singular, String genetiveSingular, String genetivePlural) {
			this.singular = singular;
			this.genetiveSingular = genetiveSingular;
			this.genetivePlural = genetivePlural;
		}

		private String singular;
		private String genetiveSingular;
		private String genetivePlural;

		public String getForm(int amount) {
			int decades = amount % 100 / 10;
			int units = amount % 10;
			if (decades == 1)
				return genetivePlural;
			if (units == 1)
				return singular;
			if (singularGenetiveUnits.contains(units))
				return genetiveSingular;
			return genetivePlural;
		}
	}
}
