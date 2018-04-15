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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;

import org.cytargetlinker.app.internal.model.ExtensionManager;
import org.cytargetlinker.app.internal.model.VisualStyleCreator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.events.NetworkDestroyedEvent;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;

/**
 * 
 * @author mkutmon
 *
 */
public class CTLManager implements NetworkAddedListener, NetworkDestroyedListener, SessionLoadedListener {
	
	private final CyServiceRegistrar registrar;
	
	// CyTargetLinker app version
	private String version;
		
	private Map<CyNetwork, ExtensionManager> extensions;
	
	public CTLManager(CyServiceRegistrar registrar) {
		this.registrar = registrar;
		extensions = new HashMap<CyNetwork, ExtensionManager>();
	}

	public ExtensionManager getExtensionManager(CyNetwork network) {
		if(!extensions.containsKey(network)) {
			ExtensionManager mgr = new ExtensionManager(network);
			extensions.put(network, mgr);
		}
		return extensions.get(network);
	}
	
	@Override
	public void handleEvent(NetworkAddedEvent nae) {
	}
	
	@Override
	public void handleEvent(NetworkDestroyedEvent nde) {
		List<CyNetwork> toRemove = new ArrayList<CyNetwork>();
		for(CyNetwork network : extensions.keySet()) {
			if(!getNetworkManager().networkExists(network.getSUID())) {
				toRemove.add(network);
			}
		}
		for(CyNetwork network : toRemove) {
			extensions.remove(network);
		}
	}
	
	/**
	 * returns network view
	 */
	public CyNetworkView getNetworkView(CyNetwork network) {
		Collection<CyNetworkView> views = getNetworkViewManager().getNetworkViews(network);
		CyNetworkView view;
		if(!views.isEmpty()) {
			view = views.iterator().next();
		} else {
			view = getNetworkViewFactory().createNetworkView(network);
			getNetworkViewManager().addNetworkView(view);
		}
		return view;
	}

	public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
		return registrar.getService(CyLayoutAlgorithmManager.class);
	}
	
	public CyNetwork getCurrentNetwork() {
		return registrar.getService(CyApplicationManager.class).getCurrentNetwork();
	}
	
	public CyNetworkFactory getNetworkFactory() {
		return registrar.getService(CyNetworkFactory.class);
	}

	public CyNetworkViewFactory getNetworkViewFactory() {
		return (registrar.getService(CyNetworkViewFactory.class));
	}
	
	public CyNetworkManager getNetworkManager() {
		return (registrar.getService(CyNetworkManager.class));
	}
	
	public CyNetworkViewManager getNetworkViewManager() {
		return (registrar.getService(CyNetworkViewManager.class));
	}
	
	public JFrame getApplicationFrame() {
		return registrar.getService(CySwingApplication.class).getJFrame();
	}
	
	public Map<CyNetwork, ExtensionManager> getExtensions() {
		return extensions;
	}

	// get CyTargetLinker app version
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public VisualMappingManager getVisualMappingManager() {
		return (registrar.getService(VisualMappingManager.class));
	}
	
	public VisualStyleFactory getVisualStyleFactory() {
		return (registrar.getService(VisualStyleFactory.class));
	}

	public VisualMappingFunctionFactory getVisualMappingFunctionFactoryPassthrough() {
		return (registrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)"));
	}
	
	public VisualMappingFunctionFactory getVisualMappingFunctionFactoryDiscrete() {
		return (registrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)"));
	}
	
	public VisualMappingFunctionFactory getVisualMappingFunctionFactoryContinuous() {
		return (registrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=continuous)"));
	}

	public VisualStyleCreator getVisualStypeCreator() {
		return new VisualStyleCreator(this);
	}
	
	
	
	public void unregisterService(Object service, Class<?> clazz) {
		registrar.unregisterService(service, clazz);
	}
	
	public <T> T getService(Class<? extends T> clazz) {
		return registrar.getService(clazz);
	}
	
	public void registerService(Object service, Class<?> clazz, Properties props) {
		registrar.registerService(service, clazz, props);
	}

	public boolean isCTLNetwork(CyNetwork net) {
		
		if(net.getRow(net).getTable().getColumn("CTL.Net") != null) {
			if(net.getRow(net).get("CTL.Net", Boolean.class) != null) {
				boolean ctlnet = net.getRow(net).get("CTL.Net", Boolean.class);
				
				if(ctlnet) {
					return true;	
				}
			}
		}
		return false;
	}

	@Override
	public void handleEvent(SessionLoadedEvent arg0) {
	}
}
