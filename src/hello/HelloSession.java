import java.io.*;
import com.morita.mhcat.servlet.*;
import com.morita.mhcat.servlet.http.*;


public class HelloSession extends HttpServlet {

    // HTML�ňӖ������������G�X�P�[�v���郆�[�e�B���e�B���\�b�h
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

        // ���ɃZ�b�V������ێ����Ă���ꍇ�̏���
        if (userName == null && gameName == null && session != null) {
            userName = (String) session.getAttribute("UserName");
            gameName = (String) session.getAttribute("GameName");
        } else {
            session.setAttribute("UserName", userName);
            session.setAttribute("GameName", gameName);
        }
        
        System.out.println(userName);
        System.out.println(gameName);
        
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
        out.println("<h1>" + escapeHtml("�Z�b�V�����I�u�W�F�N�g�̎擾(�y�[�W1)") + "</h1>");
        out.println("<p>" + introductionUserName + "</p>");
        out.println("<p>" + introductionGameName + "</p>");
        out.println("<p>" + introductionSessionId + "</p>");
        out.println("<a href=\"HelloSession2\">" + escapeHtml("���̃T�[�u���b�g�Ăяo��") + "</a>");
        out.println("</body>");
        out.println("</html>");
    }
}