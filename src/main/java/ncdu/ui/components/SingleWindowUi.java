package ncdu.ui.components;

import com.googlecode.lanterna.TerminalFacade;
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

	public SingleWindowUi()
	{
		super(null);

		this.gui = TerminalFacade.createGUIScreen();

		this.screen = this.gui.getScreen();
		this.screen.startScreen();

		setBorder(new Border.Invisible());
//		this.window.setSoloWindow(true);

		this.contentPane = new Panel();
//		this.contentPane.setBorder(new Border.Invisible());
		this.contentPane.setLayoutManager(new BorderLayout());
	}

	@Override
	public void close()
	{
		super.close();
		this.screen.stopScreen();
	}

}
