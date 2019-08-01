import java.io.*;
import com.morita.mhcat.servlet.*;
import com.morita.mhcat.servlet.http.*;


public class HelloSession extends HttpServlet {

    // HTMLで意味を持つ文字をエスケープするユーティリティメソッド
    private String escapeHtml(String src) {
        return src.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"",  "&quot;")
                .replace("'",  "&#39;");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("UserName");
        String gameName = request.getParameter("GameName");
        HttpSession session = request.getSession();

        // 既にセッションを保持している場合の処理
        if (userName == null && gameName == null && session != null) {
            userName = (String) session.getAttribute("UserName");
            gameName = (String) session.getAttribute("GameName");
        } else {
            session.setAttribute("UserName", userName);
            session.setAttribute("GameName", gameName);
        }
        
        System.out.println(userName);
        System.out.println(gameName);
        
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
        out.println("<h1>" + escapeHtml("セッションオブジェクトの取得(ページ1)") + "</h1>");
        out.println("<p>" + introductionUserName + "</p>");
        out.println("<p>" + introductionGameName + "</p>");
        out.println("<p>" + introductionSessionId + "</p>");
        out.println("<a href=\"HelloSession2\">" + escapeHtml("次のサーブレット呼び出し") + "</a>");
        out.println("</body>");
        out.println("</html>");
    }
}