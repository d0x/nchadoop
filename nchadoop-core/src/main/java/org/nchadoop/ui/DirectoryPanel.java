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

import org.nchadoop.fs.Directory;
import org.nchadoop.ui.listbox.DirectoryListBox;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;
 
@Data
@EqualsAndHashCode(callSuper = true)
public class DirectoryPanel extends Panel
{
	private DirectoryLabel		currentFolderLabel	= new DirectoryLabel();
	private DirectoryListBox	listBox				= new DirectoryListBox();
	private Directory			currentDirectory;

	public DirectoryPanel()
	{
		addComponent(this.currentFolderLabel, LinearLayout.MAXIMIZES_HORIZONTALLY);
		addComponent(this.listBox, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
	}

	public void updateDirectory(final MainWindow ncWindow, final Directory directory)
	{
		this.currentDirectory = directory;
		this.currentFolderLabel.updateDirectory(directory);
		this.listBox.updateDirectory(ncWindow, directory);
	}

}
