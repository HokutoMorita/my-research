package com.morita.mhcat.servletimpl;
import java.util.*;
import java.io.*;
import com.morita.mhcat.servlet.http.*;
import com.morita.mhcat.util.*;

public class ServletService {

	/**
	 * URLClassLoaderクラスを使いサーブレットのクラスをロードして、クラスのnewInstance()メソッドで、サーブレットを生成します。
	 */
    private static HttpServlet createServlet(ServletInfo info) throws Exception {
		Class<?> clazz = info.webApp.classLoader.loadClass(info.servletClassName);
		return (HttpServlet)clazz.newInstance();
    }

    private static Map<String, String[]> stringToMap(String str) {
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		if (str != null) {
			String[] paramArray = str.split("&");
			for (String param : paramArray) {
				String[] keyValue = param.split("=", -1);
				if (parameterMap.containsKey(keyValue[0])) {
					String[] array = parameterMap.get(keyValue[0]);
					String[] newArray = new String[array.length + 1];
					System.arraycopy(array, 0, newArray, 0, array.length);
					newArray[array.length] = keyValue[1];
					parameterMap.put(keyValue[0], newArray);
				} else {
					parameterMap.put(keyValue[0], new String[] {keyValue[1]});
				}
			}
		}
		return parameterMap;
    }

    private static String readToSize(InputStream input, int size) throws Exception{
		int ch;
		StringBuilder sb = new StringBuilder();
		int readSize = 0;

		while (readSize < size && (ch = input.read()) != -1) {
			sb.append((char)ch);
			readSize++;
		}
		return sb.toString();
    }

    public static void doService(String method, String query, ServletInfo info,
	    			 Map<String, String> requestHeader,
	    			 InputStream input, OutputStream output)
		    throws Exception {
		// 該当のサーブレットの初回の呼び出しで、まだサーブレットのインスタンスが生成されていない場合、
		if (info.servlet == null) {
			// クラスファイルを動的にロードし、サーブレットを生成します。
			info.servlet = createServlet(info);
		}

		ByteArrayOutputStream outputBuffer =  new ByteArrayOutputStream();
		HttpServletResponseImpl resp = new HttpServletResponseImpl(outputBuffer);

		HttpServletRequest req;
		if (method.equals("GET")) {
			Map<String, String[]> map;
			map = stringToMap(query);
			req = new HttpServletRequestImpl("GET", requestHeader, map, resp, info.webApp);
		} else if (method.equals("POST")) {
			int contentLength = Integer.parseInt(requestHeader.get("CONTENT-LENGTH"));
			Map<String, String[]> map;
			String line = readToSize(input, contentLength);
			map = stringToMap(line);
			req = new HttpServletRequestImpl("POST", requestHeader, map, resp, info.webApp);
		} else {
			throw new AssertionError("BAD METHOD:" + method);
		}
				
		info.servlet.service(req, resp);

		if (resp.status == HttpServletResponse.SC_OK) {
			ResponseHeaderGenerator hg = new ResponseHeaderGeneratorImpl(resp.cookies);
			SendResponse.sendOkResponseHeader(output, resp.contentType, hg);
			resp.printWriter.flush(); // ファイルをクローズする前にデータをファイルに書き込みます。
			byte[] outputBytes = outputBuffer.toByteArray();
			for (byte b: outputBytes) {
				output.write((int)b);
			}
		} else if (resp.status == HttpServletResponse.SC_FOUND) {
			String redirectLocation;
			if (resp.redirectLocation.startsWith("/")) {
				String host = requestHeader.get("HOST");
				redirectLocation = "http://" + ((host != null) ? host : Constants.SERVER_NAME) + resp.redirectLocation;
			} else {
				redirectLocation = resp.redirectLocation;
			}
			SendResponse.sendFoundResponse(output, redirectLocation);
		}
    }
}
