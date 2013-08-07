package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lombok.extern.slf4j.Slf4j;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.gui.Border.Bevel;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

@Slf4j
public class Main
{

	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
	{
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.1.218:8020"), new Configuration(), "hdfs");

		RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/"), true);

		ArrayList<LocatedFileStatus> arrayList = new ArrayList<>();

		while (listFiles.hasNext())
		{
			LocatedFileStatus file = listFiles.next();
			arrayList.add(file);
		}

		Collections.sort(arrayList, new Comparator<LocatedFileStatus>() {

			@Override
			public int compare(LocatedFileStatus o1, LocatedFileStatus o2)
			{
				return (int) (o2.getLen() - o1.getLen());
			}
		});

		
		long max = arrayList.get(0).getLen();
		
		for (LocatedFileStatus file : arrayList)
		{
			long dashes = Math.round((double)file.getLen()/max*100/10);
			System.out.print("[");
			for (int i = 0; i < 10; i++)
			{
				if(i<dashes)
					System.out.print("-");
				else
					System.out.print(" ");
			}
			System.out.println("] " + file.getPath());
		}
	}

}
