package com.morita.mhcat.servlet.http;
import java.io.*;

/**
 * レスポンスに関連する操作をまとめたインタフェース
 */
public interface HttpServletResponse {
    static final int SC_OK = 200;
    static final int SC_FOUND = 302;

    /**
     * Content-Typeレスポンスヘッダを設定します。
     */
    void setContentType(String contentType);

    /**
     * 出力するHTMLの文字コードを設定します。
     */
    void setCharacterEncoding(String charset);

    /**
     * レスポンスボディに出力するためのPrintWriterを取得します。
     */
    PrintWriter getWriter() throws IOException;

    /**
     * 指定したURLにリダイレクトします。
     */
    void sendRedirect(String location);

    /**
     * HTTPステータスコードを指定するメソッドを追加しまうs。
     */
    void setStatus(int sc);

    /**
     * クライアントに送り返すCookieを設定します。
     *  複数回呼び出すことで、複数のCookieをクライアントに送付できます。
     */
    void addCookie(Cookie cookie);
}
