package markdownDocument.span;

import markdownDocument.block.Block;
import markdownDocument.block.Paragraph;

import javax.swing.JTextPane;

public class List implements Span {
	private java.util.List<ListItem> contents;
	private boolean ordered;

	public List(java.util.List<ListItem> contents) {
		this(contents, false);
	}
	public List(java.util.List<ListItem> contents, boolean ordered) {
		this.contents = contents;
		this.ordered = ordered;
	}
	@Override
	public void render(JTextPane textPane, int indent) {
		System.out.printf("list indent == %d\n", indent);
		if (this.ordered) {
			int index = 1;
			for (final var item : this.contents) {
				item.render(textPane, String.format(" %d. ", index), indent);
				index++;
			}
		} else {
			for (final var item : this.contents) {
				item.render(textPane, " • ", indent);
			}
		}
	}
	@Override
	public String toString() {
		return String.format(
			"List { contents: %s }",
			this.contents.toString()
		);
	}

	public static class ListItem {
		private Paragraph content;
		private java.util.List<Block> elements;

		public ListItem(Paragraph content) {
			this.content = content;
			this.elements = java.util.List.of();
		}
		public ListItem(Paragraph content, java.util.List<Block> elements) {
			this.content = content;
			this.elements = elements;
		}
		public void render(JTextPane textPane, String prefix, int indent) {
			new Text.StyledText(prefix).render(textPane, indent);
			this.content.render(textPane, 0);
			// TODO: indention of elements in ListItem
			for (final var element : this.elements) {
				element.render(textPane, indent + 4);
			}
		}
		@Override
		public String toString() {
			return String.format(
				"ListItem { content: %s, elements: %s }",
				this.content.toString(),
				this.elements.toString()
			);
		}
	}
}
