import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FileHelper {
	public static boolean exists(String filename) {
		File file = new File(filename);
		return file.exists() && !file.isDirectory();
	}

	public static String read(String filename) throws Exception {
		if (!exists(filename))
			return null;
		String data = "";
		var scanner = new Scanner(new File(filename), StandardCharsets.UTF_8);
		while (scanner.hasNextLine()) {
			data += scanner.nextLine() + "\n";
		}
		scanner.close();
		return data;
	}

	public static void create(String filename) throws Exception {
		new File(filename).createNewFile();
	}

	public static void write(String filename, String content) throws Exception {
		create(filename);
		var writer = new FileWriter(filename, StandardCharsets.UTF_8);
		writer.write(content);
		writer.close();
	}

	public static void append(String filename, String content) throws Exception {
		Files.writeString(Paths.get(filename), content + System.lineSeparator(), StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);
	}

	public static PrintWriter feed(String filename) throws Exception {
		FileWriter fw = new FileWriter(filename, StandardCharsets.UTF_8, true);
		BufferedWriter bw = new BufferedWriter(fw);
		return new PrintWriter(bw);
	}
}
