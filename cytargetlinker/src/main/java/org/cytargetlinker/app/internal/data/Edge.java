package org.cytargetlinker.app.internal.data;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;


public class Edge {

	private String id;
	private Node source;
	private Node target;
	private Map<String, String> attributes;
	
	private CyNode cySource;
	private CyNode cyTarget;
	private CyEdge cyEdge;
	
	private DataSource ds;
	
	public Edge(String id) {
		this.id = id;
		attributes = new HashMap<String, String>();
	}

	public String getId() {
		return id;
	}
	
	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public CyNode getCySource() {
		return cySource;
	}

	public void setCySource(CyNode cySource) {
		this.cySource = cySource;
	}

	public CyNode getCyTarget() {
		return cyTarget;
	}

	public void setCyTarget(CyNode cyTarget) {
		this.cyTarget = cyTarget;
	}
	public CyEdge getCyEdge() {
		return cyEdge;
	}
	public void setCyEdge(CyEdge cyEdge) {
		this.cyEdge = cyEdge;
	}
	public DataSource getDs() {
		return ds;
	}
	public void setDs(DataSource ds) {
		this.ds = ds;
	}
}
