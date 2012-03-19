package ru.kontur.elba.plainTests;

import org.junit.Assert;
import org.junit.Test;
import ru.kontur.elba.core.NumberService;

import java.math.BigDecimal;

public class SumInWordsTest {
	private NumberService numberService = NumberService.getInstance();

	private void checkRoubles(String expected, int actualRoubles) {
		Assert.assertEquals(expected + " 0 копеек", numberService.sumInWords(new BigDecimal(actualRoubles)));
	}
	@Test
	public void testUnits() {
		checkRoubles("ноль рублей", 0);
		checkRoubles("один рубль", 1);
		checkRoubles("два рубля", 2);
		checkRoubles("три рубля", 3);
		checkRoubles("четыре рубля", 4);
		checkRoubles("пять рублей", 5);
		checkRoubles("шесть рублей", 6);
		checkRoubles("семь рублей", 7);
		checkRoubles("восемь рублей", 8);
		checkRoubles("девять рублей", 9);
	}

	@Test
	public void testNumbersFromTenToTwenty() {
		checkRoubles("одиннадцать рублей", 11);
		checkRoubles("четырнадцать рублей", 14);
	}

	@Test
	public void testDecadesOverTwenty() {
		checkRoubles("двадцать рублей", 20);
		checkRoubles("тридцать три рубля", 33);
	}

	@Test
	public void testHundreds() {
		checkRoubles("сто рублей", 100);
		checkRoubles("пятьсот пятьдесят один рубль", 551);
	}

	@Test
	public void testThousands() {
		checkRoubles("одна тысяча рублей", 1000);
		checkRoubles("семьсот восемьдесят девять тысяч четыреста пятьдесят шесть рублей", 789456);
	}

	@Test
	public void testCopecks() {
		Assert.assertEquals("один рубль 12 копеек", numberService.sumInWords(new BigDecimal("1.12")));
		Assert.assertEquals("один рубль 5 копеек", numberService.sumInWords(new BigDecimal("1.05")));
		Assert.assertEquals("один рубль 2 копейки", numberService.sumInWords(new BigDecimal("1.02")));
		Assert.assertEquals("ноль рублей 1 копейка", numberService.sumInWords(new BigDecimal("0.01")));
	}
}
