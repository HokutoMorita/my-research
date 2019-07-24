package com.morita.mhcat.servletimpl;
import java.io.*;
import java.util.*;
import com.morita.mhcat.servlet.http.*;

public class HttpServletResponseImpl implements HttpServletResponse {
    String contentType = "application/octet-stream";
    private String characterEncoding = "ISO-8859-1";
    private OutputStream outputStream;
    PrintWriter printWriter;
    int status;
    String redirectLocation;
    ArrayList<Cookie> cookies = new ArrayList<Cookie>();

    HttpServletResponseImpl(OutputStream output) {
        this.outputStream = output;
        this.status = SC_OK;
    }

    /**
     * Content-Typeレスポンスヘッダを設定します。
     */
    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
        String[] temp = contentType.split(" *; *");
        if (temp.length > 1) {
            String[] keyValue = temp[1].split("=");
            if (keyValue.length == 2 && keyValue[0].equals("charset")) {
                setCharacterEncoding(keyValue[1]);
            }
        }
    }

    /**
     * 出力するHTMLの文字コードを設定します。
     */
    @Override
    public void setCharacterEncoding(String charset) {
	    this.characterEncoding = charset;
    }

    /**
     * レスポンスボディに出力するためのPrintWriterを取得します。
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream, this.characterEncoding));
        return this.printWriter;
    }

    /**
     * 指定したURLにリダイレクトします。
     */
    @Override
    public void sendRedirect(String location) {
        this.redirectLocation = location;
        setStatus(SC_FOUND);
    }

    /**
     * HTTPステータスコードを指定するメソッドを追加しまうs。
     */
    @Override
    public void setStatus(int sc) {
	    this.status = sc;
    }

    /**
     * クライアントに送り返すCookieを設定します。
     *  複数回呼び出すことで、複数のCookieをクライアントに送付できます。
     */
    @Override
    public void addCookie(Cookie cookie) {
	    this.cookies.add(cookie);
    }
}
