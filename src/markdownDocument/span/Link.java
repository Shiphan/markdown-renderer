package markdownDocument.span;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class Link implements Span {
	private Text content;
	private String url;
	private String title;

	public Link(Text content, String url, String title) {
		this.content = content;
		this.url = url;
		this.title = title;
	}
	@Override
	public void render(JTextPane textPane, int indent) {
		// TODO: Link.render
		this.content.render(textPane, indent);
	}
}
