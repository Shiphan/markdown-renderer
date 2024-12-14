import java.util.Arrays;
import java.util.List;

import example.Example;
import markdownDocument.MarkdownDocument;
import markdownDocument.Util;
import markdownDocument.block.Heading.Level;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Quote;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class App {
	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale", "1.5");

		var frame = new JFrame("Markdown Renderer");
		var panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		var markdownDocument = new MarkdownDocument(
			List.of(
				/*
				new Heading("test here", Level.h1),
				new Heading("test here", Level.h2),
				new Heading("test here", Level.h3),
				new Heading("test here", Level.h4),
				new Heading("test here", Level.h5),
				new Heading("test here", Level.h6)
				*/
				new HorizontalRule()
				/*
				new Quote(List.of(
					new Heading("test", Level.h3),
					new Quote(List.of(
						new Heading("test2", Level.h5)
						//new HorizontalRule()
					))
					//new HorizontalRule(),
					//new HorizontalRule()
				))
				*/
			)
		);

		// panel.add(new JScrollPane(Example.toJTextPane()));
		// panel.add(markdownDocument.render());
		// final var renderResult = markdownDocument.render();
		panel.add(new JScrollPane(markdownDocument.render()));
		//panel.add(markdownDocument.render());

		//panel.add(new JScrollPane(new MarkdownDocument(List.of(
		//	new Heading("Test heading", Level.h2),
		//	new Heading("Test heading", Level.h2)
		//)).render()));

		final var testPane = new JTextPane();
		final var doc = testPane.getDocument();
		try {
			doc.insertString(doc.getLength(), "LKSJDFLKAJLSKDJALKJDLWKAJLK\n", null);

			Util.setSelectionToEnd(testPane);
			testPane.insertComponent(new JLabel("abc"));
			doc.insertString(doc.getLength(), "\n", null);

			doc.insertString(doc.getLength(), "LKSJDFLKAJLSKDJALKJDLWKAJLK\n", null);

			Util.setSelectionToEnd(testPane);
			testPane.insertComponent(new JSeparator());
			doc.insertString(doc.getLength(), "\n", null);

			Util.setSelectionToEnd(testPane);
			testPane.insertComponent(new JLabel("abc"));
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
		//panel.add(new JScrollPane(testPane));

		frame.add(panel);

		// frame.pack();
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
