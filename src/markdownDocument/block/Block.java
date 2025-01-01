package markdownDocument.block;

import javax.swing.JTextPane;

public interface Block {
	void render(JTextPane textPane, int indent);
}
