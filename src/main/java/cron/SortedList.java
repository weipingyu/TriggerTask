package cron;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SortedList<E> implements List<E> {
	
	private LinkedList<E> list = new LinkedList<E>();
	private Comparator<E> comparator;
	
	public SortedList() {
		super();
		this.comparator = new Comparator<E>() {

			@SuppressWarnings("unchecked")
			public int compare(E o1, E o2) {
				return ((Comparable<E>)o1).compareTo(o2);
			}
		};
	}

	public SortedList(Comparator<E> comparator) {
		super();
		this.comparator = comparator;
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public Iterator<E> iterator() {
		return list.iterator();
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	public boolean add(E e) {
		if(list.add(e)) {
			sort();
			return true;
		}
		return false;
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		if(list.addAll(c)) {
			sort();
			return true;
		}
		return false;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		if(list.addAll(index, c)) {
			sort();
			return true;
		}
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	public void clear() {
		list.clear();
	}

	public E get(int index) {
		return list.get(index);
	}

	public E set(int index, E element) {
		return list.set(index, element);
	}

	public void add(int index, E element) {
		list.add(index, element);
	}

	public E remove(int index) {
		return list.remove(index);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}
	
	public void sort() {
		Collections.sort(list, comparator);
	}

}
