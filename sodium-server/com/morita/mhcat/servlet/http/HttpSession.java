package com.morita.mhcat.servlet.http;
import java.util.*;

/**
 * �Z�b�V�����I�u�W�F�N�g��\������C���^�t�F�[�X
 */
public interface HttpSession {
    /** 
     * ���̃Z�b�V�����̃Z�b�V����ID(Cookie�ł��Ƃ肳������)���擾���܂��B 
     */
    String getId();

    /**
     * ���O���w�肵�ăf�[�^��o�^���܂��B
     */
    void setAttribute(String name, Object value);

    /**
     * �w�肵�����O�œo�^���ꂽ�f�[�^���擾���܂��B
     */
    Object getAttribute(String name);

    /**
     * ���̃Z�b�V�����ɓo�^����Ă��邷�ׂẴf�[�^�̖��O���擾���܂��B
     */
    Enumeration<String> getAttributeNames();

    /**
     * �w�肵�����O�œo�^���ꂽ�f�[�^���폜���܂��B
     */
    void removeAttribute(String name);
    
}
