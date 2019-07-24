package com.morita.mhcat.webserver;
import com.morita.mhcat.servletimpl.WebApplication;

import java.net.*;

public class Main {
    public static void main(String[] argv) throws Exception {
		WebApplication app = WebApplication.createInstance("testbbs");
		// WebApplicationにサーブレットを登録している。
		app.addServlet("/ShowBBS", "ShowBBS");
		app.addServlet("/PostBBS", "PostBBS");
		try (ServerSocket server = new ServerSocket(8001)) {
			for (;;) {
				// クライアントからの接続の待ち受けを行なっている。
				// accept()メソッドは、クライアントから接続されるまで返ってきません。
				Socket socket = server.accept();

				// TCP接続を待ち受け、accept()したら、ServerThreadにそのソケットを渡して、
				// 別スレッドで起動しています。
				ServerThread serverThread = new ServerThread(socket);
				Thread thread = new Thread(serverThread);
				thread.start();
			}
		}
    }
}
