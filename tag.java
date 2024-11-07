import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class tag {
	public static void main(String[] args) {
		testByList(new ArrayList<String>(Arrays.asList(
				"abc",
				"tag def",
				"tag\\\"\\",
				"\"tag\\\"\\",
				"\"")));
		testByString("#\" ");
	}

	static void testByString(String str) {
		System.out.println(str);
		final var result = Parser.parse(str);
		System.out.println(result);
		final var eq = Parser.build(result);
		System.out.println(eq);
		final var eqResult = Parser.parse(eq);
		System.out.println(eqResult);

		assert (result.size() == eqResult.size()) : "not equal";
		for (int i = 0; i < result.size(); i++) {
			assert (result.get(i).equals(eqResult.get(i))) : "not equal";
		}
	}

	static void testByList(ArrayList<String> list) {
		System.out.println(list);
		final var result = Parser.build(list);
		System.out.println(result);
		final var eq = Parser.parse(result);
		System.out.println(eq);
		final var eqResult = Parser.build(eq);

		assert (result.equals(eqResult)) : "not equal";
	}
}

class Parser {
	private final static Pattern space = Pattern.compile("[ ]");
	private final static Pattern escapeOrQuote = Pattern.compile("[\\\\\"]");
	private final static Pattern special = Pattern.compile("[\\\\\"]");

	static ArrayList<String> parse(String str) {
		final var len = str.length();
		var index = 0;
		var result = new ArrayList<String>();
		while (index < len) {
			index = str.indexOf('#', index);
			if (index == -1) {
				break;
			}

			if (index + 1 >= str.length()) {
				result.add("");
				break;
			}

			index += 1;
			char first = str.charAt(index);
			if (first != '"') {
				final var matcher = space.matcher(str.substring(index));
				if (!matcher.find()) {
					result.add(str.substring(index));
					break;
				}

				final var spaceIndex = matcher.start();
				result.add(str.substring(index, index + spaceIndex));
				index += spaceIndex;
				continue;
			}

			// TODO: handle tag w/ ""
			index += 1;
			var builder = new StringBuilder();
			var end = false;
			while (!end) {
				final var matcher = escapeOrQuote.matcher(str.substring(index));
				if (!matcher.find()) {
					builder.append(str.substring(index));
					break;
				}

				final var offset = matcher.start();
				builder.append(str.substring(index, index + offset));
				switch (str.charAt(index + offset)) {
					case '"':
						index += offset + 1;
						end = true;
						break;
					case '\\':
						if (index + offset + 1 >= str.length()) {
							builder.append('\\');
							end = true;
							break;
						}
						switch (str.charAt(index + offset + 1)) {
							case '\\':
								builder.append('\\');
								index += offset + 2;
								break;
							case '"':
								builder.append('"');
								index += offset + 2;
								break;
							default:
								builder.append('\\');
								index += offset + 1;
								break;
						}
						break;
				}
			}
			result.add(builder.toString());
		}

		return result;
	}

	static String build(ArrayList<String> list) {
		var newList = new ArrayList<String>(list.size());
		for (String str : list) {
			if (!space.matcher(str).find() && !str.startsWith("\"")) {
				newList.add("#" + str);
				continue;
			}

			var builder = new StringBuilder();
			builder.append("#\"");

			final var len = str.length();
			var index = 0;
			while (index < len) {
				final var matcher = special.matcher(str.substring(index));
				if (!matcher.find()) {
					builder.append(str.substring(index));
					break;
				}

				final var next = matcher.start();

				builder.append(str.substring(index, index + next));
				builder.append('\\');
				builder.append(str.charAt(index + next));
				index = index + next + 1;
			}

			builder.append('"');
			newList.add(builder.toString());
		}
		return String.join(" ", newList);
	}
}
