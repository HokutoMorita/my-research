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
     * Content-Type���X�|���X�w�b�_��ݒ肵�܂��B
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
     * �o�͂���HTML�̕����R�[�h��ݒ肵�܂��B
     */
    @Override
    public void setCharacterEncoding(String charset) {
	    this.characterEncoding = charset;
    }

    /**
     * ���X�|���X�{�f�B�ɏo�͂��邽�߂�PrintWriter���擾���܂��B
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream, this.characterEncoding));
        return this.printWriter;
    }

    /**
     * �w�肵��URL�Ƀ��_�C���N�g���܂��B
     */
    @Override
    public void sendRedirect(String location) {
        this.redirectLocation = location;
        setStatus(SC_FOUND);
    }

    /**
     * HTTP�X�e�[�^�X�R�[�h���w�肷�郁�\�b�h��ǉ����܂�s�B
     */
    @Override
    public void setStatus(int sc) {
	    this.status = sc;
    }

    /**
     * �N���C�A���g�ɑ���Ԃ�Cookie��ݒ肵�܂��B
     *  ������Ăяo�����ƂŁA������Cookie���N���C�A���g�ɑ��t�ł��܂��B
     */
    @Override
    public void addCookie(Cookie cookie) {
	    this.cookies.add(cookie);
    }
}
