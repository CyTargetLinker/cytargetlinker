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

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.values.ArrowShape;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

/**
 * 
 * @author mkutmon
 * creates the cytargetlinker visual style
 *
 */
public class VisualStyleCreator {

	private CTLManager manager;
	private VisualStyle vs;
	
	public VisualStyleCreator(CTLManager manager) {
		this.manager = manager;
		for(VisualStyle style : manager.getVisualMappingManager().getAllVisualStyles()) {
			if(style.getTitle().equals("CyTargetLinker")) {
				vs = style;
			}
		}
		if(vs == null) {
			vs = manager.getVisualStyleFactory().createVisualStyle("CyTargetLinker");
			manager.getVisualMappingManager().addVisualStyle(vs);
		}
	}
	
	private CyNetwork network;
	
	public VisualStyle getVisualStyle(CyNetwork network) {
		this.network = network;
		DiscreteMapping<String, Paint> nodeColorMapping = getNodeColor();
				
		vs.addVisualMappingFunction(nodeColorMapping);
		vs.addVisualMappingFunction(getArrowShape());
		vs.addVisualMappingFunction(getEdgeColor());
		vs.addVisualMappingFunction(getNodeLabelMapping());
		
		vs.setDefaultValue(BasicVisualLexicon.NODE_FILL_COLOR, new Color(211,211,211));
		 
		return vs;
	
	}
	
	private PassthroughMapping<String, String> getNodeLabelMapping() {
		PassthroughMapping<String, String> mapping;
		mapping = (PassthroughMapping<String, String>) manager.getVisualMappingFunctionFactoryPassthrough().createVisualMappingFunction("CTL.label", String.class, BasicVisualLexicon.NODE_LABEL);
		return mapping;
	}
	
	// TODO: change mapping based on interaction and make sure interaction is not empty in the LinkSets!
	private DiscreteMapping<String, Paint> getEdgeColor() {
		Class<String> dataType = String.class;
		DiscreteMapping<String, Paint> edgeColorMapper = (DiscreteMapping<String, Paint>) manager.getVisualMappingFunctionFactoryDiscrete().createVisualMappingFunction("CTL.LinkSet", dataType, BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT);		
		ExtensionManager mgr = manager.getExtensionManager(network);
		for(LinkSet ls : mgr.getLinkSets()) {
			edgeColorMapper.putMapValue(ls.getName(), ls.getColor());
		}
	        
	    return edgeColorMapper;
	}

	// TODO: change mapping based on interaction and make sure interaction is not empty in the LinkSets!
	private DiscreteMapping<String, ArrowShape> getArrowShape() {
		Class<String> dataType = String.class;
		DiscreteMapping<String, ArrowShape> arrowShapeMapper = (DiscreteMapping<String, ArrowShape>) manager.getVisualMappingFunctionFactoryDiscrete().createVisualMappingFunction("CTL.LinkSet", dataType, BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE);
		
		ExtensionManager mgr = manager.getExtensionManager(network);
		for(LinkSet ls : mgr.getLinkSets()) {
			arrowShapeMapper.putMapValue(ls.getName(), ArrowShapeVisualProperty.ARROW);
		}
		
        arrowShapeMapper.putMapValue("pp", ArrowShapeVisualProperty.CIRCLE);
        arrowShapeMapper.putMapValue("interaction", ArrowShapeVisualProperty.ARROW);
        arrowShapeMapper.putMapValue("Line, Arrow", ArrowShapeVisualProperty.ARROW);
        arrowShapeMapper.putMapValue("Line, TBar", ArrowShapeVisualProperty.T);
        arrowShapeMapper.putMapValue("group-connection", ArrowShapeVisualProperty.NONE);
        
        return arrowShapeMapper;
	}
	
	private DiscreteMapping<String, Paint> getNodeColor() {
		NodeColorSet ncSet = new NodeColorSet();
		String ctrAttr = "CTL.Type";
		Class<String> dataType = String.class; 
		
		DiscreteMapping<String, Paint> dMapping = (DiscreteMapping<String, Paint>) manager.getVisualMappingFunctionFactoryDiscrete().createVisualMappingFunction(ctrAttr, dataType, BasicVisualLexicon.NODE_FILL_COLOR);
		
		List<String> types = new ArrayList<String>();
		for(CyNode node : network.getNodeList()) {
			String type = network.getRow(node).get("CTL.Type", String.class);
			if(!ncSet.getNodeColorMap().containsKey(type)) {
				types.add(type);
			}
		}
		
		for(String s : ncSet.getNodeColorMap().keySet()) {
			dMapping.putMapValue(s, ncSet.getColor(s));
		}
		for(int i = 0; i < types.size(); i++) {
			dMapping.putMapValue(types.get(i), ncSet.getColor(i));
		}
		
		return dMapping;
	}
}
