package ru.kontur.elba.domainmodel;

public enum DocumentType {
	Bill,
	Act,
	DeliveryNote;

	public String toString() {
		switch (this) {
			case Bill:
				return "Счет";
			case Act:
				return "Акт";
			case DeliveryNote:
				return "Накладная";
		}
		throw new IllegalStateException("Invalid DocumentType");
	}
}
