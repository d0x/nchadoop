package com.googlecode.lanterna.gui;

import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.dialog.WaitingDialog;

public class SimpleController
{

	public static void main(String[] args) throws InterruptedException
	{
		// Construct the screen
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		guiScreen.getScreen().startScreen();

		final Window main = new Window("Main");
		final WaitingDialog waitingDialog = new WaitingDialog("Loading...", "Standby");

		// Spawn the main window in a own thread. Otherwise our Controller blocks.
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				guiScreen.showWindow(main, Position.FULL_SCREEN);
			}
		}).start();

		// Show the waiting popup. For this we can use the event thread (i thought)
		guiScreen.runInEventThread(new Action() {

			@Override
			public void doAction()
			{
				guiScreen.showWindow(waitingDialog);
			}
		});

		// heavy calculation :)
		TimeUnit.SECONDS.sleep(2);

		// loading is done. close the popup now.
		System.out.println("Close loading popup");

		waitingDialog.close();
		guiScreen.invalidate();

		// this code will never reached.
	}
}
