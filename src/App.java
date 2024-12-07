import java.util.List;

import example.Example;
import markdownDocument.MarkdownDocument;
import markdownDocument.block.Heading.Level;
import markdownDocument.block.Heading;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

public class App {
	public static void main(String[] args) {
		var frame = new JFrame("Markdown Renderer");
		var panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		var markdownDocument = new MarkdownDocument(
			List.of(
				new Heading("test here", Level.h1),
				new Heading("test here", Level.h2),
				new Heading("test here", Level.h3),
				new Heading("test here", Level.h4),
				new Heading("test here", Level.h5),
				new Heading("test here", Level.h6)
			)
		);

		panel.add(Example.toComponent());
		panel.add(markdownDocument.render());

		frame.add(panel);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

