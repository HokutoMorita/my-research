package com.morita.mhcat.webserver;
import com.morita.mhcat.servletimpl.WebApplication;

import java.net.*;

public class Main {
    public static void main(String[] argv) throws Exception {
		WebApplication app = WebApplication.createInstance("testbbs");
		// WebApplication�ɃT�[�u���b�g��o�^���Ă���B
		app.addServlet("/ShowBBS", "ShowBBS");
		app.addServlet("/PostBBS", "PostBBS");
		try (ServerSocket server = new ServerSocket(8001)) {
			for (;;) {
				// �N���C�A���g����̐ڑ��̑҂��󂯂��s�Ȃ��Ă���B
				// accept()���\�b�h�́A�N���C�A���g����ڑ������܂ŕԂ��Ă��܂���B
				Socket socket = server.accept();

				// TCP�ڑ���҂��󂯁Aaccept()������AServerThread�ɂ��̃\�P�b�g��n���āA
				// �ʃX���b�h�ŋN�����Ă��܂��B
				ServerThread serverThread = new ServerThread(socket);
				Thread thread = new Thread(serverThread);
				thread.start();
			}
		}
    }
}
