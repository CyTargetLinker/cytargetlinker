package org.cytargetlinker.app.internal.action;

import java.awt.event.ActionEvent;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.tasks.QuickStartTaskFactory;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.work.TaskIterator;


/**
 * Creates a new menu item under Apps menu section.
 *
 */
public class QuickStartAction extends AbstractCyAction {

	private Plugin plugin;
	
	public QuickStartAction(final String menuTitle, Plugin plugin) {
		
		super(menuTitle);
		setPreferredMenu("Apps.CyTargetLinker");
		this.plugin = plugin;
	}

	public void actionPerformed(ActionEvent e) {
		
		QuickStartTaskFactory fact = new QuickStartTaskFactory(plugin);
		TaskIterator it = fact.createTaskIterator();
		plugin.getDialogTaskManager().execute(it);

	}
}