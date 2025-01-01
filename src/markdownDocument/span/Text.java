package markdownDocument.span;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;

public interface Text extends Span {
	class StyledText implements Text {
		private String content;
		private boolean bold;
		private boolean italic;

		public StyledText(String content) {
			this(content, false, false);
		}
		public StyledText(String content, boolean bold, boolean italic) {
			this.content = content;
			this.bold = bold;
			this.italic = italic;
		}
		@Override
		public void render(JTextPane textPane, int indent) {
			final var doc = textPane.getStyledDocument();
			final var attributeSet = new SimpleAttributeSet();
			StyleConstants.setBold(attributeSet, this.bold);
			StyleConstants.setItalic(attributeSet, this.italic);
			try {
				doc.insertString(
					doc.getLength(),
					String.join("\n" + " ".repeat(indent), this.content.split("\n")), attributeSet
				);
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}
		@Override
		public String toString() {
			return String.format(
				"StyledText { content: %s, bold: %b, italic: %b }",
				this.content,
				this.bold,
				this.italic
			);
		}
	}

	class Code implements Text {
		private String content;

		public Code(String content) {
			this.content = content;
		}
		@Override
		public void render(JTextPane textPane, int indent) {
			final var doc = textPane.getStyledDocument();
			final var attributeSet = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attributeSet, "mono");
			StyleConstants.setForeground(attributeSet, Color.LIGHT_GRAY);
			StyleConstants.setBackground(attributeSet, Color.DARK_GRAY);
			try {
				doc.insertString(
					doc.getLength(),
					String.join("\n" + " ".repeat(indent), this.content.split("\n")), attributeSet
				);
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}
		@Override
		public String toString() {
			return String.format(
				"Code { content: %s }",
				this.content
			);
		}
	}
}
