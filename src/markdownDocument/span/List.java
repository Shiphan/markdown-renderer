package markdownDocument.span;

import markdownDocument.block.Block;
import markdownDocument.block.Paragraph;

import javax.swing.JTextPane;

public class List implements Span {
    private java.util.List<ListItem> contents;
    private boolean ordered;

    public List(java.util.List<ListItem> contents) {
        this.contents = contents;
    }
    @Override
    public void render(JTextPane textPane) {
        if (this.ordered) {
            int index = 1;
            for (final var item : this.contents) {
                item.render(textPane, index);
                index++;
            }
        } else {
            for (final var item : this.contents) {
                item.render(textPane);
            }
        }

    }

    public static class ListItem {
        private Paragraph content;
        private java.util.List<Block> elements;
        public ListItem(Paragraph content) {
            this.content = content;
            this.elements = java.util.List.of();
        }
        public ListItem(Paragraph content, java.util.List<Block> elements) {
            this.content = content;
            this.elements = elements;
        }
        public void render(JTextPane textPane) {
            this.render(textPane, " - ");
        }
        public void render(JTextPane textPane, int order) {
            this.render(textPane, String.format(" %d. ", order));
        }
        private void render(JTextPane textPane, String prefix) {
            new Text.StyledText(prefix).render(textPane);
            this.content.render(textPane);
            // TODO: indention of elements in ListItem
            for (final var element : this.elements) {
                element.render(textPane);
            }
        }
    }
}
