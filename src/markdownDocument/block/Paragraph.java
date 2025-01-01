package markdownDocument.block;

import markdownDocument.span.LineBreak;
import markdownDocument.span.Span;
import markdownDocument.span.Text.StyledText;

import javax.swing.JTextPane;
import java.util.List;
import java.util.stream.IntStream;

public class Paragraph implements Block {
	private List<Span> contents;

	public Paragraph(List<Span> contents) {
		this.contents = contents;
	}
	@Override
	public void render(JTextPane textPane, int indent) {
		System.out.printf("%s indent == %d\n", this.toString(), indent);
		new StyledText(" ".repeat(indent)).render(textPane, 0);
		final var size = this.contents.size();
		IntStream.range(0, size).forEach((i) -> {
			final var content = this.contents.get(i);
			content.render(
				textPane,
				i == size - 1 && content instanceof LineBreak ? 0 : indent
			);
		});
	}
	@Override
	public String toString() {
		return String.format(
			"Paragraph { contents: %s }",
			this.contents.toString()
		);
	}
}
