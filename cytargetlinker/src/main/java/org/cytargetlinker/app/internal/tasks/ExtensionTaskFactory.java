package org.cytargetlinker.app.internal.tasks;

import java.util.List;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ExtensionTaskFactory extends AbstractTaskFactory {

	private Plugin plugin;
	private CyNetwork network;
	private String idAttribute;
	private Direction direction;
	private List<CyNode> nodes;
	private List<DataSource> datasources;
	
	public ExtensionTaskFactory(Plugin plugin, CyNetwork network, String idAttribute, Direction direction, List<CyNode> nodes, List<DataSource> datasources) {
		this.plugin = plugin;
		this.network = network;
		this.idAttribute = idAttribute;
		this.direction = direction;
		this.nodes = nodes;
		this.datasources = datasources;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ExtensionTask(plugin, network, idAttribute, direction, nodes, datasources));
	}

}
