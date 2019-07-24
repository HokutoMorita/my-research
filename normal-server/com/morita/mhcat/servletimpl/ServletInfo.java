package com.morita.mhcat.servletimpl;
import com.morita.mhcat.servlet.http.*;

/**
 * Main.java�œo�^���Ă���URL�p�^�[����T�[�u���b�g�̃N���X���̂ق��A
 * �e�ł���WebApplication�A�����HttpServlet��ێ����Ă��܂��B
 */
public class ServletInfo {
    WebApplication webApp;
    String urlPattern;
    String servletClassName;
    HttpServlet servlet;

    public ServletInfo(WebApplication webApp, String urlPattern, String servletClassName) {
        this.webApp = webApp;
        this.urlPattern = urlPattern;
        this.servletClassName = servletClassName;
    }
}
