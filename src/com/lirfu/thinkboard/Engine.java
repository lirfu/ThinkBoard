package com.lirfu.thinkboard;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.lirfu.thinkboard.components.Block;
import com.lirfu.thinkboard.components.BoardComponent;
import com.lirfu.thinkboard.components.None;

public class Engine {
	private static MyTools selectedTool = MyTools.MOVE;
	private static String StorageFilename = "MyBoard.tb";
	
	public static void setCurrentTool(MyTools tool) {
		selectedTool = tool;
		Window.changeTool(tool);
	}
	
	public static String parseToolName(MyTools tool) {
		String name = "N/A";
		switch (tool) {
			case MOVE:
				name = "Move";
				break;
			case BLOCK:
				name = "Block";
				break;
			case LINE:
				name = "Line";
				break;
			case DELETE:
				name = "Delete";
				break;
		}
		return name;
	}
	
	public static BoardComponent useCurrentTool(String name, int x, int y) {
		BoardComponent component = null;
		
		switch (selectedTool) {
			case MOVE:
				component = new None(name, x, y);
				break;
			case BLOCK:
				component = new Block(name, x, y);
				break;
			case LINE:
//				component = new Line(name, null, null);
				break;
			case DELETE:
				break;
		}
		
		return component;
	}
	
	public static MyTools getCurrentTool() {
		return selectedTool;
	}
	
	public static enum MyTools {
		MOVE, BLOCK, LINE, DELETE
	}
	
	public static void saveCurrentBoard() {
		System.out.println("Saving board...");
		try {
			FileOutputStream output = new FileOutputStream(StorageFilename);
			ObjectOutputStream stream = new ObjectOutputStream(output);
			
			for (BoardComponent temp : Board.components) {
				stream.writeObject(temp);
			}
			stream.close();
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
		}
		
	}
	
	public static void loadCurrentBoard() {
		System.out.println("Loading board...");
		if (Board.components.size() == 0) {
			try {
				FileInputStream output = new FileInputStream(StorageFilename);
				ObjectInputStream stream = new ObjectInputStream(output);
				
				try {
					BoardComponent temp = null;
					while ((temp = (BoardComponent) stream.readObject()) != null) {
						Board.components.add(temp);
					}
				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
				} catch (EOFException e) {
//					e.printStackTrace();
				}
				stream.close();
			} catch (FileNotFoundException e) {
//				e.printStackTrace();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
	}
}
