package markdownDocument;

import markdownDocument.block.Block;

import javax.swing.JTextPane;
import java.util.List;

public class MarkdownDocument {
	private List<Block> blocks;

	public MarkdownDocument() {
		this.blocks = List.of();
	}
	public MarkdownDocument(List<Block> blocks) {
		this.blocks = blocks;
	}
	public JTextPane render() {
		var textPane = new JTextPane();

		for (var block: this.blocks) {
			block.render(textPane);
		}

		return textPane;
	}
}
