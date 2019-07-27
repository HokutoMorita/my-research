package com.morita.mhcat.servlet.http;
import com.morita.mhcat.servlet.*;

/**
 * アプリケーションプログラマがこれを継承してサーブレットを作るための抽象クラス
 */
public class HttpServlet {
	/**
	 * GETを受け付けます
	 */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {}

	/**
	 * POSTを受け付けます
	 */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {}

	/**
	 * GETでもPOSTでもどちらでも呼び出されます
	 *  メソッドを見分け、doGet()、doPost()のいずれかを呼び出します。
	 */
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
		if (req.getMethod().equals("GET")) {
			doGet(req, resp);
		} else if (req.getMethod().equals("POST")) {
			doPost(req, resp);
		}
    }
}
