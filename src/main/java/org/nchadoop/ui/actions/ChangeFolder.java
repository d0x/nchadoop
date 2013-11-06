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
package org.nchadoop.ui.actions;

import org.nchadoop.fs.Directory;
import org.nchadoop.ui.MainWindow;

import com.googlecode.lanterna.gui.Action;

public class ChangeFolder implements Action
{

    private Directory  folder;
    private MainWindow ncWindow;

    public ChangeFolder(final Directory folder, final MainWindow ncWindow)
    {
        this.folder = folder;
        this.ncWindow = ncWindow;
    }

    @Override
    public void doAction()
    {
        this.ncWindow.changeFolder(this.folder);
    }

}
