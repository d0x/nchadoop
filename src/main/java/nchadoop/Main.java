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
package nchadoop;

import java.io.IOException;
import java.net.URISyntaxException;

import nchadoop.cli.CliConfig;
import nchadoop.cli.Parser;
import nchadoop.fs.HdfsScanner;
import nchadoop.ui.MainWindow;
import nchadoop.ui.ScanningPopup;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;

public class Main
{
	public static void main(String... args) throws URISyntaxException, IOException, InterruptedException
	{
		CliConfig cliConfig = new Parser().parse(args);

		if (cliConfig == null)
		{
			return;
		}

		// Create components
		final HdfsScanner hdfsScanner = new HdfsScanner(cliConfig.getDir(), "hdfs");

		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		final MainWindow mainWindow = new MainWindow(guiScreen);
		final ScanningPopup scanningPopup = new ScanningPopup();

		final Controller controller = new Controller();

		// wire them
		mainWindow.setController(controller);

		scanningPopup.setGui(guiScreen);
		scanningPopup.setController(controller);

		controller.setGuiScreen(guiScreen);
		controller.setMainWindow(mainWindow);
		controller.setScanningPopup(scanningPopup);
		controller.setHdfsScanner(hdfsScanner);

		controller.startScan(cliConfig.getDir(), cliConfig.getFilter());
	}
}
