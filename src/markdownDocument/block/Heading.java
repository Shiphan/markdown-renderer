package markdownDocument.block;

import markdownDocument.span.Span;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Component;
import java.awt.Font;

public class Heading implements Block {
	public enum Level {
		h1, h2, h3, h4, h5, h6
	}
	private final String content;
	private final Level level;
	public Heading(String content, Level level) {
		this.content = content;
		this.level = level;
	}
	/*
	public Component render() {
		final var label = new JLabel(this.content);
		label.setFont(new Font(
			"",
			Font.PLAIN,
			switch (this.level) {
				case h1 -> 36;
				case h2 -> 32;
				case h3 -> 28;
				case h4 -> 24;
				case h5 -> 20;
				case h6 -> 16;
			}
		));
		return label;
	}
	*/
	@Override
	public void render(JTextPane textPane) {
		final var attributeSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(
			attributeSet, 
			switch (this.level) {
				case h1 -> 36;
				case h2 -> 32;
				case h3 -> 28;
				case h4 -> 24;
				case h5 -> 20;
				case h6 -> 16;
			}
		);
		final var doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), this.content + '\n', attributeSet);
		} catch (javax.swing.text.BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
}