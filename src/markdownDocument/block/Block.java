package markdownDocument.block;

import javax.swing.JTextPane;
import java.awt.Component;

public interface Block {
    void render(JTextPane textPane);
}
