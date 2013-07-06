package org.cytargetlinker.app.internal.data;

import java.awt.Color;

public class DataSource {

	private DatasourceType type;
	private String source;
	private String name;
	private Color color;
	
	public DataSource(DatasourceType type, String source) {
		super();
		this.type = type;
		this.source = source;
	}
	
	public DatasourceType getType() {
		return type;
	}

	public String getSource() {
		return source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
