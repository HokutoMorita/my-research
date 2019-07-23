package com.morita.mhcat.servlet.http;
import java.io.*;

public interface HttpServletRequest {
    String getMethod();
    String getParameter(String name);
    String[] getParameterValues(String name);
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;
    Cookie[] getCookies();

    /**
     * �Z�b�V�����I�u�W�F�N�g���擾���܂��B
     *  ���݂��Ă��Ȃ���΍쐬���ĕԂ��܂��B
     */
    HttpSession getSession();

    /**
     * �Z�b�V�����I�u�W�F�N�g���擾���܂��B
     *  ������false�ŁA�����݂��Ă��Ȃ����null��Ԃ��܂��B
     */
    HttpSession getSession(boolean create);
}
