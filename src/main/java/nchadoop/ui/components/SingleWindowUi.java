package nchadoop.ui.components;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.BorderLayout;
import com.googlecode.lanterna.screen.Screen;

public class SingleWindowUi extends Window
{
	protected GUIScreen	gui;
	protected Screen	screen;
	protected Panel		contentPane;

	public SingleWindowUi(final GUIScreen gui)
	{
		super(null);

		this.gui = gui;

		this.screen = this.gui.getScreen();
		this.screen.startScreen();

		setBorder(new Border.Invisible());
//		this.window.setSoloWindow(true);

		this.contentPane = new Panel();
//		this.contentPane.setBorder(new Border.Invisible());
		this.contentPane.setLayoutManager(new BorderLayout());
	}

}
