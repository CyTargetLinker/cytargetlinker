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
package org.cytargetlinker.app.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Edge;
import org.cytargetlinker.app.internal.data.ExtensionStep;
import org.cytargetlinker.app.internal.data.Result;
import org.cytargetlinker.app.internal.gui.ColorSet;
import org.cytargetlinker.app.internal.resources.ExtensionHandler;
import org.cytargetlinker.app.internal.resources.XgmmlFileRegINHandler;
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
	private List<DataSource> datasources;
	private Integer threshold = 1;
	private Map<CyEdge, Result> edges;
	
	public ExtensionManager(CyNetwork network) {
		this.network = network;
		history = new ArrayList<ExtensionStep>();
		datasources = new ArrayList<DataSource>();
		edges = new HashMap<CyEdge, Result>();
	}

	/**
	 * function that retrieves the regulatory interactions
	 * @param ids - list of input ids
	 * @param ds - list of different datasources (RegINs)
	 * @param dir - directory that contains the RegINs
	 * @param idAttribute - node table column containing the matching identifier
	 * @return ExtensionStep contains all added interactions for this step
	 */
	public ExtensionStep extendNodes(List<String> ids, List<DataSource> ds, Direction dir, String idAttribute) {
		List<Result> results = new ArrayList<Result>();
		for (DataSource d : ds) {
			DataSource current = datasourceExists(d);
			if(current == null) {
				current = d;
				datasources.add(current);
				current.setColor(new ColorSet().getColor(datasources.size()));
			}
			ExtensionHandler handler = decideHandler(current);
			if(handler != null) {
				Result r = handler.getNeighbours(ids, dir, current);
				if(r != null) {
					results.add(r);
					for(Edge e : r.getEdges()) {
						edges.put(e.getCyEdge(), r);
					}
				}
			} else {
				// TODO: warning
				System.out.println("invalid data source");
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
	public DataSource getDataSourceByName(String name) {
		for(DataSource d : datasources) {
			if(d.getName().equals(name)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * checks if a specific datasource already exists
	 */
	private DataSource datasourceExists(DataSource ds) {
		for(DataSource d : datasources) {
			if(d.getSource().equals(ds.getSource())) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * currently only XgmmlFileRegINHandler available
	 */
	private ExtensionHandler decideHandler(DataSource ds) {
		if(ds.getType().equals(XgmmlFileRegINHandler.getDataSourceType())) {
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

	public List<DataSource> getDatasources() {
		return datasources;
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
