package org.cytargetlinker.app.internal;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.gui.ExtensionDialog;
import org.cytargetlinker.app.internal.tasks.ExtensionTaskFactory;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.work.TaskIterator;

public class ExtensionAction extends AbstractCyAction {

private Plugin plugin;
	
	public ExtensionAction(final String menuTitle, Plugin plugin) {
		
		super(menuTitle);
		setPreferredMenu("Apps.CyTargetLinker");
		this.plugin = plugin;
	}

	public void actionPerformed(ActionEvent e) {
		
		if(plugin.getCyApplicationManager().getCurrentNetwork() != null) {
			ExtensionDialog dlg = new ExtensionDialog(plugin);
			dlg.setVisible(true);
		}
		
//		CyNetwork network = plugin.getCyApplicationManager().getCurrentNetwork();
//		List<CyNode> nodes = network.getNodeList();
//		String att = "name";
//		File file = new File("/home/mku/networks/hsa-rins");
		
//		ExtensionTaskFactory factory = new ExtensionTaskFactory(plugin, network, att, Direction.SOURCE, nodes, file);
//		TaskIterator it = factory.createTaskIterator();
//		plugin.getDialogTaskManager().execute(it);

	}
	
}
