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

    }

    public class ListItem {
        private Paragraph content;
        private java.util.List<Block> elements;
        public ListItem(Paragraph content) {
            this.content = content;
            this.elements = null;
        }
        public ListItem(Paragraph content, java.util.List<Block> elements) {
            this.content = content;
            this.elements = elements;
        }
    }
}