package com.morita.mhcat.servletimpl;
import java.util.*;
import java.io.*;
import java.nio.charset.*;

import com.morita.mhcat.nz.sodium.*;

import com.morita.mhcat.servlet.http.*;
import com.morita.mhcat.util.*;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
	private String characterEncoding = "ISO-8859-1";
	//private String characterEncoding = "UTF-8";
    private Map<String, String[]> parameterMap;
	private Cookie[] cookies;
	/** �Z�b�V������ێ����� */
	private Cell<HttpSessionImpl> session;
	/** �Z�b�V����ID��Cookie�ɕێ����邽�߂�response */
	private HttpServletResponseImpl response;
	/** �Z�b�V������Web�A�v���P�[�V�����P�ʂȂ̂ŁAWebApplication��ێ�����webApp */
	private WebApplication webApp;
	/** �Z�b�V����Cookie�̖��O */
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
	
	/**
     * GET/POST���\�b�h�����ʂ��܂��B
     * @return GET or POST
     */
    @Override
    public String getMethod() {
		return this.method;
    }

	/**
     * �p�����^���������Ƃ��Ď��A���̒l���擾���܂��B
     *  GET�̂Ƃ��̓N�G���X�g�����O�ɕt����ꂽGET�p�����^���擾���܂��B
     *  POST�̂Ƃ���POST�ő��t���ꂽ���N�G�X�g�{�f�B��POST�p�����^���擾���܂��B
     * @return �p�����^
     */
    @Override
    public String getParameter(String name) {
		String[] values = getParameterValues(name);
		if  (values == null) {
			return null;
		}
		return values[0];
    }
	
	/**
     * �����̕����̃p�����^���������Ƃ��A�����̒l�̂��ׂĂ�z��Ŏ擾���܂��B
     * @return �p�����^�z��
     */
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

	/**
     * �p�����^�̃G���R�[�f�B���O���w�肵�܂��B
     */
    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		if (!Charset.isSupported(env)) {
			throw new UnsupportedEncodingException("encoding.." + env);
		}
		this.characterEncoding = env;
    }
	
	/**
     * �N���C�A���g���瑗���Ă���Cookie��z��Ƃ��đS���擾���܂��B
     * @return Cookie�z��
     */
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
		return this.session.sample();
    }

    public HttpSession getSession(boolean create) {
		/**if (!create) {
			return this.session;
		}
		if (this.session == null) {
			SessionManager manager = this.webApp.getSessionManager();
			this.session = manager.createSession();
			addSessionCookie();
		}*/
		return this.session.sample();
    }

	/**
	 * �uJSESSIONID�v�Ƃ������O��Cookie����Z�b�V����ID�����o���ASessionManager���炻��ɕR�Â��Z�b�V�������擾���܂��B
	 * @return �Z�b�V����
	 */
    private Cell<HttpSessionImpl> getSessionInternal() {
		SessionManager manager = this.webApp.getSessionManager();
		if (this.cookies == null) {
			this.session = manager.createSession();
			addSessionCookie();
		}
		
		Cookie cookie = null;
		for (Cookie tempCookie : this.cookies) {
			if (tempCookie.getName().equals(SESSION_COOKIE_ID)) {
			cookie = tempCookie;
			}
		}
		
		if (cookie != null) {
			return manager.getSession(cookie.getValue());
		} else {
			return manager.createSession();
		}
    }

	/**
	 * �Z�b�V����ID�����X�|���X�w�b�_�Ƃ��ĕԂ����߂ɁACookie�ɒǉ����܂��B
	 *  ���̃��\�b�h�͐V���ɃZ�b�V�������쐬�����Ƃ��ƃR���X�g���N�^�ɂĊ��ɃZ�b�V�������������Ƃ��ɌĂяo����܂��B
	 */
    private void addSessionCookie() {
		// Cookie�I�u�W�F�N�g�𐶐����APath��HttpOnly��ݒ肵��HttpServletResponse�ɐݒ肵�܂��B
		Cookie cookie = new Cookie(SESSION_COOKIE_ID, this.session.sample().getId());
		cookie.setPath("/" + webApp.directory + "/");
		cookie.setHttpOnly(true);
		this.response.addCookie(cookie);
    }
}