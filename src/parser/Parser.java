package parser;

import java.util.ArrayList;

import markdownDocument.MarkdownDocument;
import markdownDocument.block.Block;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Paragraph;
import markdownDocument.block.Quote;
import markdownDocument.span.LineBreak;
import markdownDocument.span.Span;
import markdownDocument.span.Text;
import markdownDocument.span.List.ListItem;
import markdownDocument.span.Text.Code;
import markdownDocument.span.Text.StyledText;
import panic.Panic;;

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
						switch (headingLevel) {
							case 1 -> Heading.Level.h1;
							case 2 -> Heading.Level.h2;
							case 3 -> Heading.Level.h3;
							case 4 -> Heading.Level.h4;
							case 5 -> Heading.Level.h5;
							case 6 -> Heading.Level.h6;
							default -> throw new Panic();
						}
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

			// quote
			if (this.source.startsWith("> ", index)) {
				final var quoteBlocks = new ArrayList<Block>();
				while (this.source.startsWith("> ", index)) {
					index = consumeParagraph(index, quoteBlocks, "> ");
				}
				blocks.add(new Quote(quoteBlocks));
				continue;
			}

			// paragraph
			index = consumeParagraph(index, blocks, "");
		}

		return new MarkdownDocument(blocks);
	}
	private int consumeParagraph(int index, ArrayList<Block> blocks, String prefix) {
		final var len = this.source.length();
		final var spans = new ArrayList<Span>();
		final var style = new Style();
		while (index < len && this.source.charAt(index) != '\n' && this.source.startsWith(prefix, index)) {
			index += prefix.length();
			if (index >= len) {
				break;
			}
			if (charInString(this.source.charAt(index), "-*+") && index + 1 < len && this.source.charAt(index + 1) == ' ') {
				final var listItems = new ArrayList<ListItem>();
				while (index < len) {
					if (!(charInString(this.source.charAt(index), "-*+") && index + 1 < len && this.source.charAt(index + 1) == ' ')) {
						break;
					}
					final var tmpSpans = new ArrayList<Span>();
					index = this.consumeLine(index + 2, tmpSpans, style);
					tmpSpans.add(new LineBreak());
					final var elements = new ArrayList<Block>();
					while (this.source.startsWith(prefix + " ".repeat(4), index)) {
						index = consumeParagraph(index, elements, prefix + " ".repeat(4));
					}
					//index = this.consumeListItemElements(index, elements);
					listItems.add(new ListItem(new Paragraph(tmpSpans), elements));
				}
				spans.add(new markdownDocument.span.List(listItems));
				continue;
			}
			if (this.isOrderedList(index)) {
				final var listItems = new ArrayList<ListItem>();
				while (index < len) {
					if (!charInRange(this.source.charAt(index), '0', '9')) {
						break;
					}
					var tmpIndexForItem = index;
					while (tmpIndexForItem < len && charInRange(this.source.charAt(tmpIndexForItem), '0', '9')) {
						tmpIndexForItem++;
					}
					if (!this.source.startsWith(". ", tmpIndexForItem)) {
						break;
					}
					final var tmpSpans = new ArrayList<Span>();
					index = this.consumeLine(tmpIndexForItem + 2, tmpSpans, style);
					tmpSpans.add(new LineBreak());
					final var elements = new ArrayList<Block>();
					while (this.source.startsWith(prefix + " ".repeat(4), index)) {
						index = consumeParagraph(index, elements, prefix + " ".repeat(4));
					}
					//index = this.consumeListItemElements(index, elements);
					listItems.add(new ListItem(new Paragraph(tmpSpans), elements));
				}
				spans.add(new markdownDocument.span.List(listItems, true));
				continue;
			}

			index = this.consumeLine(index, spans, style);
		}
		// if (preIndex != index) {
		// 	spans.add(new StyledText(this.source.substring(preIndex, index), bold, italic));
		// }
		// if (!(spans.getLast() instanceof LineBreak)) {
		if (!spans.isEmpty() && spans.getLast() instanceof Text) {
			spans.add(new LineBreak());
		}
		blocks.add(new Paragraph(spans));
		return this.skipEmptyLine(index);
	}
	private int consumeListItemElements(int index, ArrayList<Block> blocks) {
		// TODO: handle list item elements
		final var len = this.source.length();
		if (index < len && this.source.charAt(index) == '\n') {
			index++;
		}
		if (!this.source.startsWith("    ", index)) {
			return index;
		}
		var tmpIndex = index;
		while (tmpIndex < len && this.source.startsWith("    ", tmpIndex)) {

		}
		if (index < len && this.source.charAt(index) == '\n') {
			index++;
		}
		return tmpIndex;
	}
	private int consumeLine(int index, ArrayList<Span> spans, Style style) {
		final var len = this.source.length();
		var preIndex = index;
		while (index < len && this.source.charAt(index) != '\n') {
			final var ch = this.source.charAt(index);
			if (charInString(ch, "*_")) {
				if (this.source.startsWith("***", index)
					|| this.source.startsWith("___", index)
					|| this.source.startsWith((style.bold && style.italic ? "*__" : "__*"), index)
					|| this.source.startsWith((style.bold && style.italic ? "_**" : "**_"), index)
				) {
					spans.add(new StyledText(this.source.substring(preIndex, index), style.bold, style.italic));
					// style.bold = !style.bold;
					style.bold ^= true;
					style.italic ^= true;
					index += 3;
					preIndex = index;
					continue;
				}
				if (this.source.startsWith("**", index) || this.source.startsWith("__", index)) {
					spans.add(new StyledText(this.source.substring(preIndex, index), style.bold, style.italic));
					style.bold ^= true;
					index += 2;
					preIndex = index;
					continue;
				}
				spans.add(new StyledText(this.source.substring(preIndex, index), style.bold, style.italic));
				style.italic ^= true;
				index += 1;
				preIndex = index;
				continue;
			}
			if (ch == '`') {
				if (style.code) {
					spans.add(new Code(this.source.substring(preIndex, index)));
				} else {
					spans.add(new StyledText(this.source.substring(preIndex, index), style.bold, style.italic));
				}
				style.code ^= true;
				index += 1;
				preIndex = index;
				continue;
			}
			if (this.source.startsWith("  \n", index) || this.source.startsWith("<br>\n", index)) {
				if (style.code) {
					spans.add(new Code(this.source.substring(preIndex, index)));
				} else {
					spans.add(new StyledText(this.source.substring(preIndex, index), style.bold, style.italic));
				}
				spans.add(new LineBreak());
				index = this.source.indexOf('\n', index);
				preIndex = index;
				continue;
			}
			index++;
		}
		if (preIndex != index) {
			spans.add(new StyledText(this.source.substring(preIndex, index), style.bold, style.italic));
		}
		return index + 1;
	}
	private boolean isOrderedList(int index) {
		final var len = this.source.length();
		if (index >= len || !charInRange(this.source.charAt(index), '0', '9')) {
			return false;
		}
		while (index < len && charInRange(this.source.charAt(index), '0', '9')) {
			index++;
		}
		return this.source.startsWith(". ", index);
	}
	private int skipEmptyLine(int index) {
		final var len = this.source.length();
		while (index < len && this.source.charAt(index) == '\n') {
			index++;
		}
		return index;
	}
	private static boolean charInString(char ch, String string) {
		return string.chars().anyMatch((x) -> ch == x);
	}
	private static boolean charInRange(char ch, char start, char end) {
		return start <= ch && ch <= end;
	}

	private static class Style {
		public boolean bold;
		public boolean italic;
		public boolean code;

		public Style() {
			this.bold = false;
			this.italic = false;
			this.code = false;
		}
	}
}
