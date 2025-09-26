package com.ifsc.tarefas.auth;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthService 
{
    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) 
    {
        this.authRepository = authRepository;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "redirect", required = false) String redirect, Model model) 
    {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() 
    {
        return "cadastro";
    }

    @PostMapping("/login")
    public String doLogin
    (
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(name = "redirect", required = false) String redirect, Model model,
            HttpServletResponse response
    ) 
        {
            Optional<String> token = authRepository.login(username, password);
            if(token.isEmpty())
            {
                model.addAttribute("error", "Login ou senha incorretos");
                model.addAttribute("redirect", redirect);
                return "login";
            }

            String tokenValue = token.get();

            Cookie cookie = new Cookie("AUTH_TOKEN", tokenValue);
            
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            String target = (redirect == null || redirect.isBlank()) ? "/templates/listar_tarefa" : redirect;

            if(target.contains("://"))
            {
                target = "/templates/listar_tarefa";
            }

            if(!target.startsWith("/")) 
            {
                target = "/" + target;
            }

            return "redirect:" + target ;
    }

    @PostMapping("/logout")
    public String logout
    (
        @RequestParam(name = "redirect", required = false) String redirect, 
        @RequestParam(name = "token", required = false) String token,
        HttpServletResponse response
    ) 
    {
        if(token != null)
        {
           authRepository.logout(token);
        }

        Cookie cookie = new Cookie("AUTH_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        String target = (redirect == null || redirect.isBlank()) ? "/login" : redirect;

        return "redirect:" + target;
    }

    @PostMapping("/register")
    public String register
    (
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestParam String confirmPassword,
        Model model
    ) 
    { 
        if
        (
            username == null ||
            username.isBlank() || 
            password == null || 
            password.isBlank() || 
            confirmPassword == null || 
            confirmPassword.isBlank()
        ) 
        {
            model.addAttribute("error", "Todos os campos devem ser preenchidos");
            return "cadastro";
        }
        if(!password.equals(confirmPassword))
        {
            model.addAttribute("error", "As senhas devem ser iguais");
            return "cadastro";
        }

        boolean criouComSucesso = authRepository.register(username, password);
        if(criouComSucesso)
        {
            return "login";
        } 
        else 
        {
            model.addAttribute("error", "Ocorreu um erro ao criar o usuario");
            return "cadastro";
        }
    }
}
