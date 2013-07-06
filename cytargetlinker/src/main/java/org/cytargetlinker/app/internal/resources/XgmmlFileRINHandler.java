package org.cytargetlinker.app.internal.resources;

import java.io.File;
import java.util.List;

import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.DatasourceType;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Result;
import org.cytargetlinker.app.internal.io.XgmmlParser;

public class XgmmlFileRINHandler implements ExtensionHandler {

	private DataSource ds;
	
	public XgmmlFileRINHandler(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Result getNeighbours(List<String> ids, Direction dir) {
		File file = new File(ds.getSource());
		if(file.exists()) {
			try {
				XgmmlParser parser = new XgmmlParser();
				Result res = parser.parseXgmmlFile(file, ids, dir, ds);
				ds.setName(res.getRinName());
				res.setDs(ds);
				if(res.getEdges().size() > 0) {
					return res;
				} else {
					// TODO: warning!
					System.out.println("no edges from " + ds.getSource());
				}
				
			} catch (Exception e) {
				System.out.println("exception in " + ds.getSource());
				// TODO: warning!
			}
		} else {
			// TODO: warning
			System.out.println("file does not exist " + ds.getSource());
		}

		return null;
	}

	public static DatasourceType getDataSourceType() {
		return DatasourceType.XGMML_FILE;
	}

}
