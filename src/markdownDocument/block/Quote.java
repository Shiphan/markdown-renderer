package markdownDocument.block;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

public class Quote implements Block {
    private List<Block> contents;

    public Quote(List<Block> contents) {
        this.contents = contents;
    }
    @Override
    public void render(JTextPane textPane) {
        final var quotePane = new JTextPane();
        quotePane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        quotePane.setForeground(Color.LIGHT_GRAY);
        quotePane.setBackground(Color.DARK_GRAY);

        for (final var block : this.contents) {
            block.render(quotePane);
        }

        textPane.setSelectionStart(Integer.MAX_VALUE);
        textPane.setSelectionEnd(Integer.MAX_VALUE);
        textPane.insertComponent(quotePane);
        final var doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "\n", null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
