package org.cytargetlinker.app.internal.resources;

import java.util.List;

import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.Result;


public interface ExtensionHandler {

	public Result getNeighbours(List<String> ids, Direction dir);

	// TODO: interfaces don't allow static methods yet - fix when enabled
//	public DatasourceType getDataSourceType();
}
