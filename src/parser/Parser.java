package parser;

import java.util.List;

import markdownDocument.MarkdownDocument;
import markdownDocument.block.Paragraph;
import markdownDocument.span.Text.StyledText;;

public class Parser {
	private String source;

	public Parser(String source) {
		this.source = source;
	}
	public MarkdownDocument toMarkdownDocument() {
		return new MarkdownDocument(List.of(
			new Paragraph(List.of(
				new StyledText(this.source)
			))
		));
	}
}
