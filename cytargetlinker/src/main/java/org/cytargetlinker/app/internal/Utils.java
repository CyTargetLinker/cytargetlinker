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

import java.util.Collection;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;

public class Utils {

	public static void updateVisualStyle(Plugin plugin, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(network);
		plugin.getVisualMappingManager().setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}


	public static CyNetworkView getNetworkView(CyNetwork network, Plugin plugin) {
		Collection<CyNetworkView> views = plugin.getCyNetworkViewManager().getNetworkViews(network);
		CyNetworkView view;
		if(!views.isEmpty()) {
			view = views.iterator().next();
		} else {
			view = plugin.getNetworkViewFactory().createNetworkView(network);
			plugin.getCyNetworkViewManager().addNetworkView(view);
		}
		return view;
	}
}
