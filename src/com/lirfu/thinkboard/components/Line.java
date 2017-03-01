package com.lirfu.thinkboard.components;

import java.awt.Color;
import java.awt.Graphics;

import com.lirfu.thinkboard.Engine.MyTools;

public class Line extends BoardComponent {
	private boolean isThick;
	private final BoardComponent start, end;
	private final int offset = 15;
	private boolean showText = true;

	public Line(String name, BoardComponent start, BoardComponent end) {
		super(name, start.getX(), start.getY(), MyTools.LINE);
		this.start = start;
		this.end = end;
	}

	public void setThickness(boolean thick) {
		this.isThick = thick;
	}

	public boolean isThick() {
		return isThick;
	}

	@Override
	public void paintComponent(Graphics g) {
		int lineColor = 0xFF0066FF;
		int lineTextColor = 0xFF0000FF;

		g.setColor(Color.decode(getColor()));
		for (int i = 0; i < 4 && isThick; i++)
			g.drawLine(start.getX(), start.getY() - i, end.getX(), end.getY() - i);
		g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
		g.setColor(Color.LIGHT_GRAY);
		//		int offx = start.getX() < end.getX() ? -offset : +offset;
		//		int offy = start.getY() < end.getY() ? -offset : +offset;
		//		g.drawRect((start.getX() + end.getX()) / 2 - offx / 2, (start.getY() + end.getY()) / 2 - offy / 2,
		//				(start.getX() < end.getX() ? offset : -offset), (start.getY() < end.getY() ? offset : -offset));
		g.drawRect((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2, offset, offset);
		if (showText) {
			g.setColor(Color.decode(String.valueOf(lineTextColor)));
			g.drawString(getName(), (start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
		}
	}

	@Override
	/**
	 * Uses a square in center of the line where text starts.
	 */
	public boolean containsCoordinate(int x, int y) {
		if (x >= (start.getX() + end.getX()) / 2 && x <= (start.getX() + end.getX()) / 2 + offset) {
			if (y >= (start.getY() + end.getY()) / 2 && y <= (start.getY() + end.getY()) / 2 + offset) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDetails() {
		return "\nName: " + getName() + "\nLine from " + start.getName() + " to " + end.getName() + ".";
	}

	@Override
	public boolean equals(Object o) {
		return ((Line) o).getName().contains(start.getName()) && ((Line) o).getName().contains(end.getName());
	}

	public BoardComponent[] getBothEnds() {
		return new BoardComponent[] { start, end };
	}

	/**
	 * Choose to show the line name.
	 * 
	 * @param show
	 *            True shows text, false hides text.
	 */
	public void setShowText(boolean show) {
		this.showText = show;
	}

}
