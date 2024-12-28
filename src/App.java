import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import example.Example;
import markdownDocument.MarkdownDocument;
import markdownDocument.block.Block;
import markdownDocument.block.Heading.Level;
import markdownDocument.block.Heading;
import markdownDocument.block.HorizontalRule;
import markdownDocument.block.Paragraph;
import markdownDocument.block.Quote;
import markdownDocument.span.Text.StyledText;
import markdownDocument.span.LineBreak;
import markdownDocument.span.List.ListItem;
import parser.Parser;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class App {
	static boolean debug = false;

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("--debug")) {
			debug = true;
		}
		System.setProperty("sun.java2d.uiScale", "2");
		SwingUtilities.invokeLater(() -> initUI());
	}
	private static void initUI() {
		final var frame = new JFrame("Markdown Renderer");
		final var panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		final var editor = new Editor();
		final var renderPane = new JScrollPane();
		final var latestRenderResultId = new AtomicLong(0);
		final var renderPaneLatest = new AtomicLong(0);

		var markdownDocument = new MarkdownDocument(List.of(
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
			)),
			new Paragraph(List.of(
				new StyledText("text"),
				new StyledText("Bold", true, false),
				new StyledText("italic", false, true),
				new StyledText("Bold&italic", true, true),
				new LineBreak(),
				new markdownDocument.span.List(List.of(
					new ListItem(new Paragraph(List.of(
						new StyledText("abc"),
						new LineBreak()
					))),
					new ListItem(
						new Paragraph(List.of(
							new StyledText("abc"),
							new StyledText("abc", true, false),
							new StyledText("abc"),
							new LineBreak()
						)),
						List.of(
							new Paragraph(List.of(
								new StyledText("def"),
								new StyledText("def", true, false),
								new StyledText("def")
							))
						)
					)
				))
			))
		));

		//renderPane.setViewportView(markdownDocument.render());
		renderPane.setViewportView(new MarkdownDocument().render());

		//renderPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//editor.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		renderPane.setMinimumSize(new Dimension(0, 0));
		editor.setMinimumSize(new Dimension(0, 0));

		editor.setListener((e) -> {
			//System.out.println(e.toString());
			final var id = latestRenderResultId.incrementAndGet();
			final var text = editor.getText();
			new Thread(() -> {
				final var markdown = new Parser(text).toMarkdownDocument();
				final var renderResult = markdown.render();
				// final var renderResult = new Parser(text).toMarkdownDocument().render();
				if (id > renderPaneLatest.get()) {
					if (debug) {
						System.out.printf("updated id: %d\n", id);
						System.out.println(markdown);
						System.out.println("----------");
					}
					SwingUtilities.invokeLater(() -> {
						renderPane.setViewportView(renderResult);
					});
					renderPaneLatest.set(id);
				}
			}).start();
		});

		// panel.add(new JScrollPane(Example.toJTextPane()));
		panel.add(editor);
		panel.add(renderPane);

		frame.add(panel);

		// frame.pack();
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class Editor extends JScrollPane {
	private final JTextArea textArea;

	public Editor() {
		this((e) -> {});
	}
	public Editor(Consumer<DocumentEvent> listener) {
		super();
		this.textArea = new JTextArea();
		this.setViewportView(this.textArea);
		this.textArea.setLineWrap(true);
		this.setListener(listener);
	}
	public void setListener(Consumer<DocumentEvent> listener) {
		this.textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				listener.accept(e);
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				listener.accept(e);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				listener.accept(e);
			}
		});
	}
	public String getText() {
		return this.textArea.getText();
	}
}
