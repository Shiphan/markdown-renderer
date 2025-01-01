package markdownDocument.span;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import markdownDocument.Util;

import java.awt.Component;
import java.net.MalformedURLException;
import java.net.URL;

public class Image implements Span {
	private String src;
	private String title;

	public Image(String src) {
		this.src = src;
	}
	@Override
	public void render(JTextPane textPane, int indent) {
		// TODO: Image.render
		Util.setSelectionToEnd(textPane);
		textPane.insertComponent(new JLabel(new ImageIcon(this.src, this.title)));
		final var doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
}
