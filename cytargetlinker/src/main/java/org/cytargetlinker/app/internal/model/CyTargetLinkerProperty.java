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
package org.cytargetlinker.app.internal.model;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.cytoscape.property.CyProperty;
import org.cytoscape.property.SimpleCyProperty;
import org.cytoscape.session.CySession;
import org.cytoscape.session.CySessionManager;

/**
 * 
 * @author mkutmon
 * saves the LinkSet directory which is used in the ExtensionDialog
 * TODO: saving works within one session but not after restarting Cytoscape!
 *
 */
public class CyTargetLinkerProperty {

	public static String CTL_LINKSET_DIR = "";
	public static String CTL_LINKSET_DIR_PROP = "CTL.rin.directory";
	public static Properties CTL_PROP = new Properties();
	
	private CyProperty<Properties> ctlProperty;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CyProperty<Properties> checkCyProperties(CySessionManager cySessionManager) {
		CySession session = cySessionManager.getCurrentSession();
		
		// check if cytargetlinker directory property already exists
		boolean flag = false;
		if(session != null) {
			Set<CyProperty<?>> props = new HashSet<CyProperty<?>>();
			props = session.getProperties();
			if(props != null) {
				for (CyProperty<?> prop : props) {
				    if (prop.getName() != null){
				    	if (prop.getName().equals(CTL_LINKSET_DIR_PROP)) {
				    		ctlProperty = (CyProperty<Properties>) prop;
				    		flag = true;
				    		break;
				    	}
				    }
				}
			}
		}

		if (!flag) {
			// if property does not exists yet = create new property
			CTL_PROP.setProperty(CTL_LINKSET_DIR_PROP, CTL_LINKSET_DIR);
			ctlProperty = new SimpleCyProperty("CyTargetLinker", CTL_PROP, String.class, CyProperty.SavePolicy.CONFIG_DIR);
		} else {
			// if property does exist - retrieve directory value
			CTL_PROP = ctlProperty.getProperties();
			CTL_LINKSET_DIR_PROP = (String)CTL_PROP.get(CTL_LINKSET_DIR_PROP);
		}
		return ctlProperty;
	}
	
	public static void updateProperty(String directory) {
		CTL_LINKSET_DIR_PROP = directory;
		CTL_PROP.setProperty(CTL_LINKSET_DIR_PROP, CTL_LINKSET_DIR);
	}
}
