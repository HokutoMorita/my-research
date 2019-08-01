import java.io.*;
import com.morita.mhcat.servlet.*;
import com.morita.mhcat.servlet.http.*;


public class HelloSession2 extends HttpServlet {

    // HTML�ňӖ������������G�X�P�[�v���郆�[�e�B���e�B���\�b�h
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

        String introductionUserName = "���Ȃ��̖��O��" + userName + "�ł��B";
        String introductionGameName = "���" + gameName + "�ł��B";
        String introductionSessionId = "�Z�b�V����ID��" + session.getId() + "�ł��B";
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + escapeHtml("�Z�b�V�����I�u�W�F�N�g�̎擾") + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>" + escapeHtml("�Z�b�V�����I�u�W�F�N�g�̎擾(�y�[�W2)") + "</h1>");
        out.println("<p>" + introductionUserName + "</p>");
        out.println("<p>" + introductionGameName + "</p>");
        out.println("<p>" + introductionSessionId + "</p>");
        out.println("<a href=\"HelloSession\">" + escapeHtml("�O�̃T�[�u���b�g�ɖ߂�") + "</a>");
        out.println("</body>");
        out.println("</html>");
    }
}