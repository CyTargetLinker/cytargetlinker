package org.cytargetlinker.app.internal;

import java.util.ArrayList;
import java.util.List;

import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.ExtensionStep;
import org.cytargetlinker.app.internal.data.Result;
import org.cytargetlinker.app.internal.gui.ColorSet;
import org.cytargetlinker.app.internal.resources.ExtensionHandler;
import org.cytargetlinker.app.internal.resources.XgmmlFileRINHandler;
import org.cytoscape.model.CyNetwork;

public class ExtensionManager {

	private CyNetwork network;
	private List<ExtensionStep> history;
	private List<DataSource> datasources;
	
	public ExtensionManager(CyNetwork network) {
		this.network = network;
		history = new ArrayList<ExtensionStep>();
		datasources = new ArrayList<DataSource>();
	}
	
	public ExtensionStep extendNodes(List<String> ids, List<DataSource> ds, Direction dir, String idAttribute) {
		List<Result> results = new ArrayList<Result>();
		int count = 1;
		for (DataSource d : ds) {
			ExtensionHandler handler = decideHandler(d);
			if(handler == null) {
				// TODO: warning
				System.out.println("invalid data source");
			} else {
				Result r = handler.getNeighbours(ids, dir);
				if(r != null) {
					System.out.println(r.getRinName() + "\t" + r.getEdges().size());
					results.add(r);
					d.setColor(new ColorSet().getColor(count));
					count++;
					datasources.add(d);
				} else {
					// TODO: warning
				}
			}
		}

		ExtensionStep step = new ExtensionStep(network, idAttribute);
		step.getResults().addAll(results);
		history.add(step);
		int stepNum = history.indexOf(step);
		step.setStepNum(stepNum+1);
		return step;
	}
	
	
	private ExtensionHandler decideHandler(DataSource ds) {
		if(ds.getType().equals(XgmmlFileRINHandler.getDataSourceType())) {
			return new XgmmlFileRINHandler(ds);
		}
		return null;
	}

	public CyNetwork getNetwork() {
		return network;
	}

	public List<ExtensionStep> getHistory() {
		return history;
	}

	public List<DataSource> getDatasources() {
		return datasources;
	}
}
