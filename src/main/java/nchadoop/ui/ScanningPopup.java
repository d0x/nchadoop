package nchadoop.ui;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Label;

public class ScanningPopup extends Window
{
	private GUIScreen	gui;

	public ScanningPopup(final GUIScreen gui)
	{
		super("Scanning...");

		this.gui = gui;

		addComponent(new Label("Please wait until the directories are scanned."));
	}

	public void show()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				ScanningPopup.this.gui.showWindow(ScanningPopup.this, Position.CENTER);
			}
		}, "ui-scanPopup").start();
	}

	@Override
	public void close()
	{
		super.close();
		this.gui.invalidate();
	}
}
