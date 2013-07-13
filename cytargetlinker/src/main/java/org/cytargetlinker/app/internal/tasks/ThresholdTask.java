package org.cytargetlinker.app.internal.tasks;

import org.cytargetlinker.app.internal.ExtensionManager;
import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.Utils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class ThresholdTask extends AbstractTask{
	
	private Integer threshold;
	private Plugin plugin;
	private CyNetwork network;
	private CyNetworkView view;
	
	public ThresholdTask(ExtensionManager mgr, Plugin plugin) {
		this.plugin = plugin;
		this.threshold = mgr.getThreshold();
		this.network = mgr.getNetwork();
		this.view = Utils.getNetworkView(network, plugin);
	}
	
	@Override
	public void run(TaskMonitor tm) throws Exception {
		
		tm.setTitle("Apply Threshold");
		tm.setProgress(0.0);
		tm.setStatusMessage("Hidding interactions with overlap threshold > " + threshold);
		
		Utils.applyThreshold(plugin, network, view, threshold);
		
		tm.setProgress(1.0);
		view.updateView();
	}

}
