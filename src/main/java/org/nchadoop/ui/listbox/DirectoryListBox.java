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
package org.nchadoop.ui.listbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.nchadoop.fs.Directory;
import org.nchadoop.ui.MainWindow;

import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.gui.component.AbstractListBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.TerminalPosition;

public class DirectoryListBox extends AbstractListBox
{
    private long maximumSize;

    public DirectoryListBox()
    {
        super(null);
    }

    public void updateDirectory(final MainWindow ncWindow, final Directory directory)
    {
        clearItems();
        if (directory == null)
        {
            return;
        }

        final List<Displayable> items = new ArrayList<>();

        for (final Directory folder : directory.getDirectories())
        {
            items.add(new Displayable(ncWindow, directory.getParent(), folder, "/" + folder.getName(), folder.getSize(), folder));
        }

        for (final FileStatus file : directory.getFiles())
        {
            items.add(new Displayable(ncWindow, directory.getParent(), null, file.getPath().getName(), file.getLen(), file));
        }

        Collections.sort(items);

        if (!items.isEmpty())
        {
            this.maximumSize = items.get(0).getSize();
        }

        if (!directory.isRoot())
        {
            items.add(0, new Displayable(ncWindow, directory.getParent(), directory.getParent(), "/..", -1, null));
        }

        for (final Displayable displayable : items)
        {
            addItem(displayable);
        }

        if (directory.isRoot())
        {
            setSelectedItem(0);
        }
        else
        {
            if (getNrOfItems() > 1)
            {
                setSelectedItem(1);
            }
        }
    }

    @Override
    protected void printItem(final TextGraphics graphics, final int x, final int y, final int index)
    {
        final Displayable item = (Displayable) getItemAt(index);

        final String text = item.formatDisplayText(graphics.getWidth(), this.maximumSize);

        graphics.drawString(x, y, text);
    }

    @Override
    public Result keyboardInteraction(Key key)
    {
        final Displayable selectedItem = (Displayable) getSelectedItem();

        switch (key.getKind())
        {
            case Enter:
            case ArrowRight:
                selectedItem.navigate();
                return Result.EVENT_HANDLED;

            case ArrowLeft:
                selectedItem.navigateToParent();
                return Result.EVENT_HANDLED;

            default:
                break;
        }

        switch (key.getCharacter())
        {
            case 'l':
                return super.keyboardInteraction(new Key(Kind.ArrowRight));

            case 'h':
                return super.keyboardInteraction(new Key(Kind.ArrowLeft));

            case 'k':
                return super.keyboardInteraction(new Key(Kind.ArrowUp));

            case 'j':
                return super.keyboardInteraction(new Key(Kind.ArrowDown));

            default:
                break;
        }

        return super.keyboardInteraction(key);
    }

    @Override
    protected Theme.Definition getListItemThemeDefinition(final Theme theme)
    {
        return theme.getDefinition(Theme.Category.SCREEN_BACKGROUND);
    }

    @Override
    protected Theme.Definition getSelectedListItemThemeDefinition(final Theme theme)
    {
        return theme.getDefinition(Theme.Category.TEXTBOX_FOCUSED);
    }

    @Override
    public TerminalPosition getHotspot()
    {
        return null; // No hotspot
    }

    @Override
    protected String createItemString(final int index)
    {
        // We don't create those strings
        return "";
    }

}
