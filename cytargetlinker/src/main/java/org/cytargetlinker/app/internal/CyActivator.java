// CyTargetLinker,
// a Cytoscape plugin to extend biological networks with regulatory interaction
//
// Copyright 2011-2013 Department of Bioinformatics - BiGCaT, Maastricht University
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

import java.util.Properties;

import org.cytargetlinker.app.internal.action.ExtensionAction;
import org.cytargetlinker.app.internal.action.HelpAction;
import org.cytargetlinker.app.internal.gui.CyTargetLinkerPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.property.CyProperty;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CySessionManager;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author mkutmon
 * OSGi activator class of CyTargetLinker
 * Retrieves all necessary services from Cytoscape and registeres all actions
 *
 */
public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CyNetworkViewFactory networkViewFactory = getService(context, CyNetworkViewFactory.class);
		CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
		CyNetworkManager networkManager = getService(context, CyNetworkManager.class);
		DialogTaskManager dialogTaskManager = getService(context, DialogTaskManager.class);
		VisualMappingManager vmmServiceRef = getService(context,VisualMappingManager.class);
		VisualStyleFactory visualStyleFactoryServiceRef = getService(context,VisualStyleFactory.class);
		VisualMappingFunctionFactory vmfFactoryC = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
		VisualMappingFunctionFactory vmfFactoryD = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
		VisualMappingFunctionFactory vmfFactoryP = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
		CyLayoutAlgorithmManager cyAlgorithmManager = getService(context, CyLayoutAlgorithmManager.class);
		CyNetworkViewManager cyNetworkViewManager = getService(context, CyNetworkViewManager.class);
		CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
		OpenBrowser openBrowser = getService(context, OpenBrowser.class);
						
		Plugin plugin = new Plugin(networkFactory, networkManager, dialogTaskManager, networkViewFactory, vmmServiceRef, visualStyleFactoryServiceRef,
				vmfFactoryC, vmfFactoryD, vmfFactoryP, cyAlgorithmManager, cyApplicationManager, cyNetworkViewManager, cySwingApplication);
		registerService(context, plugin, NetworkDestroyedListener.class, new Properties());
		
		// CyTargetLinker implements two actions: extend network and help
		ExtensionAction extAction = new ExtensionAction("Extend network", plugin);
		registerAllServices(context, extAction, new Properties());
		
		HelpAction helpAction = new HelpAction("Help", openBrowser);
		registerAllServices(context, helpAction, new Properties());
		
		// property stores last used RegIN directory
		CyTargetLinkerProperty property = new CyTargetLinkerProperty();
		CyProperty<Properties> prop = property.checkCyProperties(getService(context, CySessionManager.class));
		registerService(context, prop, CyProperty.class, new Properties());
		
		// registers the panel for CyTargetLinker
		CyTargetLinkerPanel panel = new CyTargetLinkerPanel(plugin);
		registerService(context, panel, CytoPanelComponent.class, new Properties());
	}
}