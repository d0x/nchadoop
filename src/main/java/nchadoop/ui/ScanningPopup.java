package nchadoop.ui;

import lombok.Data;
import nchadoop.Controller;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.dialog.WaitingDialog;
import com.googlecode.lanterna.input.Key;

@Data
public class ScanningPopup extends WaitingDialog
{
	private GUIScreen	gui;
	private Controller	controller;

	public ScanningPopup()
	{
		super("Scanning...", "Please wait until the directories are scanned.");
	}

	public void show()
	{
		new Thread(new Runnable() {

			@Override
			public void run()
			{
				ScanningPopup.this.gui.showWindow(ScanningPopup.this, Position.CENTER);
			}
		}).start();
	}

	@Override
	public void onKeyPressed(Key key)
	{
		if (controller.handleGlobalKeyPressed(this, key) == false)
		{
			super.onKeyPressed(key);
		}
	}

	@Override
	public void close()
	{
		super.close();
		this.gui.invalidate();
	}
}
