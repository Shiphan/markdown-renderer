package markdownDocument.block;

import markdownDocument.span.Span;

import javax.swing.JTextPane;
import java.util.List;

public class Paragraph implements Block {
    private List<Span> contents;

    public Paragraph(List<Span> contents) {
        this.contents = contents;
    }
    @Override
    public void render(JTextPane textPane) {
        for (final var span : this.contents) {
            span.render(textPane);
        }
    }
    @Override
    public String toString() {
        return String.format(
            "Paragraph { contents: %s }",
            this.contents.toString()
        );
    }
}
