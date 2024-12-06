package markdownDocument.block;

import markdownDocument.span.Span;

import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Heading implements Block {
	public enum Level {
		h1, h2, h3, h4, h5, h6
	}
	private String content;
	private Level level;
	public Heading(String content, Level level) {
		this.content = content;
		this.level = level;
	}
	@Override
	public void render(StyledDocument doc) {
		var attributeSet = new SimpleAttributeSet();
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
		try {
			doc.insertString(doc.getLength(), this.content + '\n', attributeSet);
		} catch (javax.swing.text.BadLocationException e) {
			System.out.printf("render error: %s\n", e.getMessage());
			e.printStackTrace();
		}
	}
}
