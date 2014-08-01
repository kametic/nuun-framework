package org.nuunframework.universalvisitor.api;

public class Metadata {

	private int index = -1;
	private Object key = null;

	public Metadata() {
	}

	public Metadata(Object key) {
		this.key = key;
	}

	public Metadata(int index) {
		this.index = index;
	}

	public int index() {
		return index;
	}

	public Object key() {
		return key;
	}

	@Override
	public String toString() {
		String metadata = "";

		if (index > -1) {
			metadata = "[" + index + "]";
		}

		if (key != null) {
			metadata += "[" + key + "]";
		}

		return metadata;
	}
}