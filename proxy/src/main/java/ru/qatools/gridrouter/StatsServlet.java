package ru.qatools.gridrouter;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnServletContext;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 */
@WebServlet(urlPatterns = {"/stats"}, asyncSupported = true)
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"user"}))
public class StatsServlet extends HttpServlet {

    @Autowired
    private SessionStorage sessionStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(SC_OK);
        response.setContentType(TEXT_PLAIN_VALUE);
        try (OutputStream output = response.getOutputStream()) {
            int count = sessionStorage.getCountFor(request.getRemoteUser());
            IOUtils.write(String.valueOf(count), output, UTF_8);
        }
    }
}
