package com.fmart.hibernate.pojos;

import java.util.Comparator;
import java.lang.reflect.Field;

import org.primefaces.model.SortOrder;

public class LazySorter<T extends Comparable<T>> implements Comparator<T> {

	private String sortField;

	private SortOrder sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public int compare(T t1, T t2) {
		try {
			Object value1 = null;
			Object value2 = null;
			this.sortField = this.sortField.replace(".", "_");
			Field field = t1.getClass().getDeclaredField(
					this.sortField);
			field.setAccessible(true);
			value1 = field.get(t1);
			field = t2.getClass().getDeclaredField(this.sortField);
			field.setAccessible(true);
			value2 = field.get(t2);
			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
