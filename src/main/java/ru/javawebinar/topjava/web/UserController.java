package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ruslan on 12.12.17.
 */
@Controller
@RequestMapping("/users")
public class UserController extends RootController{

    @Autowired
    private UserService service;

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", service.getAll());
        return "users";
    }

    @PostMapping
    public String registerUser(HttpServletRequest request) {
        AuthorizedUser.setId(Integer.valueOf(request.getParameter("userId")));
        return "redirect:meals";
    }
}