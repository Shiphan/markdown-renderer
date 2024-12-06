package markdownDocument.span;

import javax.swing.text.StyledDocument;

public interface Text extends Span {
	public class StyledText implements Text {
		private String content;
		private boolean bold;
		private boolean ltalic;
		public StyledText(String content, boolean bold, boolean ltalic) {
			this.content = content;
			this.bold = bold;
			this.ltalic = ltalic;
		}
		@Override
		public void render(StyledDocument doc) {

		}
	}
	public class Code implements Text {
		private String content;
		public Code(String content) {
			this.content = content;
		}
		@Override
		public void render(StyledDocument doc) {
		}
	}
}
