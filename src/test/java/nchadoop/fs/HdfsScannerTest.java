package nchadoop.fs;



import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class HdfsScannerTest
{
	URI			namenode;
	HdfsScanner	cut;
	String		pwd;
	SearchRoot	refresh;

	@Before
	public void setup() throws URISyntaxException, IOException, InterruptedException
	{
		pwd = new File("").getAbsolutePath();

		this.namenode = new URI("file://" + pwd + "/src/test/resources/fakeFileSystems/");

		System.out.println(namenode);
		this.cut = new HdfsScanner(namenode, "hdfs");

		this.refresh = cut.refresh(namenode);
	}

	@Test
	public void shouldCountAllFiles() throws FileNotFoundException, IOException
	{
		assertThat(refresh.getTotalFiles(), is(3L));
	}
	
	@Test
	public void shouldSumTotalDiskUsage()
	{
		assertThat(refresh.getTotalDiskUsage(), is(26L));
	}
	
	@Test
	public void shouldSumRootFolder()
	{
		assertThat(refresh.getSize(), is(26L));
	}
	
	@Test
	public void shouldSumChildFolder()
	{
		assertThat(refresh.getDirectories().get(0).getSize(), is(26L));
	}
	
	@Test
	public void shouldStartAtSearchFolder()
	{
		List<Directory> directories = refresh.getDirectories();
		assertThat(directories.size(), is(1));
		
		Directory directory = directories.get(0);
		
		assertThat(directory.getName(), equalTo("simple"));
		
	}
	
}
