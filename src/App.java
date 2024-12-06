import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;
import java.util.List;

import markdownDocument.block.Block;
import markdownDocument.span.Span;
import markdownDocument.block.Heading.Level;
import markdownDocument.block.Heading;

public class App {
	public static void main(String[] args) {
		var frame = new JFrame();
		var pane = new JTextPane();
		var doc = pane.getStyledDocument();

		List<Block> list = List.of(
			new Heading("test here", Level.h1),
			new Heading("test here", Level.h2),
			new Heading("test here", Level.h3),
			new Heading("test here", Level.h4),
			new Heading("test here", Level.h5),
			new Heading("test here", Level.h6)
		);

		for (var ele : list) {
			ele.render(doc);
		}

		frame.add(pane);
		frame.setSize(200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	static boolean documentAppend(StyledDocument doc, String str, AttributeSet style) {
		try {
			doc.insertString(doc.getLength(), str, style);
		} catch (javax.swing.text.BadLocationException e) {
			return false;
		}
		return true;
	}
}
