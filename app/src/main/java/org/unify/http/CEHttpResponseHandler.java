package org.unify.http;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class CEHttpResponseHandler extends AsyncHttpResponseHandler {
	public String context;

	private native void onJniSuccess(final String context,
			final int statusCode, final String responseString);

	private native void onJniFailure(final String context,
			final int statusCode, final String errorMessage);

	@Override
	public void onFailure(int statusCode,
			cz.msebera.android.httpclient.Header[] arg1, byte[] responseData,
			Throwable arg3) {
		if (responseData != null) {
			String responseString = new String(responseData);
			this.onJniFailure(this.context, statusCode, responseString);
		} else {
			this.onJniFailure(this.context, statusCode, null);
		}
	}

	@Override
	public void onSuccess(int statusCode,
			cz.msebera.android.httpclient.Header[] arg1, byte[] responseData) {
		String responseString = new String(responseData);
		this.onJniSuccess(this.context, statusCode, responseString);
	}
}
