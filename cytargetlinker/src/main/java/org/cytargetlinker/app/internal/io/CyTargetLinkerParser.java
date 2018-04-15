// CyTargetLinker,
// a Cytoscape plugin to extend biological networks with regulatory interactions and other relationships
//
// Copyright 2011-2018 Department of Bioinformatics - BiGCaT, Maastricht University
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
package org.cytargetlinker.app.internal.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Edge;
import org.cytargetlinker.app.internal.data.Node;
import org.cytargetlinker.app.internal.data.NodeType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author mkutmon
 * reads in xgmml files and filters interactions based on the query ids
 *
 */
public class CyTargetLinkerParser extends DefaultHandler {

	private List<Node> nodeList;
	private List<Edge> edgeList;
	private Node currentNode;
	private Edge currentEdge;
	
	private Map<String, Node> nodeMap;
	private Set<String> ids;
	
	private String networkName;
	private Map<String, String> networkAttr;
	private Direction dir;
	private LinkSet ds;
	
	public CyTargetLinkerParser(Set<String> nodeIds, Direction dir, LinkSet ds) {
		this.ids = nodeIds;
		this.dir = dir;
		this.ds = ds;
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		nodeMap = new HashMap<String, Node>();
		networkAttr = new HashMap<String, String>();
	}
	
	/**
	 * parses a new XML element
	 */
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
					} else if (dir.equals(Direction.SOURCES)) {
						if(targetNode.isQuery()) {
							addEdge(currentEdge, id, sourceNode, targetNode);
						}
					} else if (dir.equals(Direction.TARGETS)) {
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
	
	/**
	 * adds a new CTL Edge
	 */
	private void addEdge(Edge e, String id, Node sourceNode, Node targetNode) {
		currentEdge = new Edge(id);
		currentEdge.setSource(sourceNode);
		currentEdge.setTarget(targetNode);
		defineNodeType(sourceNode, NodeType.SOURCE);
		defineNodeType(targetNode, NodeType.TARGET);
	}
	
	/**
	 * defines node type
	 */
	private void defineNodeType(Node node, NodeType type) {
		if(node.getNodeType() == null) {
			node.setNodeType(type);
		} else if(!node.getNodeType().equals(type)) {
			node.setNodeType(NodeType.BOTH);
		}
	}
	
	/**
	 * parses XML attributes
	 */
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

	/**
	 * XML element close tag
	 */
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
	
	// ////////////////////////////////////
	// SETTERS AND GETTERS
	// ////////////////////////////////////
	
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
