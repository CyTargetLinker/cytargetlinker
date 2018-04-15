package org.cytargetlinker.app.internal.tasks;

import java.awt.Component;
import java.util.Properties;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.ui.CTLCytoPanel;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

public class ShowResultsPanelTask extends AbstractTask {
	final CTLManager manager;
	final ShowResultsPanelTaskFactory factory;
	
	@Tunable (description = "Show / hide result panel")
	public boolean show;

	public ShowResultsPanelTask(final CTLManager manager, final ShowResultsPanelTaskFactory factory, boolean show) {
		this.manager = manager;
		this.factory = factory;
		this.show = show;
	}

	public void run(TaskMonitor monitor) {
		if(!show) {
			monitor.setTitle("Show results panel");
		} else {
			monitor.setTitle("Hide results panel");
		}

		CySwingApplication swingApplication = manager.getService(CySwingApplication.class);
		if(swingApplication != null) {
			CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);
	
			// If the panel is not already registered, create it
			if (show && cytoPanel.indexOfComponent("org.cytargetlinker.app") < 0) {
				CytoPanelComponent2 panel = new CTLCytoPanel(manager);
	
				// Register it
				manager.registerService(panel, CytoPanelComponent.class, new Properties());
				manager.registerService(panel, NetworkAddedListener.class, new Properties());
				manager.registerService(panel, NetworkDestroyedListener.class, new Properties());
				manager.registerService(panel, SessionLoadedListener.class, new Properties());
				
				if (cytoPanel.getState() == CytoPanelState.HIDE) {
					cytoPanel.setState(CytoPanelState.DOCK);
				}
				cytoPanel.setSelectedIndex(cytoPanel.indexOfComponent("org.cytargetlinker.app"));
			} else if (!show && cytoPanel.indexOfComponent("org.cytargetlinker.app") >= 0) {
				int compIndex = cytoPanel.indexOfComponent("org.cytargetlinker.app");
				Component panel = cytoPanel.getComponentAt(compIndex);
				if (panel instanceof CytoPanelComponent2) {
					// Unregister it
					manager.unregisterService(panel, CytoPanelComponent.class);
					manager.unregisterService(panel, NetworkAddedListener.class);
					manager.unregisterService(panel, NetworkDestroyedListener.class);
					manager.unregisterService(panel, SessionLoadedListener.class);
				}
			}
		}
		factory.reregister();
	}

	public static boolean isPanelRegistered(CTLManager manager) {
		CySwingApplication swingApplication = manager.getService(CySwingApplication.class);
		if(swingApplication != null) {
			CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);
			if (cytoPanel.indexOfComponent("org.cytargetlinker.app") >= 0) {
				return true;
			}
		}
		return false;
	}
}
