package markdownDocument;

import javax.swing.JTextPane;

public class Util {
	public static void setSelectionToEnd(JTextPane textPane) {
		final var end = textPane.getText().length();
		textPane.setSelectionStart(end);
		textPane.setSelectionEnd(end);
	}
}
