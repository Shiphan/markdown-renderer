package markdownDocument.span;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class Image implements Span {
    private String src;

    public Image(String src) {
        this.src = src;
    }
    @Override
    public void render(JTextPane textPane) {

    }
}
