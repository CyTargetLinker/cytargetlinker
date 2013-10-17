// CyTargetLinker,
// a Cytoscape plugin to extend biological networks with regulatory interaction
//
// Copyright 2011-2013 Department of Bioinformatics - BiGCaT, Maastricht University
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.cytargetlinker.app.internal.data;

/**
 * 
 * @author mkutmon
 * Indicates the direction of the extension of the network
 *
 */
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
