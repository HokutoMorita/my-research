package com.morita.mhcat.servlet.http;
import com.morita.mhcat.servlet.*;

/**
 * �A�v���P�[�V�����v���O���}��������p�����ăT�[�u���b�g����邽�߂̒��ۃN���X
 */
public class HttpServlet {
	/**
	 * GET���󂯕t���܂�
	 */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {}

	/**
	 * POST���󂯕t���܂�
	 */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {}

	/**
	 * GET�ł�POST�ł��ǂ���ł��Ăяo����܂�
	 *  ���\�b�h���������AdoGet()�AdoPost()�̂����ꂩ���Ăяo���܂��B
	 */
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
		if (req.getMethod().equals("GET")) {
			doGet(req, resp);
		} else if (req.getMethod().equals("POST")) {
			doPost(req, resp);
		}
    }
}
