package com.morita.mhcat.servletimpl;
import com.morita.mhcat.servlet.http.*;
import java.util.*;
import java.util.concurrent.*;

public class HttpSessionImpl implements HttpSession {
    private String id;

    /** セッションデータを保持します。 */
    private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    private volatile long lastAccessedTime;
    
    public HttpSessionImpl(String id) {
	    this.id = id;
	    this.access();
    }
    
    /** 
     * このセッションのセッションID(Cookieでやりとりされるもの)を取得します。 
     */
    public String getId() {
    	return this.id;
    }

    /**
     * 名前を指定してデータを登録します。
     */
    public void setAttribute(String name, Object value) {
        if (value == null) {
            removeAttribute(name);
            return;
        }
        this.attributes.put(name, value);
    }

    /**
     * 指定した名前で登録されたデータを取得します。
     */
    public Object getAttribute(String name) {
	    return this.attributes.get(name);
    }

    /**
     * このセッションに登録されているすべてのデータの名前を取得します。
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = new HashSet<String>();
        names.addAll(attributes.keySet());

        return Collections.enumeration(names);
    }

    /**
     * 指定した名前で登録されたデータを削除します。
     */
    public void removeAttribute(String name) {
    	this.attributes.remove(name);
    }

    /**
     * セッションに最後にアクセスした時刻を更新します。
     */
    synchronized void access() {
    	this.lastAccessedTime = System.currentTimeMillis();
    }

    long getLastAccessedTime() {
    	return this.lastAccessedTime;
    }
}
