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
package org.cytargetlinker.app.internal.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Result;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.ParserAdapter;

/**
 * 
 * @author mkutmon
 * sets up the cytargetlinker parser to extract interactions from one datasource file
 *
 */
public class XgmmlParser {
	
	public Result parseXgmmlFile(File file, Set<String> nodeIds, Direction dir, LinkSet ds) {
		Result res = new Result();
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			ParserAdapter pa = new ParserAdapter(sp.getParser());
			CyTargetLinkerParser parser = new CyTargetLinkerParser(nodeIds, dir, ds);
			pa.setContentHandler(parser);
			pa.parse(new InputSource(new FileInputStream(file)));
			
			res.setLinkSetName(parser.getNetworkName());
			res.setDirection(dir);
			if(parser.getNetworkAttr().containsKey("url")) {
				res.setLinkSetURL(parser.getNetworkAttr().get("url"));
			}
			if(parser.getNetworkAttr().containsKey("type")) {
				res.setLinkSetType(parser.getNetworkAttr().get("type"));
			}
			
			res.getEdges().addAll(parser.getEdgeList());
			
			parser.clean();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}