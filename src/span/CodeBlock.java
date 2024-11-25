package span;

import javax.swing.text.StyledDocument;

public class CodeBlock implements Span {
	private String content;
	private String language;
	public CodeBlock(String content, String language) {
		this.content = content;
		this.language = language;
	}
	public void render(StyledDocument doc) {

	}
}
