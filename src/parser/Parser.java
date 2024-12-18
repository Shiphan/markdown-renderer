package parser;

import java.util.ArrayList;
import java.util.stream.IntStream;

import markdownDocument.MarkdownDocument;
import markdownDocument.block.Block;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Paragraph;
import markdownDocument.span.LineBreak;
import markdownDocument.span.Span;
import markdownDocument.span.Text.Code;
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
			index = consumeParagraph(index, blocks);
		}

		return new MarkdownDocument(blocks);
	}
	private int consumeParagraph(int index, ArrayList<Block> blocks) {
		final var len = this.source.length();
		final var spans = new ArrayList<Span>();
		var bold = false;
		var italic = false;
		var code = false;
		var preIndex = index;
		while (index < len) {
			final var ch = this.source.charAt(index);
			if (ch == '*') {
				if (this.source.startsWith("***", index) || this.source.startsWith("**_", index)) {
					spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
					bold = !bold;
					italic = !italic;
					index += 3;
					preIndex = index;
					continue;
				}
				if (this.source.startsWith("**", index)) {
					spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
					bold = !bold;
					index += 2;
					preIndex = index;
					continue;
				}
				spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
				italic = !italic;
				index += 1;
				preIndex = index;
				continue;
			}
			if (ch == '_') {

			}
			if (ch == '`') {
				if (code) {
					spans.add(new Code(this.source.substring(preIndex, index)));
				} else {
					spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
				}
				code = !code;
				index += 1;
				preIndex = index;
				continue;
			}
			if (ch == ' ') {

			}
			if (ch == '\n') {
				if (preIndex != index) {
					spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
				}
				index++;
				preIndex = index;
				if (index < len && this.source.charAt(index) == '\n') {
					break;
				}
				continue;
			}
			index++;
		}
		if (preIndex != index) {
			spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
		}
		if (!(spans.getLast() instanceof LineBreak)) {
			spans.add(new LineBreak());
		}
		blocks.add(new Paragraph(spans));
		return this.skipEmptyLine(index);
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
