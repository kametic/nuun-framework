package org.nuunframework.universalvisitor.api;

public class Metadata {

	private int index = -1;
	private Object key = null;
	private Kind kind = null;

	public Metadata() {
	}

	public Metadata(Object key) {
		this.key = key;
		kind = Kind.OBJECT_MAP_KEY;
	}

	public Metadata(int index) {
		this.index = index;
		kind = Kind.OBJECT_INDEX;
	}
	
	public Kind kind () {
		return kind;
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