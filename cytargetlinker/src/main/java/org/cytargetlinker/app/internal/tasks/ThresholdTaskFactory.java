package org.cytargetlinker.app.internal.tasks;

import org.cytargetlinker.app.internal.ExtensionManager;
import org.cytargetlinker.app.internal.Plugin;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ThresholdTaskFactory extends AbstractTaskFactory {

	private Plugin plugin;
	private ExtensionManager mgr;
	
	public ThresholdTaskFactory(ExtensionManager mgr, Plugin plugin) {
		this.plugin = plugin;
		this.mgr = mgr;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ThresholdTask(mgr, plugin));
	}

}
