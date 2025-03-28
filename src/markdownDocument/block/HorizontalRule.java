package markdownDocument.block;

import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import markdownDocument.MarkdownDocument;
import markdownDocument.Util;

public class HorizontalRule implements Block {
	public HorizontalRule() {}

	@Override
	public void render(JTextPane textPane, int indent) {
		final var doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), " ".repeat(indent), null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
		Util.setSelectionToEnd(textPane);
		textPane.insertComponent(new JSeparator());
		try {
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
}
