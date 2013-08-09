package nchadoop.ui.actions;

import lombok.Data;

import org.apache.hadoop.fs.LocatedFileStatus;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.dialog.MessageBox;

@Data
public class ShowLocatedFileStatus implements Action
{
	private final GUIScreen			owner;
	private final LocatedFileStatus	file;

	@Override
	public void doAction()
	{
		MessageBox.showMessageBox(this.owner, this.file.getPath().toString(), this.file.toString());
	}

}
