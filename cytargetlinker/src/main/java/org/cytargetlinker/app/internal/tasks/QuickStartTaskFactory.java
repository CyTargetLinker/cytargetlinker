package org.cytargetlinker.app.internal.tasks;

import org.cytargetlinker.app.internal.Plugin;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class QuickStartTaskFactory extends AbstractTaskFactory {

	private Plugin plugin;
	
	public QuickStartTaskFactory(Plugin plugin){
		this.plugin = plugin;
	}

	public TaskIterator createTaskIterator() {
		return new TaskIterator(new QuickStartTask(plugin));
	}

}