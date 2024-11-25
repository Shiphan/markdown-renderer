package span;

import javax.swing.text.StyledDocument;

public class Text implements Span {
	private String content;
	private boolean bold;
	private boolean ltalic;
	public Text(String content, boolean bold, boolean ltalic) {
		this.content = content;
		this.bold = bold;
		this.ltalic = ltalic;
	}
	public void render(StyledDocument doc) {

	}
}
