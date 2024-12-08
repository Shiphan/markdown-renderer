package markdownDocument;

import markdownDocument.block.Block;
import markdownDocument.block.Heading;
import markdownDocument.block.Heading.Level;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import java.awt.Component;
import java.util.List;

public class MarkdownDocument {
	private List<Block> blocks;

	public MarkdownDocument() {}
	// public MarkdownDocument(String source) {}
	public MarkdownDocument(List<Block> blocks) {
		this.blocks = blocks;
	}
	public Component render() {
		var textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));

		for (var block: this.blocks) {
			block.render(textPane);
		}

		return textPane;
	}
}
