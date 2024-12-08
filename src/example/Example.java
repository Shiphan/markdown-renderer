package example;

import javax.swing.BoxLayout;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Component;

public class Example {
	public static Component toComponent() {
		final var textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));

		heading("Heading", 36, textPane.getStyledDocument());
		heading("Heading too", 32, textPane.getStyledDocument());
		heading("Heading 3", 28, textPane.getStyledDocument());

		textPane.add(new JSeparator(), -1);




		return textPane;
	}
	private static void heading(String str, int size, StyledDocument doc) {
		final var attributeSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(attributeSet, size);
		try {
			doc.insertString(doc.getLength(), str + '\n', attributeSet);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}

	}
}
