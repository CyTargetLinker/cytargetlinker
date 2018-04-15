package org.cytargetlinker.app.internal.tasks;

import static org.cytoscape.work.ServiceProperties.COMMAND;
import static org.cytoscape.work.ServiceProperties.COMMAND_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_LONG_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_NAMESPACE;
import static org.cytoscape.work.ServiceProperties.IN_MENU_BAR;
import static org.cytoscape.work.ServiceProperties.MENU_GRAVITY;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;

import java.util.Properties;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class ShowResultsPanelTaskFactory extends AbstractTaskFactory {
	final CTLManager manager;
	boolean show = false;

	public ShowResultsPanelTaskFactory(final CTLManager manager) {
		this.manager = manager;
	}

	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ShowResultsPanelTask(manager, this, show));
	}

	public void reregister() {
		manager.unregisterService(this, TaskFactory.class);
		Properties props = new Properties();
		props.setProperty(PREFERRED_MENU, "Apps.CyTargetLinker");

		if (ShowResultsPanelTask.isPanelRegistered(manager)) {
			props.setProperty(TITLE, "Hide results panel");
			show = false;
		} else {
			props.setProperty(TITLE, "Show results panel");
			show = true;
		}
		props.setProperty(MENU_GRAVITY, "1.0");
		props.setProperty(IN_MENU_BAR, "true");
		
		props.setProperty(COMMAND_NAMESPACE, "cytargetlinker");
		props.setProperty(COMMAND, "showPanel");
		props.setProperty(COMMAND_DESCRIPTION, "Shows/hides CyTargetLinker result panel.");
		props.setProperty(COMMAND_LONG_DESCRIPTION, "Shows/hides CyTargetLinker result panel.");
		manager.registerService(this, TaskFactory.class, props);
	}

	public boolean isReady() {
		// We always want to be able to shut it off
		if (!show) return true;

		CyNetwork net = manager.getCurrentNetwork();
		if (net == null) return false;

		if (manager.isCTLNetwork(net)) return true;

		return false;
	}
}

