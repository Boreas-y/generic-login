package org.boreas.common.login;

public final class Attributes {

	private Attributes() {
	}

	public static final String SESSION_LOGIN_ERR_MSG = "loginErrMsg";

	public static final String SESSION_LOGIN_ERR_COUNT = "loginErrCount";

	public static final String SESSION_LOGIN_USERNAME = "loginUsername";

	public static final String AJAX_HANDLE_KEY = "ajax";

	public static final String LOGIN_USER = "user";

	public static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";

	public static final String LOGIN_JSON_RESULT = "{\"result\":%d,\"errCount\":%d,\"msg\":\"%s\"}";

	public static final String getLoginJsonResult(int resultCode, int errCount,
			String msg) {
		return String.format(LOGIN_JSON_RESULT, resultCode, errCount, msg);
	}
}
