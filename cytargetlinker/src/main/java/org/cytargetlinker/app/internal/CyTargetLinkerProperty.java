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

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.cytoscape.property.CyProperty;
import org.cytoscape.property.SimpleCyProperty;
import org.cytoscape.session.CySession;
import org.cytoscape.session.CySessionManager;

// TODO: saving works within one session but not after restarting cytoscape
public class CyTargetLinkerProperty {

	public static String CTL_RIN_DIRECTORY = "";
	public static String CTL_RIN_DIRECTOY_PROP = "CTL.rin.directory";
	public static Properties CTL_PROP = new Properties();
	
	private CyProperty<Properties> ctlProperty;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CyProperty<Properties> checkCyProperties(CySessionManager cySessionManager) {
		CySession session = cySessionManager.getCurrentSession();
		
		if(session.equals(null))
			System.out.println("session null");

		//3. Get all properties and loop through to find your own.
		Set<CyProperty<?>> props = new HashSet<CyProperty<?>>();
		props = session.getProperties();
		if(props.equals(null))
			System.out.println("props null");
		boolean flag = false;

		for (CyProperty<?> prop : props) {
		    if (prop.getName() != null){
		    	if (prop.getName().equals(CTL_RIN_DIRECTOY_PROP)) {
		    		ctlProperty = (CyProperty<Properties>) prop;
		        flag = true;
		        break;
		    	}
		    }
		}

		//4. If the property does not exists, create nodeBorderWidthProperty
		if (!flag)
		{
			CTL_PROP.setProperty(CTL_RIN_DIRECTOY_PROP, CTL_RIN_DIRECTORY);
			ctlProperty = new SimpleCyProperty("CyTargetLinker", CTL_PROP, String.class, CyProperty.SavePolicy.CONFIG_DIR);
			System.out.println("create new property");
		}
		//5. If not null, property exists, get value from it and set NodeBorderWidthInPathsValue
		else
		{
			CTL_PROP = ctlProperty.getProperties();
			CTL_RIN_DIRECTORY = (String)CTL_PROP.get(CTL_RIN_DIRECTOY_PROP);
		}
		return ctlProperty;
	}
	
	public static void updateProperty(String directory) {
		CTL_RIN_DIRECTORY = directory;
		CTL_PROP.setProperty(CTL_RIN_DIRECTOY_PROP, CTL_RIN_DIRECTORY);
	}
}
