package parser;

import java.util.ArrayList;

import markdownDocument.MarkdownDocument;
import markdownDocument.block.Block;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Paragraph;
import markdownDocument.block.Quote;
import markdownDocument.span.LineBreak;
import markdownDocument.span.List;
import markdownDocument.span.List.ListItem;
import markdownDocument.span.Span;
import markdownDocument.span.Text;
import markdownDocument.span.Text.Code;
import markdownDocument.span.Text.StyledText;
import panic.Panic;

public class Parser2 {
	private final String source;
	private final int len;
	private State state;

	public Parser2(String source) {
		this.source = source;
		this.len = source.length();
		this.state = new State();
	}
	public MarkdownDocument toMarkdownDocument() {
		this.state = new State();

		final var blocks = new ArrayList<Block>();
		final var stack = new ArrayList<Layer>();
		stack.add(new Layer.Root(blocks));

		while (this.state.cursor < this.len) {
			System.out.printf("stack: %s\n", stack.getLast().toString());
			System.out.printf("cursor: %d\n", this.state.cursor);
			switch (stack.getLast()) {
				case Layer.Root(ArrayList<Block> blockList) -> {
					this.consumeEmptyLine();
					this.consumeBlock(blockList, stack);
				}
				case Layer.Paragraph(ArrayList<Span> spanList, ArrayList<Block> returnPoint, Layer.Paragraph.State state) -> {
					if (this.endOfParagraph(stack)) {
						if (state.cursor != this.state.cursor) {
							if (state.code) {
								spanList.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
							} else {
								spanList.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
							}
						}
						if (!spanList.isEmpty() && (spanList.getLast() instanceof Text)) {
							spanList.add(new LineBreak());
						}
						returnPoint.add(new Paragraph(spanList));
						stack.removeLast();
						continue;
					}
					this.consumeSpan(spanList, stack, state);
				}
				case Layer.Quote(ArrayList<Block> blockList, ArrayList<Block> returnPoint) -> {
					if (this.endOfQuote()) {
						returnPoint.add(new Quote(blockList));
						stack.removeLast();
						continue;
					}
					this.consumeBlock(blockList, stack);
				}
				case Layer.List(ArrayList<ListItem> items, ArrayList<Span> returnPoint, Layer.List.State state) -> {
					System.out.println(this.state.cursor);
					if (this.endOfList(state)) {
						returnPoint.add(new List(items));
						stack.removeLast();
						continue;
					}
					this.consumeListItem(items, stack, state);
				}
				case Layer.ListItemElements(ArrayList<Block> blockList, ArrayList<ListItem> returnPoint) -> {
					throw new RuntimeException("Layer.ListItemElements");
				}
			}
		}

		while (stack.size() > 1) {
			System.out.printf("size: %d\n", stack.size());
			switch (stack.removeLast()) {
				case Layer.Root(ArrayList<Block> blockList) -> {
					throw new Panic();
				}
				case Layer.Paragraph(ArrayList<Span> spanList, ArrayList<Block> returnPoint, Layer.Paragraph.State state) -> {
					if (state.cursor != this.state.cursor) {
						if (state.code) {
							spanList.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
						} else {
							spanList.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
						}
					}
					if (!spanList.isEmpty() && (spanList.getLast() instanceof Text)) {
						spanList.add(new LineBreak());
					}
					returnPoint.add(new Paragraph(spanList));
				}
				case Layer.Quote(ArrayList<Block> blockList, ArrayList<Block> returnPoint) -> {
					returnPoint.add(new Quote(blockList));
				}
				case Layer.List(ArrayList<ListItem> items, ArrayList<Span> returnPoint, Layer.List.State state) -> {
					returnPoint.add(new List(items));
				}
				case Layer.ListItemElements(ArrayList<Block> blockList, ArrayList<ListItem> returnPoint) -> {
					throw new RuntimeException("Layer.ListItemElements");
				}
			}
		}
		
		return new MarkdownDocument(blocks);
	}
	private void consumeEmptyLine() {
		while (this.state.cursor < this.len && this.source.charAt(this.state.cursor) == '\n') {
			this.state.cursor++;
		}
	}
	private void consumeBlock(ArrayList<Block> blocks, ArrayList<Layer> stack) {
		if (this.endOfBlocks()) {
			stack.removeLast();
			return;
		}
		if (this.consumeHeading(blocks)) {
			return;
		}
		if (this.consumeHorizontalRule(blocks)) {
			return;
		}
		if (this.consumeQuote()) {
			this.state.indent++;
			stack.add(new Layer.Quote(new ArrayList<Block>(), blocks));
			return;
		}
		stack.add(new Layer.Paragraph(new ArrayList<Span>(), blocks, new Layer.Paragraph.State(this.state.cursor)));
	}
	private void consumeSpan(ArrayList<Span> spans, ArrayList<Layer> stack, Layer.Paragraph.State state) {
		if (this.state.cursor >= this.len) {
			return;
		}
		final var ch = this.source.charAt(this.state.cursor);
		if (this.source.startsWith("  \n", this.state.cursor) || this.source.startsWith("<br>\n", this.state.cursor)) {
			if (state.cursor != this.state.cursor) {
				if (state.code) {
					spans.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
				} else {
					spans.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
				}
			}
			spans.add(new LineBreak());
			this.state.cursor = this.source.indexOf('\n', this.state.cursor) + 1;
			state.cursor = this.state.cursor;
			return;
		}
		if (ch == '\n') {
			if (state.code) {
				this.state.cursor++;
				return;
			}
			if (state.cursor != this.state.cursor) {
				if (state.code) {
					spans.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
				} else {
					spans.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
				}
				state.cursor = this.state.cursor;
			}
			spans.add(new StyledText(" "));
			this.state.cursor++;
			state.cursor = this.state.cursor;
			return;
		}
		if (charInString(ch, "-*+") && (this.state.cursor - 1 < 0 || this.source.charAt(this.state.cursor) == '\n')) {
			if (stack.size() > 1 && stack.get(stack.size() - 2) instanceof Layer.List) {

			}
			stack.add(new Layer.List(new ArrayList<ListItem>(), spans, new Layer.List.State()));
			return;
		}
		if (this.source.startsWith("***", this.state.cursor)) {
			if (state.cursor != this.state.cursor) {
				if (state.code) {
					spans.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
				} else {
					spans.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
				}
			}
			state.bold ^= true;
			state.italic ^= true;
			this.state.cursor += 3;
			state.cursor = this.state.cursor;
			return;
		}
		if (this.source.startsWith("**", this.state.cursor)) {
			if (state.cursor != this.state.cursor) {
				if (state.code) {
					spans.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
				} else {
					spans.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
				}
			}
			state.bold ^= true;
			this.state.cursor += 2;
			state.cursor = this.state.cursor;
			return;
		}
		if (this.source.startsWith("*", this.state.cursor)) {
			if (state.cursor != this.state.cursor) {
				if (state.code) {
					spans.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
				} else {
					spans.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
				}
			}
			state.italic ^= true;
			this.state.cursor += 1;
			state.cursor = this.state.cursor;
			return;
		}
		if (this.source.startsWith("```", this.state.cursor)) {
			if (state.cursor != this.state.cursor) {
				if (state.code) {
					spans.add(new Code(this.source.substring(state.cursor, this.state.cursor)));
				} else {
					spans.add(new StyledText(this.source.substring(state.cursor, this.state.cursor), state.bold, state.italic));
				}
			}
			state.code ^= true;
			this.state.cursor += 3;
			state.cursor = this.state.cursor;
			return;
		}

		this.state.cursor++;
	}
	private void consumeListItem(ArrayList<ListItem> items, ArrayList<Layer> stack, Layer.List.State state) {
		System.out.printf("consumeListItem: %d\n", this.state.cursor);
		switch (state.part) {
			case Layer.List.State.Part.Content(ArrayList<Block> blocks) -> {
				if (blocks.isEmpty()) {
					this.state.cursor += 2;
					stack.add(new Layer.Paragraph(new ArrayList<Span>(), blocks, new Layer.Paragraph.State(this.state.cursor)));
				} else {
					state.part = new Layer.List.State.Part.Elements((Paragraph)blocks.getFirst(), null);
				}
			}
			case Layer.List.State.Part.Elements(Paragraph content, ArrayList<Block> blocks) -> {
				if (blocks == null) {
					state.part = new Layer.List.State.Part.Elements(content, new ArrayList<Block>());
				} else {
					items.add(new ListItem(content, blocks));
					state.part = new Layer.List.State.Part.Content(new ArrayList<Block>());
				}
			}
		}

	}
	private boolean consumeHeading(ArrayList<Block> blocks) {
		final var cursor = this.state.cursor + this.state.indent;
		if (cursor < len && this.source.charAt(cursor) == '#') {
			var tmpIndex = firstNot(this.source, '#', cursor);
			final var headingLevel = tmpIndex - cursor;
			if (headingLevel <= 6 && tmpIndex < len && this.source.charAt(tmpIndex) == ' ') {
				tmpIndex = this.source.indexOf('\n', tmpIndex + 1);
				if (tmpIndex == -1) {
					tmpIndex = this.len;
				}
				blocks.add(new Heading(
					this.source.substring(cursor + headingLevel + 1, tmpIndex),
					Heading.Level.values()[headingLevel - 1]
				));
				this.state.cursor = tmpIndex + 1;
				return true;
			}
		}
		// TODO: support `=` and `-` for heading
		return false;
	}
	private boolean consumeHorizontalRule(ArrayList<Block> blocks) {
		final var cursor = this.state.cursor + this.state.indent;
		if (cursor >= this.len) {
			return false;
		}
		final var ch = this.source.charAt(cursor);
		if (charInString(ch, "*-_")) {
			final var tmpIndex = firstNot(this.source, ch, cursor);
			if (tmpIndex - cursor >= 3 && (tmpIndex >= len || this.source.charAt(tmpIndex) == '\n')) {
				blocks.add(new HorizontalRule());
				this.state.cursor = tmpIndex + 1;
				return true;
			}
		}
		return false;
	}
	private boolean consumeQuote()  {
		final var cursor = this.state.cursor + this.state.indent;
		if (cursor >= this.len) {
			return false;
		}
		final var ch = this.source.charAt(cursor);
		if (ch == '>') {
			this.state.cursor = cursor + 1;
			return true;
		}
		return false;
	}
	private void consumeParagraph() {
		throw new RuntimeException("consumeParagraph");
		//return false;
	}
	private boolean endOfBlocks() {
		return firstNot(this.source, ' ', this.state.cursor) - this.state.cursor < this.state.indent;
	}
	private boolean endOfParagraph(ArrayList<Layer> stack) {
		if (this.state.cursor > this.len
			&& charAtInString(this.source, this.state.cursor, "-*+")
			&& (this.state.cursor - 1 < 0 || this.source.charAt(this.state.cursor) == '\n')
			&& stack.size() > 1
			&& stack.get(stack.size() - 2) instanceof Layer.List
		) {
			return true;
		}
		return this.state.cursor >= this.len || this.source.startsWith("\n\n", this.state.cursor);
	}
	private boolean endOfQuote() {
		return false;
	}
	private boolean endOfList(Layer.List.State state) {
		return state.part instanceof Layer.List.State.Part.Elements
			&& !charAtInString(this.source, this.state.cursor, "-*+");
	}
	private static boolean charAtIs(String string, int index, char ch) {
		return index < string.length() && string.charAt(index) == ch;
	}
	private static boolean charAtInString(String string, int index, String targets) {
		return index < string.length() && charInString(string.charAt(index), targets);
	}
	private static boolean charInString(char ch, String string) {
		return string.chars().anyMatch((x) -> ch == x);
	}
	private static boolean charInRange(char ch, char start, char end) {
		return start <= ch && ch <= end;
	}
	/**
	 * @return the first character that is not in string, string's length if not found
	 */
	private static int firstNot(String string, char ch, int fromIndex) {
		final var len = string.length();
		var index = fromIndex;
		while (index < len && string.charAt(index) == ch) {
			index++;
		}
		return index;
	}
 
	private static class State {
		public int cursor;
		public int indent;

		public State() {
			this.cursor = 0;
			this.indent = 0;
		}
	}
	private static sealed interface Layer {
		record Root(ArrayList<Block> blocks) implements Layer {}
		record Paragraph(ArrayList<Span> spans, ArrayList<Block> returnPoint, State state) implements Layer {
			private static class State {
				public int cursor;
				public boolean bold;
				public boolean italic;
				public boolean code;

				public State(int cursor) {
					this.cursor = cursor;
					this.bold = false;
					this.italic = false;
					this.code = false;
				}
			}
		}
		record List(ArrayList<ListItem> items, ArrayList<Span> returnPoint, State state) implements Layer {
			private static class State {
				public Part part;

				public State() {
					this.part = new Layer.List.State.Part.Content(new ArrayList<Block>());
				}

				private static sealed interface Part {
					record Content(ArrayList<Block> blocks) implements Part {}
					record Elements(markdownDocument.block.Paragraph content, ArrayList<Block> blocks) implements Part {}
				}
			}
		}
		record ListItemElements(ArrayList<Block> blocks, ArrayList<ListItem> returnPoint) implements Layer {}
		record Quote(ArrayList<Block> blocks, ArrayList<Block> returnPoint) implements Layer {}
	}
}
