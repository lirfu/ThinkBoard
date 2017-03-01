package com.lirfu.thinkboard.components;

import java.awt.Graphics;

import com.lirfu.thinkboard.Engine.MyTools;

public class None extends BoardComponent {
	public None(String name, int x, int y) {
		super(name, x, y, MyTools.MOVE);
	}
	
	@Override
	public void paintComponent(Graphics g) {
	}
	
	@Override
	public boolean containsCoordinate(int x, int y) {
		return false;
	}
	
	@Override
	public String getDetails() {
		return "";
	}
	
}
