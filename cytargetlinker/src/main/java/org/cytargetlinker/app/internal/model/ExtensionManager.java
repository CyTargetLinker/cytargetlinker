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
package org.cytargetlinker.app.internal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Edge;
import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.data.Result;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

/**
 * 
 * @author mkutmon
 * This class represents the history of the extension of one network
 * It contains all attributes that are needed for the multi-step extension of a network
 * TODO: undo functionality needs to be implemented!
 *
 */
public class ExtensionManager {

	private CyNetwork network;
	private List<ExtensionStep> history;
	private List<LinkSet> linkSets;
	private Integer threshold = 1;
	private Map<CyEdge, Result> edges;
	
	private Map<LinkSet, InvalidLinkSetException> invalidLinkSets;
	
	public ExtensionManager(CyNetwork network) {
		this.network = network;
		history = new ArrayList<ExtensionStep>();
		linkSets = new ArrayList<LinkSet>();
		edges = new HashMap<CyEdge, Result>();
		invalidLinkSets = new HashMap<LinkSet, InvalidLinkSetException>();
	}

	/**
	 * function that retrieves the regulatory interactions
	 * @param ids - list of input ids
	 * @param ds - list of different datasources (RegINs)
	 * @param dir - directory that contains the RegINs
	 * @param idAttribute - node table column containing the matching identifier
	 * @return ExtensionStep contains all added interactions for this step
	 */
	public ExtensionStep extendNetwork(Set<String> nodeIds, Set<LinkSet> linkSets, Direction direction, String idAttribute) {
		List<Result> results = new ArrayList<Result>();
		for (LinkSet ls : linkSets) {
			// check if datasource has been used before
			LinkSet current = setupLinkSet(ls);
			ExtensionHandler handler = getHandler(current);
			if(handler != null) {
				try {
					Result r = handler.getNeighbours(nodeIds, direction, current);
					if(r != null) {
						results.add(r);
						for(Edge e : r.getEdges()) {
							edges.put(e.getCyEdge(), r);
						}
					}
				} catch(InvalidLinkSetException e) {
					invalidLinkSets.put(ls, e);
				}
			} else {
				invalidLinkSets.put(ls, new InvalidLinkSetException(ls, "LinkSet format not supported."));
			}
		}

		ExtensionStep step = new ExtensionStep(network, idAttribute);
		step.getResults().addAll(results);
		history.add(step);
		int stepNum = history.indexOf(step);
		step.setStepNum(stepNum+1);
		return step;
	}
	
	/**
	 * a datasource is a RegIN that contain regulatory interactions
	 * @param name = name of the RegIN (e.g. ENCODE)
	 * @return DataSource object or null if datasource does not exist yet
	 */
	public LinkSet getLinkSetByName(String name) {
		for(LinkSet ls : linkSets) {
			if(ls.getName().equals(name)) {
				return ls;
			}
		}
		return null;
	}
	
	/**
	 * checks if a specific linkset has already been used
	 */
	private LinkSet setupLinkSet(LinkSet ls) {
		// check if linkset has been used before
		for(LinkSet d : linkSets) {
			if(d.getSource().equals(ls.getSource())) {
				return d;
			}
		}
		// add linkset and select color
		linkSets.add(ls);
		ls.setColor(new ColorSet().getColor(linkSets.size()));
		return ls;
	}
	
	/**
	 * currently only XgmmlFileRegINHandler available
	 */
	private ExtensionHandler getHandler(LinkSet linkSet) {
		if(linkSet.getType().equals(XgmmlFileRegINHandler.getLinkSetType())) {
			return new XgmmlFileRegINHandler();
		}
		return null;
	}

	//////////////////////////////////////
	// SETTERS AND GETTERS
	//////////////////////////////////////
	
	public CyNetwork getNetwork() {
		return network;
	}

	public List<ExtensionStep> getHistory() {
		return history;
	}

	public List<LinkSet> getLinkSets() {
		return linkSets;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public Map<CyEdge, Result> getEdges() {
		return edges;
	}
}
