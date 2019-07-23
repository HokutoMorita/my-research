package com.morita.mhcat.servletimpl;
import com.morita.mhcat.servlet.http.*;
import java.util.*;
import java.util.concurrent.*;

public class HttpSessionImpl implements HttpSession {
    private String id;

    /** �Z�b�V�����f�[�^��ێ����܂��B */
    private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    private volatile long lastAccessedTime;
    
    public HttpSessionImpl(String id) {
	    this.id = id;
	    this.access();
    }
    
    /** 
     * ���̃Z�b�V�����̃Z�b�V����ID(Cookie�ł��Ƃ肳������)���擾���܂��B 
     */
    public String getId() {
    	return this.id;
    }

    /**
     * ���O���w�肵�ăf�[�^��o�^���܂��B
     */
    public void setAttribute(String name, Object value) {
        if (value == null) {
            removeAttribute(name);
            return;
        }
        this.attributes.put(name, value);
    }

    /**
     * �w�肵�����O�œo�^���ꂽ�f�[�^���擾���܂��B
     */
    public Object getAttribute(String name) {
	    return this.attributes.get(name);
    }

    /**
     * ���̃Z�b�V�����ɓo�^����Ă��邷�ׂẴf�[�^�̖��O���擾���܂��B
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = new HashSet<String>();
        names.addAll(attributes.keySet());

        return Collections.enumeration(names);
    }

    /**
     * �w�肵�����O�œo�^���ꂽ�f�[�^���폜���܂��B
     */
    public void removeAttribute(String name) {
    	this.attributes.remove(name);
    }

    /**
     * �Z�b�V�����ɍŌ�ɃA�N�Z�X�����������X�V���܂��B
     */
    synchronized void access() {
    	this.lastAccessedTime = System.currentTimeMillis();
    }

    long getLastAccessedTime() {
    	return this.lastAccessedTime;
    }
}
