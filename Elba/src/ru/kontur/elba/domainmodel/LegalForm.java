package ru.kontur.elba.domainmodel;

public enum LegalForm{
	Ip(0),
	Ooo(1);

	private int value;

	LegalForm(int value) {
		this.value = value;
	}
}
