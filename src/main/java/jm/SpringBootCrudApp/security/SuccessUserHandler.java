package jm.SpringBootCrudApp.security;

import jm.SpringBootCrudApp.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute("user", user);
        httpSession.setAttribute("roles", authentication.getAuthorities());

        if (authentication
                .getAuthorities()
                .stream()
                .anyMatch(role -> role
                        .getAuthority()
                        .equals("ROLE_ADMIN"))) {
            httpServletResponse
                    .sendRedirect("/admin");
        } else {
            httpServletResponse
                    .sendRedirect("/user");

        }
    }
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }

        return null;
    }

}

