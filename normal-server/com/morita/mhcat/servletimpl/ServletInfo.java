package com.morita.mhcat.servletimpl;
import com.morita.mhcat.servlet.http.*;

public class ServletInfo {
    WebApplication webApp;
    String urlPattern;
    String servletClassName;
    HttpServlet servlet;

    public ServletInfo(WebApplication webApp, String urlPattern,
                       String servletClassName) {
	this.webApp = webApp;
	this.urlPattern = urlPattern;
	this.servletClassName = servletClassName;
    }
}
