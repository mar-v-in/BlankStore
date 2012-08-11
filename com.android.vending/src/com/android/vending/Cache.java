package com.android.vending;

import java.util.HashMap;
import java.util.LinkedList;

public class Cache<Identifier, Item> {
	private final HashMap<Identifier, Item> map;
	private final LinkedList<Identifier> range;
	private int size;

	public Cache(int size) {
		map = new HashMap<Identifier, Item>();
		range = new LinkedList<Identifier>();
		this.size = size;
	}

	private void clean() {
		while (range.size() > size) {
			final Identifier rem = range.poll();
			map.remove(rem);
		}
	}

	public Item get(Identifier ident) {
		for (final Identifier ident2 : range) {
			if (ident2.equals(ident)) {
				final Item item = map.get(ident);
				synchronized (range) {
					range.remove(ident2);
					range.push(ident);
				}
				return item;
			}
		}
		return null;
	}

	public int getSize() {
		return size;
	}

	public void put(Identifier ident, Item item) {
		if (item == null || ident == null) {
			return;
		}
		map.put(ident, item);
		synchronized (range) {
			if (range.contains(ident)) {
				range.remove(ident);
			}
			range.push(ident);
			clean();
		}
	}

	public void setSize(int size) {
		synchronized (range) {
			this.size = size;
			clean();
		}
	}
}
