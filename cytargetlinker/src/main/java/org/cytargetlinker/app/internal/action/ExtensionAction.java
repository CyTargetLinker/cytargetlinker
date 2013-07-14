package org.cytargetlinker.app.internal.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.gui.ExtensionDialog;
import org.cytoscape.application.swing.AbstractCyAction;

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
		} else {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "No network in current session.", "Warning", JOptionPane.WARNING_MESSAGE);
		}

	}
	
}
