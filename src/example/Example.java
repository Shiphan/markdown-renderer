package example;

import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.util.List;

public class Example {
	public static JTextPane toJTextPane() {
		final var textPane = new JTextPane();
		// textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));

		heading(textPane, "Heading", 36);
		heading(textPane, "Heading too", 32);
		heading(textPane, "Heading 3", 28);

		rule(textPane);

		content(textPane, List.of("content of a markdown file", "content of a markdown file"));
		lineBreak(textPane);
		code(textPane, "content of a markdown file\ncontent of a markdown file");
		lineBreak(textPane);
		content(textPane, "content of a markdown file");
		lineBreak(textPane);
		content(textPane, "a line end with two or more spaces will has a \\n but still in the same paragraph");
		lineBreak(textPane);
		content(textPane, "inline ");
		code(textPane, "code");
		lineBreak(textPane);

		rule(textPane);

		content(textPane, "abc ");
		content(textPane, "Bold Style", true, false);
		content(textPane, " def");
		lineBreak(textPane);

		content(textPane, "abc");
		content(textPane, "Bold Style", true, false);
		content(textPane, "def");
		lineBreak(textPane);

		content(textPane, "abc ");
		content(textPane, "Italic Style", false, true);
		content(textPane, " def");
		lineBreak(textPane);

		content(textPane, "abc");
		content(textPane, "Italic Style", false, true);
		content(textPane, "def");
		lineBreak(textPane);

		rule(textPane);

		quote(textPane);




		setSelectionTo(textPane, 0);
		return textPane;
	}
	private static void heading(JTextPane textPane, String str, int size) {
		final var doc = textPane.getStyledDocument();
		final var attributeSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(attributeSet, size);
		StyleConstants.setBold(attributeSet, true);
		try {
			doc.insertString(doc.getLength(), str + '\n', attributeSet);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	private static void rule(JTextPane textPane) {
		setSelectionTo(textPane, Integer.MAX_VALUE);
		textPane.insertComponent(new JSeparator());
		setSelectionTo(textPane, Integer.MAX_VALUE);
		final var doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	private static void content(JTextPane textPane, String string) {
		content(textPane, List.of(string), false, false);
	}
	private static void content(JTextPane textPane, List<String> strings) {
		content(textPane, strings, false, false);
	}
	private static void content(JTextPane textPane, String string, boolean bold, boolean italic) {
		content(textPane, List.of(string), bold, italic);
	}
	private static void content(JTextPane textPane, List<String> strings, boolean bold, boolean italic) {
		final var doc = textPane.getStyledDocument();
		final var attributeSet = new SimpleAttributeSet();
		StyleConstants.setBold(attributeSet, bold);
		StyleConstants.setItalic(attributeSet, italic);
		try {
			doc.insertString(doc.getLength(), String.join(" ", strings), attributeSet);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	private static void lineBreak(JTextPane textPane) {
		final var doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	private static void code(JTextPane textPane, String str) {
		final var doc = textPane.getStyledDocument();
		final var attributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attributeSet, "Mono");
		StyleConstants.setForeground(attributeSet, Color.LIGHT_GRAY);
		StyleConstants.setBackground(attributeSet, Color.DARK_GRAY);
		try {
			doc.insertString(doc.getLength(), str, attributeSet);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	private static void quote(JTextPane textPane) {
		final var quotePane = new JTextPane();
		quotePane.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
		quotePane.setForeground(Color.LIGHT_GRAY);
		quotePane.setBackground(Color.DARK_GRAY);

		final var quoteDoc= quotePane.getStyledDocument();
		try {
			quoteDoc.insertString(quoteDoc.getLength(), "stuff in quote", null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}

		setSelectionTo(textPane, Integer.MAX_VALUE);
		textPane.insertComponent(quotePane);
		setSelectionTo(textPane, Integer.MAX_VALUE);
		final var doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	private static void setSelectionTo(JTextPane textPane, int selection) {
		textPane.setSelectionStart(selection);
		textPane.setSelectionEnd(selection);
	}
}