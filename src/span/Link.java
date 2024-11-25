package span;

import javax.swing.text.StyledDocument;

public class Link implements Span {
	private Span content;
	private String url;
	private String title;
	public Link(Span content, String url, String title) {
		this.content = content;
		this.url = url;
		this.title = title;
	}
	public void render(StyledDocument doc) {

	}
}
