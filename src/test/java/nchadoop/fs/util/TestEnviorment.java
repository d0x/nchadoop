package nchadoop.fs.util;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.Data;

@Data
public class TestEnviorment implements Closeable
{

	private Path	tempDirectory;

	public TestEnviorment(String fsDefinitionFile) throws IOException
	{
		tempDirectory = Files.createTempDirectory("unitTest");

		try (BufferedReader fsDefinition = new BufferedReader(new InputStreamReader(TestEnviorment.class.getResourceAsStream(fsDefinitionFile)));)
		{
			String definition;

			while ((definition = fsDefinition.readLine()) != null)
			{
				Item item = parseDefinition(definition);

				Path newPath = Paths.get(tempDirectory.toString(), item.path);
				Files.createDirectories(newPath.getParent());

				if (item.isFile)
				{
					Path createdFile = Files.createFile(newPath);

					OutputStream outputStream = Files.newOutputStream(createdFile);

					for (long i = 0; i < item.size; i++)
					{
						outputStream.write('∞');
					}

					outputStream.close();
				}
				else
				{
					Files.createDirectory(newPath);
				}

			}
		}
	}

	private static Item parseDefinition(String definition)
	{
		String[] split = definition.split("\t");

		return new Item(split[0].equalsIgnoreCase("f"), Long.parseLong(split[1]), split[2]);
	}

	@Data
	static class Item
	{
		private final boolean	isFile;
		private final long		size;
		private final String	path;
	}

	@Override
	public void close() throws IOException
	{
		try
		{
			Files.walkFileTree(tempDirectory, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
				{
					Files.delete(file);
					return CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
				{
					if (exc == null)
					{
						Files.delete(dir);
						return CONTINUE;
					}
					else
					{
						throw exc;
					}
				}

			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		Files.deleteIfExists(tempDirectory);
	}
}
