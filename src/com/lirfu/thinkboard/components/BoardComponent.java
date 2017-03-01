package com.lirfu.thinkboard.components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.LinkedList;

import com.lirfu.thinkboard.Engine.MyTools;

public abstract class BoardComponent implements Serializable {
	private String name, description = "";
	private int x, y;
	private MyTools type;
	private final LinkedList<String> connectedTo;
	private String colorString = "0xaaaaaa";

	public BoardComponent(String name, int x, int y, MyTools type) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.type = type;
		this.connectedTo = new LinkedList<>();
	}

	//// ----- Abstracts:
	/**
	 * Used to achieve ability of selecting of the component.
	 * 
	 * @param x
	 *            coordinate.
	 * @param y
	 *            coordinate.
	 * @return Are given coordinates pointing on the element (<code>true</code>)
	 *         or on empty space (<code>false</code>).
	 */
	public abstract boolean containsCoordinate(int x, int y);

	public abstract void paintComponent(Graphics g);

	/**
	 * Gets all the details of a component.
	 * 
	 * @return Formatted string ready for output.
	 */
	public abstract String getDetails();

	//// ----- Specials:
	public void addConnection(String destination) {
		if (!this.connectedTo.contains(destination))
			this.connectedTo.add(destination);
	}

	public boolean removeConnection(String destination) {
		return this.connectedTo.remove(destination);
	}

	public String[] getConnections() {
		return this.connectedTo.toArray(new String[] {});
	}

	@Override
	public boolean equals(Object obj) {
		return ((BoardComponent) obj).getName().matches(this.name);
	}

	//// ----- Getters&setters:
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public MyTools getType() {
		return type;
	}

	protected void setType(MyTools type) {
		this.type = type;
	}

	public String getColor() {
		return this.colorString;
	}

	public void setColor(String colorString) {
		try {
			Color.decode(colorString);
			this.colorString = colorString;
		} catch (NumberFormatException e) {
			this.colorString = "0xaaaaaa";
		}

	}
}
