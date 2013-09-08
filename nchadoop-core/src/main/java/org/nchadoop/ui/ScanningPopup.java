/*******************************************************************************
 * Copyright 2013 Christian Schneider
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.nchadoop.ui;

import java.net.URI;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.nchadoop.Controller;
import org.nchadoop.fs.SearchRoot;
import org.nchadoop.fs.HdfsScanner.StatusCallback;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.input.Key;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScanningPopup extends Window implements StatusCallback
{
	public static final String[]	SPINNER		= new String[]{"-", "\\", "|", "/"};

	private final GUIScreen			guiScreen;
	private Controller				controller;

	private final Thread			updateThread;

	private final Label				spinLabel	= new Label("-");
	private Label					fileLabel	= new Label("");
	private Label					totalLabel	= new Label("");

	private boolean					isClosed;

	private String					lastVisitedFile;
	private int						largestName	= 0;
	private long					totalSize	= 0;
	private long					fileCount	= 0;

	public ScanningPopup(final GUIScreen guiScreen)
	{
		super("Scanning...");

		this.guiScreen = guiScreen;

		final Panel panel = new Panel(Panel.Orientation.HORISONTAL);
		panel.addComponent(new Label("Please wait until the directories are scanned."));
		panel.addComponent(spinLabel);
		addComponent(panel);

		addComponent(fileLabel);
		addComponent(totalLabel);

		isClosed = false;
		updateThread = new Thread(new Update());
	}

	@Override
	protected void onVisible()
	{
		super.onVisible();
		updateThread.start();
	}

	public void show()
	{
		guiScreen.runInEventThread(new Action() {
			@Override
			public void doAction()
			{
				ScanningPopup.this.guiScreen.showWindow(ScanningPopup.this, Position.CENTER);
			}
		});
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
		isClosed = true;
		super.close();
		this.guiScreen.invalidate();
	}

	@Override
	public void onScanStarted(URI searchUri)
	{
		show();
	}

	@Override
	public void onVisitFile(FileStatus next)
	{
		this.lastVisitedFile = next.getPath().toString();
		totalSize += next.getLen();
		fileCount++;
	}

	@Override
	public void onScanFinished(SearchRoot searchRoot)
	{
		close();
	}

	private class Update implements Runnable
	{
		private int	currentSpinner	= 0;

		public void run()
		{
			while (!isClosed)
			{
				String displayFile = lastVisitedFile;

				if (displayFile != null)
				{
					if (displayFile.length() > largestName)
					{
						largestName = displayFile.length();
					}
					else
					{
						displayFile = StringUtils.rightPad(displayFile, largestName);
					}
				}
				else
				{
					displayFile = "";
				}

				final String preparedString = displayFile;

				final long fileCountToShow = fileCount;

				if (getOwner() != null)
				{
					getOwner().runInEventThread(new Action() {
						public void doAction()
						{
							fileLabel.setText(preparedString);
							spinLabel.setText(SPINNER[currentSpinner = ++currentSpinner % SPINNER.length]);
							totalLabel.setText("Files: " + fileCountToShow);
						}
					});
				}
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}
}
