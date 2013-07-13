package org.cytargetlinker.app.internal.tasks;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.data.Result;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ShowHideTaskFactory extends AbstractTaskFactory {

	private Plugin plugin;
	private CyNetwork network;
	private boolean show;
	private Result result;
	
	public ShowHideTaskFactory(Plugin plugin, CyNetwork network, boolean show, Result result){
		this.plugin = plugin;
		this.network = network;
		this.show = show;
		this.result = result;
	} 
	
	@Override
	public TaskIterator createTaskIterator() {
		
		return new TaskIterator(new ShowHideTask(plugin, network, show, result));
	}

}
