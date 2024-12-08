package markdownDocument.span;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class LineBreak implements Span {

    public LineBreak() {}
    @Override
    public void render(JTextPane textPane) {
        final var doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "\n", null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
