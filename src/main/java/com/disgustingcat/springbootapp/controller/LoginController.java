package com.disgustingcat.springbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.disgustingcat.springbootapp.dao.UserRepository;
import com.disgustingcat.springbootapp.entity.Session;
import com.disgustingcat.springbootapp.entity.User;
import com.disgustingcat.springbootapp.service.SessionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;

    @PostMapping("/api/login")
    private ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response) {
        User actualUser = userRepository.findByUsername(user.getUsername());
        if (actualUser.getPassword().equals(user.getPassword())) {
            Session s = sessionService.createSession(user.getUsername());
            final Cookie cookie = new Cookie("AppsessionID", s.getId().toString());
            cookie.setSecure(false);
            cookie.setHttpOnly(false);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.addHeader("Access-Control-Allow-Credentials", "true");
            return new ResponseEntity<String>(user.getUsername(), null, 200);
            // return new JsonObject("{\"username\": " + user.getUsername() +
            // "}").getJson();
        }
        return new ResponseEntity<String>("Wrong user/password", null, 403);
    }

    @PostMapping("/api/logout")
    private ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() == null) {
            return new ResponseEntity<Void>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                boolean expired = sessionService.expireSession(Long.parseLong(cookie.getValue()));
                if (!expired) {
                    return new ResponseEntity<Void>(null, null, 403);
                }
                final Cookie newCookie = new Cookie("AppsessionID", "none");
                newCookie.setSecure(false);
                newCookie.setHttpOnly(false);
                newCookie.setPath("/");
                newCookie.setMaxAge(0);
                response.addCookie(newCookie);
                response.addHeader("Access-Control-Allow-Credentials", "true");
                return new ResponseEntity<Void>(null, null, 200);
            }
        }
        return new ResponseEntity<Void>(null, null, 403);
    }
}
