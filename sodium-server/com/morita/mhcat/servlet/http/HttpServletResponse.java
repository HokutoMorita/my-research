package com.morita.mhcat.servlet.http;
import java.io.*;

/**
 * ���X�|���X�Ɋ֘A���鑀����܂Ƃ߂��C���^�t�F�[�X
 */
public interface HttpServletResponse {
    static final int SC_OK = 200;
    static final int SC_FOUND = 302;

    /**
     * Content-Type���X�|���X�w�b�_��ݒ肵�܂��B
     */
    void setContentType(String contentType);

    /**
     * �o�͂���HTML�̕����R�[�h��ݒ肵�܂��B
     */
    void setCharacterEncoding(String charset);

    /**
     * ���X�|���X�{�f�B�ɏo�͂��邽�߂�PrintWriter���擾���܂��B
     */
    PrintWriter getWriter() throws IOException;

    /**
     * �w�肵��URL�Ƀ��_�C���N�g���܂��B
     */
    void sendRedirect(String location);

    /**
     * HTTP�X�e�[�^�X�R�[�h���w�肷�郁�\�b�h��ǉ����܂�s�B
     */
    void setStatus(int sc);

    /**
     * �N���C�A���g�ɑ���Ԃ�Cookie��ݒ肵�܂��B
     *  ������Ăяo�����ƂŁA������Cookie���N���C�A���g�ɑ��t�ł��܂��B
     */
    void addCookie(Cookie cookie);
}
