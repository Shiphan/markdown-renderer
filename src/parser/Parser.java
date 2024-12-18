package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import markdownDocument.MarkdownDocument;
import markdownDocument.block.Block;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Paragraph;
import markdownDocument.span.LineBreak;
import markdownDocument.span.Span;
import markdownDocument.span.Text.StyledText;;

public class Parser {
	private String source;

	public Parser(String source) {
		this.source = source;
	}
	public MarkdownDocument toMarkdownDocument() {
		final var len = this.source.length();
		final var blocks = new ArrayList<Block>();

		var index = 0;
		while (index < len) {
			final var ch = this.source.charAt(index);

			// heading
			if (ch == '#') {
				var tmpIndex = index + 1;
				while (tmpIndex < len && this.source.charAt(tmpIndex) == '#' && tmpIndex - index < 6) {
					tmpIndex++;
				}
				final var headingLevel = tmpIndex - index;
				if (tmpIndex < len && this.source.charAt(tmpIndex) == ' ') {
					while (tmpIndex < len && this.source.charAt(tmpIndex) != '\n') {
						tmpIndex++;
					}
					blocks.add(new Heading(
						this.source.substring(index + headingLevel + 1, tmpIndex),
						Heading.Level.values()[headingLevel - 1]
					));
					index = this.skipEmptyLine(tmpIndex);
					continue;
				}
			}

			// horizontal rules
			if (charInString(ch, "*-_")) {
				var tmpIndex = index + 1;
				while (tmpIndex < len && this.source.charAt(tmpIndex) == ch) {
					tmpIndex++;
				}
				if (tmpIndex - index >= 3 && (tmpIndex >= len || this.source.charAt(tmpIndex) == '\n')) {
					blocks.add(new HorizontalRule());
					index = this.skipEmptyLine(tmpIndex);
					continue;
				}
			}

			// paragraph
			var tmpIndex = index;
			final var spans = new ArrayList<Span>();
			while (tmpIndex < len && this.source.charAt(tmpIndex) != '\n') {
				final var start = index;
				while (tmpIndex < len && this.source.charAt(tmpIndex) != '\n') {
					tmpIndex++;
				}
				final var content = this.source.substring(start, tmpIndex);
				spans.add(new StyledText(content));
				if (content.endsWith("  ") || content.endsWith("<br>")) {
					spans.add(new LineBreak());
				}
				tmpIndex++;
			}
			if (!(spans.getLast() instanceof LineBreak)) {
				spans.add(new LineBreak());
			}
			blocks.add(new Paragraph(spans));
			index = this.skipEmptyLine(tmpIndex);
			continue;
		}

		return new MarkdownDocument(blocks);
	}
	private int skipEmptyLine(int index) {
		final var len = this.source.length();
		while (index < len && this.source.charAt(index) == '\n') {
			index++;
		}
		return index;
	}
	private static boolean charInString(char ch, String string) {
		return IntStream.range(0, string.length()).anyMatch((i) -> ch == string.charAt(i));
	}
}
