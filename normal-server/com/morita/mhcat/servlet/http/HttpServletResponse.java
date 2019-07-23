package com.morita.mhcat.servlet.http;
import java.io.*;

public interface HttpServletResponse {
    static final int SC_OK = 200;
    static final int SC_FOUND = 302;

    void setContentType(String contentType);
    void setCharacterEncoding(String charset);
    PrintWriter getWriter() throws IOException;
    void sendRedirect(String location);
    void setStatus(int sc);
    void addCookie(Cookie cookie);
}
