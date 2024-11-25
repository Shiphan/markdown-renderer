package span;

import javax.swing.text.StyledDocument;

public class Code implements Span {
	private String content;
	public Code(String content) {
		this.content = content;
	}
	public void render(StyledDocument doc) {

	}
}
