package ncdu;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

public class NcWindow extends Window
{

	public NcWindow(final Screen screen)
	{
		super("nchdfs");
		setBorder(new Border.Invisible());
		setSoloWindow(true);

		final ActionListBox actionListBox = new ActionListBox();

	}

	@Override
	public void onKeyPressed(final Key key)
	{
		if (key.getKind() == Key.Kind.Escape)
		{
			close();
			getOwner().getScreen().stopScreen();
		}
	}
}
