package com.morita.mhcat.servlet.http;
import java.io.*;

/**
 * HTTPリクエストを表現するインタフェースで、doGet()やdoPost()に引数で渡されます。
 */
public interface HttpServletRequest {
    /**
     * GET/POSTメソッドを識別します。
     * @return GET or POST
     */
    String getMethod();

    /**
     * パラメタ名を引数として私、その値を取得します。
     *  GETのときはクエリストリングに付けられたGETパラメタを取得します。
     *  POSTのときはPOSTで送付されたリクエストボディのPOSTパラメタを取得します。
     * @return パラメタ
     */
    String getParameter(String name);

    /**
     * 同名の複数のパラメタがあったとき、それらの値のすべてを配列で取得します。
     * @return パラメタ配列
     */
    String[] getParameterValues(String name);

    /**
     * パラメタのエンコーディングを指定します。
     */
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;

    /**
     * クライアントから送られてきたCookieを配列として全件取得します。
     * @return Cookie配列
     */
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
