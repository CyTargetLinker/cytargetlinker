package org.cytargetlinker.app.internal.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Edge;
import org.cytargetlinker.app.internal.data.Node;
import org.cytargetlinker.app.internal.data.NodeType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CyTargetLinkerParser extends DefaultHandler {

	private List<Node> nodeList;
	private List<Edge> edgeList;
	private Node currentNode;
	private Edge currentEdge;
	
	private Map<String, Node> nodeMap;
	private List<String> ids;
	
	private String networkName;
	private Map<String, String> networkAttr;
	private Direction dir;
	private DataSource ds;
	
	public CyTargetLinkerParser(List<String> ids, Direction dir, DataSource ds) {
		this.ids = ids;
		this.dir = dir;
		this.ds = ds;
		System.out.println(ids);
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		nodeMap = new HashMap<String, Node>();
		networkAttr = new HashMap<String, String>();
	}
	
	public void startElement(String namespace, String localName, String qName, Attributes atts) throws SAXException {
		if(localName.equals("node")) {
			String id = atts.getValue("id");
			if(id != null && !id.equals("")) {
				currentNode = new Node(id);
				nodeMap.put(id, currentNode);				
			}
		} else if(localName.equals("edge")) {
			String id = atts.getValue("id");
			String source = atts.getValue("source");
			String target = atts.getValue("target");
			
			if(id != null && source != null && target != null) {
				Node sourceNode = nodeMap.get(source);
				Node targetNode = nodeMap.get(target);

				if(sourceNode != null && targetNode != null) {
					if(dir.equals(Direction.BOTH)) {
						if(sourceNode.isQuery() || targetNode.isQuery()) {
							addEdge(currentEdge, id, sourceNode, targetNode);
						}
					} else if (dir.equals(Direction.SOURCE)) {
						if(targetNode.isQuery()) {
							addEdge(currentEdge, id, sourceNode, targetNode);
						}
					} else if (dir.equals(Direction.TARGET)) {
						if(sourceNode.isQuery()) {
							addEdge(currentEdge, id, sourceNode, targetNode);
						}
					}
					
					
				}
			}
		} else if (localName.equals("att")) {
			parseProperties(atts);
		} else if (localName.equals("graph")) {
			String label = atts.getValue("label");
			if(label != null) {
				networkName = label;
			}
		}
		
	}
	
	private void addEdge(Edge e, String id, Node sourceNode, Node targetNode) {
		currentEdge = new Edge(id);
		currentEdge.setSource(sourceNode);
		currentEdge.setTarget(targetNode);
		defineNodeType(sourceNode, NodeType.REGULATOR);
		defineNodeType(targetNode, NodeType.TARGET);
	}
	
	private void defineNodeType(Node node, NodeType type) {
		if(node.getNodeType() == null) {
			node.setNodeType(type);
		} else if(!node.getNodeType().equals(type)) {
			node.setNodeType(NodeType.BOTH);
		}
	}
	
	
	private void parseProperties(Attributes atts) {
		String name = atts.getValue("name");
		String label = atts.getValue("label");
		String value = atts.getValue("value");
		
		String key = "";
		
		if(label != null) {
			key = label;
		} else if(name != null) {
			key = name;
		}
		
		if (key != null && value != null) {
			if(currentNode != null) {
				if(key.equals("identifiers")) {
					currentNode.getIdentifiers().add(value);
					if(ids.contains(value)) {
						currentNode.setQuery(true);
					}
				} else {
					currentNode.getAttributes().put(key, value);
				}
			} else if (currentEdge != null) {
				currentEdge.getAttributes().put(key, value);
			} else {
				networkAttr.put(key, value);
			}
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(localName.equals("node")) {
			if(currentNode != null) {
				nodeList.add(currentNode);
				currentNode = null;
			}
		} else if (localName.equals("edge")) {
			if(currentEdge != null) {
				currentEdge.setDs(ds);
				edgeList.add(currentEdge);
				currentEdge = null;
			}
		}
	}
	
	public void clean() {
		nodeList.clear();
		edgeList.clear();
		nodeMap.clear();
	}
	
	public List<Edge> getEdgeList() {
		return edgeList;
	}

	public String getNetworkName() {
		return networkName;
	}

	public Map<String, String> getNetworkAttr() {
		return networkAttr;
	}
}
