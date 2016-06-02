package unify.timer;

public class TimerCallback {
		public String context;

		private native void onJniCallback(final String context);

		public void callback() {
				this.onJniCallback(this.context);
		}
}