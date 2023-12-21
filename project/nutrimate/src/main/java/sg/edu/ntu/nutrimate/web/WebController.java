package sg.edu.ntu.nutrimate.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/signin.html")
    public String signin(Model model) {
        return "signin.html";
    }

    @RequestMapping(value = "/signin-error.html")
    public String signinError(Model model) {
        model.addAttribute("loginError", true);
        return "signin.html";
    }
    
}
