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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytargetlinker.app.internal.gui.CyTargetLinkerPanel;
import org.cytargetlinker.app.internal.gui.VisualStyleCreator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.events.NetworkDestroyedEvent;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.task.create.CloneNetworkTaskFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.swing.DialogTaskManager;

/**
 * 
 * @author martina kutmon
 * This class is the container for all necessary services
 * and functionality within Cytoscape.
 *
 */
public class Plugin implements NetworkDestroyedListener {

	public Map<CyNetwork, ExtensionManager> extensionManager;
	private CyNetworkFactory cyNetworkFactory;
	private CyNetworkManager cyNetworkManager;
	private DialogTaskManager dialogTaskManager;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private VisualMappingManager visualMappingManager;
	private VisualStyleFactory visualStyleFactory; 
	private VisualMappingFunctionFactory visualMappingFunctionFactoryContinous; 
	private VisualMappingFunctionFactory visualMappingFunctionFactoryDiscrete;
	private VisualMappingFunctionFactory visualMappingFunctionFactoryPassthrough;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmManager;
	private CyApplicationManager cyApplicationManager;
	private CyNetworkViewManager cyNetworkViewManager;
	private CySwingApplication cySwingApplication;
	private CloneNetworkTaskFactory cloneNetworkTaskFactory;
	
	private CyTargetLinkerPanel panel;
	
	public Plugin(CyNetworkFactory cyNetFct, 
			CyNetworkManager cyNetMgr, 
			DialogTaskManager dialogTaskManager, 
			CyNetworkViewFactory networkViewFactory,
			VisualMappingManager vmmServiceRef, 
			VisualStyleFactory visualStyleFactoryServiceRef, 
			VisualMappingFunctionFactory vmfFactoryC, 
			VisualMappingFunctionFactory vmfFactoryD, 
			VisualMappingFunctionFactory vmfFactoryP, 
			CyLayoutAlgorithmManager cyAlgorithmManager, 
			CyApplicationManager cyApplicationManager,
			CyNetworkViewManager cyNetworkViewManager,
			CySwingApplication cySwingApplication,
			CloneNetworkTaskFactory cloneNetworkTaskFactory) {
		extensionManager = new HashMap<CyNetwork, ExtensionManager>();
		this.cyNetworkFactory = cyNetFct;
		this.cyNetworkManager = cyNetMgr;
		this.dialogTaskManager = dialogTaskManager;
		this.cyNetworkViewFactory = networkViewFactory;
		this.visualMappingManager = vmmServiceRef;
		this.visualStyleFactory = visualStyleFactoryServiceRef;
		this.visualMappingFunctionFactoryContinous = vmfFactoryC;
		this.visualMappingFunctionFactoryDiscrete = vmfFactoryD;
		this.visualMappingFunctionFactoryPassthrough = vmfFactoryP;
		this.cyLayoutAlgorithmManager = cyAlgorithmManager;
		this.cyApplicationManager = cyApplicationManager;
		this.cyNetworkViewManager = cyNetworkViewManager;
		this.cySwingApplication = cySwingApplication;
		this.cloneNetworkTaskFactory = cloneNetworkTaskFactory;
	}
	
	private VisualStyleCreator vsCreator;
	
	public ExtensionManager getExtensionManager(CyNetwork network) {
		if(extensionManager.containsKey(network)) {
			return extensionManager.get(network);
		} else {
			ExtensionManager mgr = new ExtensionManager(network);
			extensionManager.put(network, mgr);
			return mgr;
		}
	}

	public VisualStyleCreator getVisualStypeCreator() {
		if(vsCreator == null) {
			vsCreator = new VisualStyleCreator(this);
		}
		return vsCreator;
	}

	public Map<CyNetwork, ExtensionManager> getManagers() {
		return extensionManager;
	}


	public DialogTaskManager getDialogTaskManager() {
		return dialogTaskManager;
	}

	public CyNetworkViewFactory getNetworkViewFactory() {
		return cyNetworkViewFactory;
	}
	
	public CyLayoutAlgorithmManager getCyAlgorithmManager() {
		return cyLayoutAlgorithmManager;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}

	public VisualStyleCreator getVsCreator() {
		return vsCreator;
	}

	public CyNetworkViewManager getCyNetworkViewManager() {
		return cyNetworkViewManager;
	}

	public Map<CyNetwork, ExtensionManager> getExtensionManager() {
		return extensionManager;
	}

	public CyNetworkFactory getCyNetworkFactory() {
		return cyNetworkFactory;
	}

	public CyNetworkManager getCyNetworkManager() {
		return cyNetworkManager;
	}

	public CyNetworkViewFactory getCyNetworkViewFactory() {
		return cyNetworkViewFactory;
	}

	public VisualMappingManager getVisualMappingManager() {
		return visualMappingManager;
	}

	public VisualStyleFactory getVisualStyleFactory() {
		return visualStyleFactory;
	}

	public VisualMappingFunctionFactory getVisualMappingFunctionFactoryContinous() {
		return visualMappingFunctionFactoryContinous;
	}

	public VisualMappingFunctionFactory getVisualMappingFunctionFactoryDiscrete() {
		return visualMappingFunctionFactoryDiscrete;
	}

	public VisualMappingFunctionFactory getVisualMappingFunctionFactoryPassthrough() {
		return visualMappingFunctionFactoryPassthrough;
	}

	public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
		return cyLayoutAlgorithmManager;
	}

	public CySwingApplication getCySwingApplication() {
		return cySwingApplication;
	}

	public CyTargetLinkerPanel getPanel() {
		return panel;
	}

	public void setPanel(CyTargetLinkerPanel panel) {
		this.panel = panel;
	}

	@Override
	public void handleEvent(NetworkDestroyedEvent event) {
		List<CyNetwork> toRemove = new ArrayList<CyNetwork>();
		for(CyNetwork network : extensionManager.keySet()) {
			if(!cyNetworkManager.networkExists(network.getSUID())) {
				toRemove.add(network);
			}
		}
		for(CyNetwork network : toRemove) {
			extensionManager.remove(network);
			panel.setCurrentNetwork(null);
		}
		panel.update();
	}


	public CloneNetworkTaskFactory getCloneNetworkTaskFactory() {
		return cloneNetworkTaskFactory;
	}
}
