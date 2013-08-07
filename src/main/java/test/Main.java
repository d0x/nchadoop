package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lombok.extern.slf4j.Slf4j;
import ncdu.Utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

@Slf4j
public class Main
{
//	public static final String namenode = "hdfs://192.168.1.218:8020";
	public static final String	namenode	= "hdfs://carolin:8020";

	public static void main(final String[] args) throws IOException, InterruptedException, URISyntaxException
	{
		final FileSystem fileSystem = FileSystem.get(new URI(namenode), new Configuration(), "hdfs");

		final RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/"), true);

		final ArrayList<LocatedFileStatus> arrayList = new ArrayList<>();

		while (listFiles.hasNext())
		{
			final LocatedFileStatus file = listFiles.next();
			arrayList.add(file);
		}

		Collections.sort(arrayList, new Comparator<LocatedFileStatus>() {

			@Override
			public int compare(final LocatedFileStatus o1, final LocatedFileStatus o2)
			{
				return (int) (o2.getLen() - o1.getLen());
			}
		});

		final long max = arrayList.get(0).getLen();

		for (final LocatedFileStatus file : arrayList)
		{
			final long dashes = Math.round((double) file.getLen() / max * 100 / 10);
			System.out.print("[");
			for (int i = 0; i < 10; i++)
			{
				if (i < dashes)
					System.out.print("-");
				else
					System.out.print(" ");
			}
			System.out.println("] " + Utils.readableFileSize(file.getLen()) + " " + file.getPath());
		}
	}

}
