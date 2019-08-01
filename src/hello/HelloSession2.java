import java.io.*;
import com.morita.mhcat.servlet.*;
import com.morita.mhcat.servlet.http.*;


public class HelloSession2 extends HttpServlet {

    // HTMLで意味を持つ文字をエスケープするユーティリティメソッド
    private String escapeHtml(String src) {
        return src.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"",  "&quot;")
                .replace("'",  "&#39;");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = "";
        String gameName = "";
        
        HttpSession session = request.getSession();
        userName = (String) session.getAttribute("UserName");
        gameName = (String) session.getAttribute("GameName");

        String introductionUserName = "あなたの名前は" + userName + "です。";
        String introductionGameName = "趣味は" + gameName + "です。";
        String introductionSessionId = "セッションIDは" + session.getId() + "です。";
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + escapeHtml("セッションオブジェクトの取得") + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>" + escapeHtml("セッションオブジェクトの取得(ページ2)") + "</h1>");
        out.println("<p>" + introductionUserName + "</p>");
        out.println("<p>" + introductionGameName + "</p>");
        out.println("<p>" + introductionSessionId + "</p>");
        out.println("<a href=\"HelloSession\">" + escapeHtml("前のサーブレットに戻る") + "</a>");
        out.println("</body>");
        out.println("</html>");
    }
}