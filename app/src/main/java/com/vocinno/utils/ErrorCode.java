package com.vocinno.utils;

public class ErrorCode {

	public final static int ERROR_CODE_CORRECT = 0;
	public final static int ERROR_CODE_SQL_EXCEPTION = 9001;
	public final static String ERROR_CODE_SQL_EXCEPTION_STRING = "用户太拥挤了，请稍候一下下";

	public final static int ERROR_CODE_SYSTEM_EXCEPTION = 9003;
	public final static int ERROR_CODE_DATA_LOAD_ERROR = 9004;
	public final static int ERROR_CODE_SERVER_DOWN = 9005;
	public final static int ERROR_CODE_MD5_FAIL = 9006;
	public final static int ERROR_CODE_FILE_SAVE_FAIL = 9007;
	public final static int ERROR_SQL_TEXT_ERROR = 9008; // 发表帖子，评论，标签的时候，出现表情符号，会报这个错误

	public final static int ERROR_CODE_CLIENT_DATA_ERROR = 8001;
	public final static int ERROR_CODE_USER_NOT_FOUND = 8003;
	public final static int ERROR_CODE_USER_PASSWORD_ERROR = 8004;
	public final static int ERROR_CODE_USER_DUPLICATED = 8005;
	public final static int ERROR_CODE_USER_AUTHORITY_FAIL = 8006;
	public final static int ERROR_CODE_USER_SESSION_TIMEOUT = 8007;
	public final static int ERROR_CODE_EMAIL_DUPLICATED = 8008;
	public final static int ERROR_CODE_USER_NO_AUTHORITY = 8009;
	public final static int ERROR_CODE_USER_NO_FRIEND = 8010;
	public final static int ERROR_CODE_USER_IS_BLOCKED = 8011;

	// 数据库获取不到数据，数据不存在
	public final static int ERROR_CODE_CLIENT_DATA_ERROR_POSTID = 8101;
	public final static int ERROR_CODE_CLIENT_DATA_ERROR_VOTE = 8102;
	public final static int ERROR_CODE_CLIENT_DATA_ERROR_TAG = 8103;
	public final static int ERROR_CODE_CLIENT_DATA_ERROR_COMMENT = 8104;
	public final static int ERROR_CODE_CLIENT_DATA_ERROR_EVENT = 8104;
	public final static int ERROR_CODE_CLIENT_DATA_ERROR_POST_ANALYSIS = 8105;

	// 文件错误
	public final static int ERROR_CODE_FILE_CONTENT_ERROR = 8201;

	// Like已经存在
	public final static int ERROR_CODE_LIKE_EXISTED = 8301;

	public final static String ERROR_CODE_USER_NOT_FOUND_STRING = "账户不存在，请重新输入";
	public final static String ERROR_CODE_USER_PASSWORD_ERROR_STRING = "密码错误，请重新输入";
	public final static String ERROR_CODE_USER_DUPLICATED_STRING = "这里每个人都是独一无二的，快换一个吧";
	public final static String ERROR_CODE_USER_AUTHORITY_FAIL_STRING = "为确保账号安全，请重新登录";
	public final static String ERROR_CODE_EMAIL_DUPLICATED_STRING = "该账户已被注册哦，请再试一次哦";
	public final static String ERROR_CODE_USER_NO_AUTHORITY_STRING = "您的权限不够，请联系管理员";
	public final static String ERROR_CODE_USER_IS_BLOCKED_STRING = "您的账户已经被屏蔽";

	public final static String ERROR_GOT_EXCEPTIION_INSERT_POST = "骚瑞，您输入的部分文字暂时无法识别，请重新输入";

}
