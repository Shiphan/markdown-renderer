package markdownDocument.block;

import javax.swing.text.StyledDocument;

public interface Block {
    void render(StyledDocument doc);
}
