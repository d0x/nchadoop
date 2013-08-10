package nchadoop.fs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import nchadoop.fs.util.TestEnviorment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HdfsScannerTest
{
	URI					namenode;
	HdfsScanner			cut;
	String				pwd;
	SearchRoot			refresh;
	TestEnviorment	testFolder;

	@Before
	public void setup() throws URISyntaxException, IOException, InterruptedException
	{
		testFolder = new TestEnviorment("/simple.fs");

		this.namenode = new URI("file://" + testFolder.getTempDirectory().toString());

		this.cut = new HdfsScanner(namenode, "hdfs");

		this.refresh = cut.refresh(namenode);
	}

	@After
	public void tearDown() throws IOException
	{
		testFolder.close();
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
