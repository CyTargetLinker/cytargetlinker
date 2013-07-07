package org.cytargetlinker.app.internal.data;

public enum Direction {
	// add only regulators = SOURCE
	SOURCE ("Add regulators"),
	// add only targets = TARGET
	TARGET ("Add targets"),
	// add both = BOTH
	BOTH ("Add both");
	
	private final String direction;
	
	private Direction(String s) {
		direction = s;
	}
	
	public boolean equalsName(String otherName) {
		return (otherName == null) ? false:direction.equals(otherName);
	}
	
	public String toString() {
		return direction;
	}
}
