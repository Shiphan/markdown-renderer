import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;
import java.util.List;

public class App {
	public static void main(String[] args) {
		var frame = new JFrame();
		var pane = new JTextPane();
		var doc = pane.getStyledDocument();

		var list = List.of(
			new Span("test here\n", null),
			new Span("test here\n", null)
		);

		for (var ele : list) {
			documentAppend(doc, ele.value, ele.style);
		}

		frame.add(pane);
		frame.setSize(200, 200);
		frame.setVisible(true);
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

class Span {
	public String value;
	public AttributeSet style;
	public Span(String value, AttributeSet style) {
		this.value = value;
		this.style = style;
	}
}
