package markdownDocument.span;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public interface Span {
	void render(JTextPane textPane, int indent);
}
