package com.lirfu.thinkboard.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.LinkedList;

import com.lirfu.thinkboard.Engine.MyTools;

public class Block extends BoardComponent {
	private final int standardSize = 50;

	public Block(String name, int x, int y) {
		super(name, x, y, MyTools.BLOCK);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.decode(getColor()));
		//		g.fillRect(getX() - standardSize / 2, getY() - standardSize / 2, standardSize, standardSize);
		g.fillOval(getX() - standardSize / 2, getY() - standardSize / 2, standardSize, standardSize);
		g.setColor(Color.DARK_GRAY);
		//		g.drawRect(getX() - standardSize / 2, getY() - standardSize / 2, standardSize, standardSize);
		g.drawOval(getX() - standardSize / 2, getY() - standardSize / 2, standardSize, standardSize);
		g.setColor(Color.BLACK);
		g.drawString(getName(), getX() - standardSize / 2 + 5, getY());
	}

	@Override
	public boolean containsCoordinate(int x, int y) {
		boolean t = false;

		if (x >= getX() - standardSize / 2 && x <= getX() + standardSize / 2 && y >= getY() - standardSize / 2 && y <= getY() + standardSize / 2)
			t = true;

		return t;
	}

	@Override
	public String getDetails() {
		String str = "Name: " + getName() + "\nConnected to:\n";
		LinkedList<String> list = new LinkedList<>();

		for (String temp : getConnections()) {
			list.add(temp);
		}

		list.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		for (String temp : list)
			str += " -" + temp + "\n";
		return str;
	}

}
