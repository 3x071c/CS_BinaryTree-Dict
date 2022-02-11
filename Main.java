import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.io.Console;
import java.util.Scanner;

public final class Main {
	private static Console console = System.console();

	private final static void log(String msg) {
		System.out.println(msg);
	}

	private final static void log(String msg, boolean heading) {
		if (!heading) {
			log(msg);
			return;
		}
		var size = 120;
		var padding = "=".repeat(size);
		var margin = "-".repeat((int) ((size - 2 - msg.length()) / 2));
		var spacing = margin.length() > 0 ? " " : "";
		log(padding);
		log(margin + spacing + msg + spacing + margin);
		log(padding);
	}

	private static String promptString(String prompt) {
		if (console != null) {
			return console.readLine(prompt + ": ");
		} else { // BlueJ has no proper console
			System.out.print(prompt + ": ");
			var scanner = new Scanner(System.in);
			var input = scanner.nextLine();
			scanner.close();
			return input;
		}
	}

	private static boolean promptBoolean(String prompt) {
		var input = "";
		while (!input.matches("^[Yy](?:es)?$") && !input.matches("^[Nn]o?$")) {
			input = promptString(prompt + " (Y/n)");
		}
		return input.matches("^[Yy](?:es)?$");
	}

	private static boolean tryParseInt(String value) {
		if (value.isBlank())
			return false;
		try {
			return Integer.parseInt(value) >= 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static int promptInt(String prompt) {
		var input = "";
		while (!tryParseInt(input)) {
			input = promptString(prompt + " (Number)");
		}
		return Integer.parseInt(input);
	}

	private final static String found(Boolean success) {
		return success ? "Found it!" : "Didn't find it :(";
	}

	private final static String parseTranslation(String response) {
		var regexp = Pattern.compile("\\\"translatedText\\\":\\\"(?<translation>.*?)\\\"").matcher(response);
		if (!regexp.find())
			return null;
		return regexp.group("translation");
	}

	private final static String parseDefinition(String response) {
		var regexp = Pattern.compile("\\\"definition\\\":\\\"(?<definition>.*?)\\\"").matcher(response);
		if (!regexp.find())
			return null;
		return regexp.group("definition");
	}

	private final static ArrayList<String> clean(String[] array) {
		var cleaned = new ArrayList<String>(Arrays.asList(array));
		cleaned.removeAll(Arrays.asList("", null));
		return cleaned;
	}

	private final static String replaceWord(String text, String orig, String translated) {
		return text.replaceAll(
				"(?<=[.,\\/#!$%\\^&\\*;:{}=\\-_~()?\" ])" + orig + "(?=[.,\\/#!$%\\^&\\*;:{}=\\-_~()?\" ])",
				"|||" + translated + "|||"); // (?i) case-insensitive
	}

	private final static String truncate(String text, int maxLength) {
		return text.substring(0, Math.min(text.length(), maxLength)) + (text.length() > 100 ? "..." : "");
	}

	private final static Translation translate(String query) throws Exception {
		var queryTranslation = parseTranslation(
				HttpHelper.post("https://libretranslate.com/translate", "q=" + query + "&source=en&target=de"));
		if (queryTranslation == null)
			queryTranslation = query;
		var queryDefinition = parseDefinition(
				HttpHelper.get("https://api.dictionaryapi.dev/api/v2/entries/en_US/" + query, ""));
		if (queryDefinition == null)
			queryDefinition = query;
		return new Translation(queryTranslation, queryDefinition);
	}

	public final static void main(String[] args) throws Exception {
		log("Binary Tree", true);
		log("Initializing binary tree with values (in order): 15, 5, 16, 3, 12, 20, 10, 13, 18, 23, 6, 7");
		var tree = new BinaryTree<Integer>(Integer::compareTo, 15, 5, 16, 3, 12, 20, 10, 13, 18, 23, 6, 7);
		log("Traversal (Preorder): " + tree.toPreOrder());
		log("Traversal (Inorder): " + tree.toInOrder());
		log("Traversal (Postorder): " + tree.toPostOrder());
		log("Find 12: " + found(tree.find(12)));
		log("Find 3: " + found(tree.find(3)));
		log("Find 24: " + found(tree.find(24)));
		log("Find 23: " + found(tree.find(23)));
		log("Adding 24 to binary tree");
		tree.add(24);
		log("Find 24 (again): " + found(tree.find(24)));
		log("Dictionary Tree | B.S. 72/2", true);
		log("Initializing dictionary");
		var dict = new DictionaryTree<String, Translation>(String::compareToIgnoreCase);
		log("Reading foreign text from Text.txt");
		var sourceText = FileHelper.read("Text.txt");
		var sourceWords = new HashSet<String>(
				clean(sourceText.replaceAll("[.,\\/#!$%\\^&\\*;:{}=\\-_~()?\"]", "").split("\\s+"))); // [A-Za-z]*[^A-Za-z\\s]+[A-Za-z]*
		var translatedText = " " + sourceText + " ";
		log("Fetching cached translations from cache.txt");
		var translatedCache = FileHelper.read("cache.txt");
		if (translatedCache != null && !translatedCache.isBlank()) {
			for (String translation : clean(translatedCache.split("\\R+"))) {
				var translatedParts = translation.split(Pattern.quote("|||"));
				if (translatedParts.length != 3)
					throw new Exception("Unexpected cache value; delete cache.txt");
				dict.add(translatedParts[0], new Translation(translatedParts[1], translatedParts[2]));
				sourceWords.remove(translatedParts[0]);
				translatedText = replaceWord(translatedText, translatedParts[0], translatedParts[1]);
			}
		}
		if (promptBoolean("Dictionary now contains " + dict.length() + " entries (" + sourceWords.size()
				+ " missing to completely translate text). Do you want to retrieve more translations from the internet")) {
			var requestCount = promptInt("How many translations do you want to request? 0 for all words or");
			if (requestCount == 0)
				requestCount = sourceWords.size();
			var requestDelay = promptInt(
					"How many seconds do you want to wait between individual requests? Setting a too low delay can result in an IP block. 1 (recommendation) or");
			log("Translating/Defining over the internet - This will take around "
					+ (int) Math.ceil(requestCount * requestDelay / 60.0) + " minute(s)");
			Object thread = new Object();
			var i = 0;
			var out = FileHelper.feed("cache.txt");
			synchronized (thread) {
				for (String sourceWord : sourceWords) {
					if (i >= requestCount)
						break;
					var translation = translate(sourceWord);
					dict.add(sourceWord, translation);
					out.println(sourceWord + "|||" + translation.word + "|||" + translation.definition);
					translatedText = replaceWord(translatedText, sourceWord, translation.word);
					out.flush();
					i += 1;
					thread.wait(requestDelay * 1000);
				}
			}
			out.close();
		}
		log("Translated text:");
		translatedText = translatedText.replace("|||", "");
		translatedText = translatedText.substring(1, translatedText.length() - 1);
		log(truncate(translatedText, 100));
		FileHelper.write("Translated.txt", translatedText);
		log("Dumped translated text to Translated.txt");
		log("Dictionary lookup", true);
		while (promptBoolean("Do you want to search the dictionary for an english word")) {
			var query = promptString("Enter search query");
			var translation = dict.get(query);
			if (translation == null)
				translation = new Translation(query, query);
			log("Query: \"" + query + "\"");
			if (!translation.word.equalsIgnoreCase(query)) {
				log("Found translation: \"" + translation.word + "\"!");
			} else {
				log("No translation found");
				if (promptBoolean("Do you want to retrieve the data from the internet")) {
					translation = translate(query);
					dict.add(query, translation);
					FileHelper.append("cache.txt", query + "|||" + translation.word + "|||" + translation.definition);
					if (!translation.word.equalsIgnoreCase(query)) {
						log("Found translation: \"" + translation.word + "\"!");
					} else {
						log("No translation found");
					}
				}
			}
			if (!translation.definition.equalsIgnoreCase(query)) {
				log("Found definition: \"" + translation.definition + "\"!");
			} else {
				log("No definition found");
			}
		}
		log("Ok, goodbye!");
		log("Done :)", true);
	}
}
