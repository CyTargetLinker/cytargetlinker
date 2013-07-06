package org.cytargetlinker.app.internal;

import java.util.HashMap;
import java.util.Map;

import org.cytargetlinker.app.internal.gui.VisualStyleCreator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.swing.DialogTaskManager;

public class Plugin {

	public Map<CyNetwork, ExtensionManager> managers;
	private CyNetworkFactory cyNetFct;
	private CyNetworkManager cyNetMgr;
	private DialogTaskManager dialogTaskManager;
	private CyNetworkViewFactory networkViewFactory;
	private CyNetworkViewManager cyNetViewMgr;
	private VisualMappingManager vmmServiceRef;
	private VisualStyleFactory visualStyleFactoryServiceRef; 
	private VisualMappingFunctionFactory vmfFactoryC; 
	private VisualMappingFunctionFactory vmfFactoryD;
	private VisualMappingFunctionFactory vmfFactoryP;
	private CyLayoutAlgorithmManager cyAlgorithmManager;
	private CyApplicationManager cyApplicationManager;
	
	
	public Plugin(CyNetworkFactory cyNetFct, 
			CyNetworkManager cyNetMgr, 
			DialogTaskManager dialogTaskManager, 
			CyNetworkViewFactory networkViewFactory,
			CyNetworkViewManager cyNetViewMgr, 
			VisualMappingManager vmmServiceRef, 
			VisualStyleFactory visualStyleFactoryServiceRef, 
			VisualMappingFunctionFactory vmfFactoryC, 
			VisualMappingFunctionFactory vmfFactoryD, 
			VisualMappingFunctionFactory vmfFactoryP, 
			CyLayoutAlgorithmManager cyAlgorithmManager, 
			CyApplicationManager cyApplicationManager) {
		managers = new HashMap<CyNetwork, ExtensionManager>();
		this.cyNetFct = cyNetFct;
		this.cyNetMgr = cyNetMgr;
		this.dialogTaskManager = dialogTaskManager;
		this.networkViewFactory = networkViewFactory;
		this.cyNetViewMgr = cyNetViewMgr;
		this.vmmServiceRef = vmmServiceRef;
		this.visualStyleFactoryServiceRef = visualStyleFactoryServiceRef;
		this.vmfFactoryC = vmfFactoryC;
		this.vmfFactoryD = vmfFactoryD;
		this.vmfFactoryP = vmfFactoryP;
		this.cyAlgorithmManager = cyAlgorithmManager;
		this.cyApplicationManager = cyApplicationManager;
	}
	
	private VisualStyleCreator vsCreator;
	
	public ExtensionManager getExtensionManager(CyNetwork network) {
		if(managers.containsKey(network)) {
			return managers.get(network);
		} else {
			ExtensionManager mgr = new ExtensionManager(network);
			managers.put(network, mgr);
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
		return managers;
	}

	public CyNetworkFactory getCyNetFct() {
		return cyNetFct;
	}

	public CyNetworkManager getCyNetMgr() {
		return cyNetMgr;
	}

	public DialogTaskManager getDialogTaskManager() {
		return dialogTaskManager;
	}

	public CyNetworkViewFactory getNetworkViewFactory() {
		return networkViewFactory;
	}

	public CyNetworkViewManager getCyNetViewMgr() {
		return cyNetViewMgr;
	}

	public VisualMappingManager getVmmServiceRef() {
		return vmmServiceRef;
	}

	public VisualStyleFactory getVisualStyleFactoryServiceRef() {
		return visualStyleFactoryServiceRef;
	}

	public VisualMappingFunctionFactory getVmfFactoryC() {
		return vmfFactoryC;
	}

	public VisualMappingFunctionFactory getVmfFactoryD() {
		return vmfFactoryD;
	}

	public VisualMappingFunctionFactory getVmfFactoryP() {
		return vmfFactoryP;
	}

	public CyLayoutAlgorithmManager getCyAlgorithmManager() {
		return cyAlgorithmManager;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}

	public VisualStyleCreator getVsCreator() {
		return vsCreator;
	}
}
