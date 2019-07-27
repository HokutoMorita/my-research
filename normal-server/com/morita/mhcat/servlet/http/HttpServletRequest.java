package com.morita.mhcat.servlet.http;
import java.io.*;

/**
 * HTTP���N�G�X�g��\������C���^�t�F�[�X�ŁAdoGet()��doPost()�Ɉ����œn����܂��B
 */
public interface HttpServletRequest {
    /**
     * GET/POST���\�b�h�����ʂ��܂��B
     * @return GET or POST
     */
    String getMethod();

    /**
     * �p�����^���������Ƃ��Ď��A���̒l���擾���܂��B
     *  GET�̂Ƃ��̓N�G���X�g�����O�ɕt����ꂽGET�p�����^���擾���܂��B
     *  POST�̂Ƃ���POST�ő��t���ꂽ���N�G�X�g�{�f�B��POST�p�����^���擾���܂��B
     * @return �p�����^
     */
    String getParameter(String name);

    /**
     * �����̕����̃p�����^���������Ƃ��A�����̒l�̂��ׂĂ�z��Ŏ擾���܂��B
     * @return �p�����^�z��
     */
    String[] getParameterValues(String name);

    /**
     * �p�����^�̃G���R�[�f�B���O���w�肵�܂��B
     */
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;

    /**
     * �N���C�A���g���瑗���Ă���Cookie��z��Ƃ��đS���擾���܂��B
     * @return Cookie�z��
     */
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
