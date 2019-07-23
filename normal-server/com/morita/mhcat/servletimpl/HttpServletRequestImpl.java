package com.morita.mhcat.servletimpl;
import java.util.*;
import java.io.*;
import java.nio.charset.*;

import com.morita.mhcat.servlet.http.*;
import com.morita.mhcat.util.*;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
    private String characterEncoding = "ISO-8859-1";
    private Map<String, String[]> parameterMap;
	private Cookie[] cookies;
	/** セッションを保持する */
	private HttpSessionImpl session;
	/** セッションIDをCookieに保持するためのresponse */
	private HttpServletResponseImpl response;
	/** セッションはWebアプリケーション単位なので、WebApplicationを保持するwebApp */
	private WebApplication webApp;
	/** セッションCookieの名前 */
	private final String SESSION_COOKIE_ID = "JSESSIONID";
	
	HttpServletRequestImpl(String method, 
					Map<String, String> requestHeader,
	    		   	Map<String, String[]> parameterMap,
	    		   	HttpServletResponseImpl resp,
	    		   	WebApplication webApp) {
		this.method = method;
		this.parameterMap = parameterMap;
		this.cookies = parseCookies(requestHeader.get("COOKIE"));
		this.response = resp;
		this.webApp = webApp;
		this.session = getSessionInternal();
		if (this.session != null) {
			addSessionCookie();
		}
	}
	
    @Override
    public String getMethod() {
		return this.method;
    }

    @Override
    public String getParameter(String name) {
		String[] values = getParameterValues(name);
		if  (values == null) {
			return null;
		}
		return values[0];
    }
    
    @Override
    public String[] getParameterValues(String name) {
		String[] values = this.parameterMap.get(name);
		if  (values == null) {
			return null;
		}
		String[] decoded = new String[values.length];
		try {
			for (int i = 0; i < values.length; i++) {
			decoded[i] = MyURLDecoder.decode(values[i], this.characterEncoding);
			}
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
		return decoded;	
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		if (!Charset.isSupported(env)) {
			throw new UnsupportedEncodingException("encoding.." + env);
		}
		this.characterEncoding = env;
    }
    
    @Override
    public Cookie[] getCookies() {
		return this.cookies;
    }

    private static Cookie[] parseCookies(String cookieString) {
		if (cookieString == null) {
			return null;
		}
		String[] cookiePairArray = cookieString.split(";");
		Cookie[] ret = new Cookie[cookiePairArray.length];
		int cookieCount = 0;

		for (String cookiePair : cookiePairArray) {
			String[] pair = cookiePair.split("=", 2);

			ret[cookieCount] = new Cookie(pair[0], pair[1]);
			cookieCount++;
		}

		return ret;
    }

    public HttpSession getSession() {
		return getSession(true);
    }

    public HttpSession getSession(boolean create) {
		if (!create) {
			return this.session;
		}
		if (this.session == null) {
			SessionManager manager = this.webApp.getSessionManager();
			this.session = manager.createSession();
			addSessionCookie();
		}
		return this.session;
    }

	/**
	 * 「JSESSIONID」という名前のCookieからセッションIDを取り出し、SessionManagerからそれに紐づくセッションを取得します。
	 * @return セッション
	 */
    private HttpSessionImpl getSessionInternal() {
		if (this.cookies == null) {
			return null;
		}
		Cookie cookie = null;
		for (Cookie tempCookie : this.cookies) {
			if (tempCookie.getName().equals(SESSION_COOKIE_ID)) {
			cookie = tempCookie;
			}
		}
		SessionManager manager = this.webApp.getSessionManager();
		HttpSessionImpl ret = null;
		if (cookie != null) {
			ret = manager.getSession(cookie.getValue());
		}
		return ret;
    }

	/**
	 * セッションIDをレスポンスヘッダとして返すために、Cookieに追加します。
	 *  このメソッドは新たにセッションを作成したときとコンストラクタにて既にセッションがあったときに呼び出されます。
	 */
    private void addSessionCookie() {
		// Cookieオブジェクトを生成し、PathとHttpOnlyを設定してHttpServletResponseに設定します。
		Cookie cookie = new Cookie(SESSION_COOKIE_ID, this.session.getId());
		cookie.setPath("/" + webApp.directory + "/");
		cookie.setHttpOnly(true);
		this.response.addCookie(cookie);
    }
}
