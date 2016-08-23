package org.succlz123.s1go.app.api.bean;

/**
 * Created by succlz123 on 2015/4/20.
 */
public class SendInfo {
	private SetThreadsAndReviewsMessage Message;

	public SetThreadsAndReviewsMessage getMessage() {
		return Message;
	}

	public void setMessage(SetThreadsAndReviewsMessage message) {
		Message = message;
	}

	public class SetThreadsAndReviewsMessage {
		private String messagestr;
		private String messageval;

		public String getMessagestr() {
			return messagestr;
		}

		public void setMessagestr(String messagestr) {
			this.messagestr = messagestr;
		}

		public String getMessageval() {
			return messageval;
		}

		public void setMessageval(String messageval) {
			this.messageval = messageval;
		}
	}
}
