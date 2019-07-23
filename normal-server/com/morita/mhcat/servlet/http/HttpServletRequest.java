package com.morita.mhcat.servlet.http;
import java.io.*;

public interface HttpServletRequest {
    String getMethod();
    String getParameter(String name);
    String[] getParameterValues(String name);
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;
    Cookie[] getCookies();

    /**
     * セッションオブジェクトを取得します。
     *  存在していなければ作成して返します。
     */
    HttpSession getSession();

    /**
     * セッションオブジェクトを取得します。
     *  引数がfalseで、かつ存在していなければnullを返します。
     */
    HttpSession getSession(boolean create);
}
