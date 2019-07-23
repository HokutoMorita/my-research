package com.morita.mhcat.servlet.http;
import java.util.*;

/**
 * セッションオブジェクトを表現するインタフェース
 */
public interface HttpSession {
    /** 
     * このセッションのセッションID(Cookieでやりとりされるもの)を取得します。 
     */
    String getId();

    /**
     * 名前を指定してデータを登録します。
     */
    void setAttribute(String name, Object value);

    /**
     * 指定した名前で登録されたデータを取得します。
     */
    Object getAttribute(String name);

    /**
     * このセッションに登録されているすべてのデータの名前を取得します。
     */
    Enumeration<String> getAttributeNames();

    /**
     * 指定した名前で登録されたデータを削除します。
     */
    void removeAttribute(String name);
    
}
