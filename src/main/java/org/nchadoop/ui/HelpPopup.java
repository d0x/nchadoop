package org.nchadoop.ui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.input.Key;

@Data
@EqualsAndHashCode(callSuper = true)
public class HelpPopup extends Window
{
    private final GUIScreen guiScreen;

    public HelpPopup(final GUIScreen guiScreen) throws IOException, URISyntaxException
    {
        super("Help");

        this.guiScreen = guiScreen;

        List<String> content = Files.readAllLines(Paths.get(HelpPopup.class.getClassLoader().getResource("helpPopup.txt").toURI()), Charset.forName("UTF8"));

        for (String line : content)
        {
            addComponent(new Label(line));
        }
    }

    public void show()
    {
        guiScreen.runInEventThread(new Action() {
            @Override
            public void doAction()
            {
                guiScreen.showWindow(HelpPopup.this, Position.CENTER);
            }
        });
    }

    @Override
    public void onKeyPressed(Key key)
    {
        close();
    }
}
