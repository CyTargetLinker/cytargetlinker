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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author martina kutmon
 * Node object within CyTargetLinker
 *
 */
public class Node {

	private String id;
	private Set<String> identifiers;
	private Map<String, String> attributes;
	private boolean query = false;
	private NodeType nodeType;

	public Node(String id) {
		this.id = id;
		identifiers = new HashSet<String>();
		attributes = new HashMap<String, String>();
	}
	
	public String getId() {
		return id;
	}

	public Set<String> getIdentifiers() {
		return identifiers;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public boolean isQuery() {
		return query;
	}

	public void setQuery(boolean query) {
		this.query = query;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	
}
