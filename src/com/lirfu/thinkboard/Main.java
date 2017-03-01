package com.lirfu.thinkboard;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.lirfu.thinkboard.Engine.MyTools;

public class Main {
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					Window window = new Window();
					window.setLocation(20, 20);
					window.setVisible(true);
					window.setSize(600, 300);
					window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					
					Engine.setCurrentTool(MyTools.MOVE);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
