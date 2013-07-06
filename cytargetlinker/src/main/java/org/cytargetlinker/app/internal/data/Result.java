package org.cytargetlinker.app.internal.data;

import java.util.ArrayList;
import java.util.List;

public class Result {

	private DataSource ds;
	private String rinName;
	private String rinUrl;
	private String rinType;
	
	private Direction dir;

	private List<Edge> edges;
	
	public Result() {
		edges = new ArrayList<Edge>();
	}
	
	public String getRinName() {
		return rinName;
	}
	public void setRinName(String rinName) {
		this.rinName = rinName;
	}
	public String getRinUrl() {
		return rinUrl;
	}
	public void setRinUrl(String rinUrl) {
		this.rinUrl = rinUrl;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public String getRinType() {
		return rinType;
	}
	public void setRinType(String rinType) {
		this.rinType = rinType;
	}
	public Direction getDir() {
		return dir;
	}
	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public DataSource getDs() {
		return ds;
	}
	public void setDs(DataSource ds) {
		this.ds = ds;
	}
}
