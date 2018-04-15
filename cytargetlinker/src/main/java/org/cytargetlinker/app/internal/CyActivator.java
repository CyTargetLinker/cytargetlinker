// CyTargetLinker,
// a Cytoscape plugin to extend biological networks with regulatory interactions and other relationships
//
// Copyright 2011-2018 Department of Bioinformatics - BiGCaT, Maastricht University
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.cytargetlinker.app.internal;

import static org.cytoscape.work.ServiceProperties.COMMAND;
import static org.cytoscape.work.ServiceProperties.COMMAND_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_EXAMPLE_JSON;
import static org.cytoscape.work.ServiceProperties.COMMAND_LONG_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_NAMESPACE;
import static org.cytoscape.work.ServiceProperties.COMMAND_SUPPORTS_JSON;

import java.util.Properties;

import org.cytargetlinker.app.internal.action.ExtensionAction;
import org.cytargetlinker.app.internal.action.HelpAction;
import org.cytargetlinker.app.internal.tasks.ApplyLayoutTaskFactory;
import org.cytargetlinker.app.internal.tasks.ApplyVisualStyleTaskFactory;
import org.cytargetlinker.app.internal.tasks.ExtendNetworkTaskFactory;
import org.cytargetlinker.app.internal.tasks.FilterOverlapTaskFactory;
import org.cytargetlinker.app.internal.tasks.ShowResultsPanelTaskFactory;
import org.cytargetlinker.app.internal.tasks.VersionTaskFactory;
import org.cytargetlinker.app.internal.ui.CTLCytoPanel;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

	public CyActivator() {
		super();
	}

	public void start(BundleContext bc) {

		// Get a handle on the CyServiceRegistrar
		CyServiceRegistrar registrar = getService(bc, CyServiceRegistrar.class);
		CTLManager manager = new CTLManager(registrar);

		// Get our version number
		String version = bc.getBundle().getVersion().toString();
		manager.setVersion(version);

		{
			// Register our network added listener and session loaded listener
			registerService(bc, manager, NetworkAddedListener.class, new Properties());
			registerService(bc, manager, SessionLoadedListener.class, new Properties());
		}
		
		{
			ExtendNetworkTaskFactory expandFactory = new ExtendNetworkTaskFactory(manager);
			Properties expandProps = new Properties();
			expandProps.setProperty(COMMAND_NAMESPACE, "cytargetlinker");
			expandProps.setProperty(COMMAND, "extend");
			expandProps.setProperty(COMMAND_DESCRIPTION, "Extends the current/specified network with first neighbour nodes from selected link sets.");
			expandProps.setProperty(COMMAND_LONG_DESCRIPTION, "Extends the current/specified network with first neighbour nodes from selected link sets. " + 
					"If this is the first CyTargetLinker extension of a network, the network is first cloned and then extended, keeping the original network unaltered.");
			registerService(bc, expandFactory, TaskFactory.class, expandProps);
		}
		
		{
			ApplyVisualStyleTaskFactory visualStyleFactory = new ApplyVisualStyleTaskFactory(manager);
			Properties visualStyleProps = new Properties();
			visualStyleProps.setProperty(COMMAND_NAMESPACE, "cytargetlinker");
			visualStyleProps.setProperty(COMMAND, "applyVisualstyle");
			visualStyleProps.setProperty(COMMAND_DESCRIPTION, "Creates CyTargetLinker visual style and applies it to current/specified network");
			visualStyleProps.setProperty(COMMAND_LONG_DESCRIPTION, "Creates CyTargetLinker visual style and applies it to current/specified network");
			registerService(bc, visualStyleFactory, TaskFactory.class, visualStyleProps);
		}
		
		{
			ApplyLayoutTaskFactory layoutFactory = new ApplyLayoutTaskFactory(manager);
			Properties layoutProps = new Properties();
			layoutProps.setProperty(COMMAND_NAMESPACE, "cytargetlinker");
			layoutProps.setProperty(COMMAND, "applyLayout");
			layoutProps.setProperty(COMMAND_DESCRIPTION, "Applies forced-directed layout.");
			layoutProps.setProperty(COMMAND_LONG_DESCRIPTION, "Applies forced-directed layout to current/specified network if network contains less than 10,000 nodes.");
			registerService(bc, layoutFactory, TaskFactory.class, layoutProps);
		}
		
		{
			VersionTaskFactory versionFactory = new VersionTaskFactory(version);
			Properties versionProps = new Properties();
			versionProps.setProperty(COMMAND_NAMESPACE, "cytargetlinker");
			versionProps.setProperty(COMMAND, "version");
			versionProps.setProperty(COMMAND_DESCRIPTION, "Returns the version of CyTargetLinker app");
			versionProps.setProperty(COMMAND_LONG_DESCRIPTION, "Returns the version of CyTargetLinker app");
			versionProps.setProperty(COMMAND_SUPPORTS_JSON, "true");
			versionProps.setProperty(COMMAND_EXAMPLE_JSON, "{\"version\":\"2.1.0\"}");
			registerService(bc, versionFactory, TaskFactory.class, versionProps);
		}
		
		{
			FilterOverlapTaskFactory versionFactory = new FilterOverlapTaskFactory(manager);
			Properties overlapProps = new Properties();
			overlapProps.setProperty(COMMAND_NAMESPACE, "cytargetlinker");
			overlapProps.setProperty(COMMAND, "filterOverlap");
			overlapProps.setProperty(COMMAND_DESCRIPTION, "Hides interactions if not enough link sets support the interactions");
			overlapProps.setProperty(COMMAND_LONG_DESCRIPTION, "Hides interactions if not enough link sets support the interactions");
			registerService(bc, versionFactory, TaskFactory.class, overlapProps);
		}
		
		{
			ShowResultsPanelTaskFactory showResults = new ShowResultsPanelTaskFactory(manager);
			showResults.reregister();
		}
		
		{
			CTLCytoPanel panel = new CTLCytoPanel(manager);
			registerService(bc,panel, NetworkAddedListener.class, new Properties());
			registerService(bc,panel, NetworkDestroyedListener.class, new Properties());
			registerService(bc,panel, SessionLoadedListener.class, new Properties());
		}
		
		{
			// CyTargetLinker implements two actions: extend network and help
			ExtensionAction extAction = new ExtensionAction("Extend network", manager);
			registerAllServices(bc, extAction, new Properties());
			
			OpenBrowser openBrowser = getService(bc, OpenBrowser.class);
			HelpAction helpAction = new HelpAction("Help", openBrowser);
			registerAllServices(bc, helpAction, new Properties());
		}
	}

}
