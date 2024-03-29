package com.morita.mhcat.servletimpl;
import com.morita.mhcat.servlet.http.*;
import com.morita.mhcat.util.*;
import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Set-Cookieヘッダを生成するクラス
 */
class ResponseHeaderGeneratorImpl implements ResponseHeaderGenerator {
	private ArrayList<Cookie> cookies;
	
	ResponseHeaderGeneratorImpl(ArrayList<Cookie> cookies) {
		this.cookies = cookies;
    }

    private static String getCookieDateString(Calendar cal) {
		DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss", Locale.US);
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime()) + " GMT";
    }

	/**
	 * Set-Cookieヘッダを生成します。
	 */
    public void generate(OutputStream output) throws IOException {
		for (Cookie cookie : cookies) {
			String header;
			header = "Set-Cookie: " + cookie.getName() + "=" + cookie.getValue();

			if (cookie.getDomain() != null) {
				header += "; Domain=" + cookie.getDomain();
			}
			if (cookie.getMaxAge() > 0) {
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				cal.add(Calendar.SECOND, cookie.getMaxAge());
				header += "; Expires=" + getCookieDateString(cal);
			} else if (cookie.getMaxAge() == 0) {
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				cal.set(1970, 0, 1, 0, 0, 10);
				header += "; Expires=" + getCookieDateString(cal);
			}
			if (cookie.getPath() != null) {
				header += "; Path=" + cookie.getPath();
			}
			if (cookie.getSecure()) {
				header += "; Secure";
			}
			if (cookie.isHttpOnly()) {
				header += "; HttpOnly";
			}
			Util.writeLine(output, header);
		}
    }
}