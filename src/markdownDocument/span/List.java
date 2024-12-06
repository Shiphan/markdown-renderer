package markdownDocument.span;

import markdownDocument.block.Block;
import markdownDocument.block.Paragraph;

import javax.swing.text.StyledDocument;

public class List implements Span {
    @Override
    public void render(StyledDocument doc) {

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
