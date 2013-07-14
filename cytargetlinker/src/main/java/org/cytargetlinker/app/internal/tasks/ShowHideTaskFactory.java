package org.cytargetlinker.app.internal.tasks;

import org.cytargetlinker.app.internal.ExtensionManager;
import org.cytargetlinker.app.internal.Plugin;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ShowHideTaskFactory extends AbstractTaskFactory {

	private Plugin plugin;
	private ExtensionManager exMgr;
	
	public ShowHideTaskFactory(Plugin plugin, ExtensionManager exMgr){
		this.plugin = plugin;
		this.exMgr = exMgr;
	} 
	
	@Override
	public TaskIterator createTaskIterator() {
		
		return new TaskIterator(new ShowHideTask(plugin, exMgr));
	}

}
