import java.util.List;

import example.Example;
import markdownDocument.MarkdownDocument;
import markdownDocument.block.Heading.Level;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Quote;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class App {
	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale", "1.5");

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
				new Heading("test here", Level.h6),
				new HorizontalRule(),
				new Quote(List.of(
					new Heading("test", Level.h3),
					new Quote(List.of(
						new Heading("test2", Level.h5),
						new HorizontalRule()
					)),
					new HorizontalRule(),
					new HorizontalRule()
				))
			)
		);

		// panel.add(new JScrollPane(Example.toJTextPane()));
		// panel.add(markdownDocument.render());
		// final var renderResult = markdownDocument.render();
		panel.add(new JScrollPane(markdownDocument.render()));

		frame.add(panel);

		// frame.pack();
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
