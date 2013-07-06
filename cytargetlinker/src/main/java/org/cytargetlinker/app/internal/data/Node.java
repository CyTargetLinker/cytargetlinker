package org.cytargetlinker.app.internal.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
