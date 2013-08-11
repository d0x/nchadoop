package nchadoop.fs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;

public class DirectoryTest
{

	@Test
	public void shouldGetCorrectPath()
	{
		Directory root = new Directory(null, "fs://root");
		Directory child = root.addDirectory("child");
		Directory grandchild = child.addDirectory("grandchild");
		
		assertThat(grandchild.absolutDirectoryName(), is("fs://root/child/grandchild/"));
	}
	
	@Test
	public void shouldGetCorrectPathOnlyRoot()
	{
		Directory root = new Directory(null, "fs://root");
		assertThat(root.absolutDirectoryName(), is("fs://root/"));
	}
	
	@Test
	public void shouldFindDirectories()
	{
		Directory root = new Directory(null, "fs://root");
		Directory child1 = root.addDirectory("child1");
		Directory child2 = root.addDirectory("child2");
		
		root.directories.add(child1);
		root.directories.add(child2);
		
		assertThat(root.findDirectoryByName("child1"), is(child1));
		assertThat(root.findDirectoryByName("child2"), is(child2));
	}

}
