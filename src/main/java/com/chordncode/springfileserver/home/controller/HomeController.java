package com.chordncode.springfileserver.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chordncode.springfileserver.home.service.IHomeService;
import com.chordncode.springfileserver.util.ResultStatus;

@Controller
public class HomeController {

    private final IHomeService homeService;
    public HomeController(IHomeService homeService){
        this.homeService = homeService;
    }

    @GetMapping("")
    public String home(){
        return "redirect:/file";
    }

    @GetMapping("/login")
    public String login(){
        return "home/login";
    }

    @PostMapping("/changePw")
    public String changePw(@RequestParam String newPw, Model model){
        ResultStatus result = homeService.changePw(newPw);
        if(result == ResultStatus.SUCCESS){
            model.addAttribute("msg", "비밀번호가 변경되었습니다.");
            model.addAttribute("loc", "/file");
            return "alert/alertLoc";
        } else {
            model.addAttribute("msg", "잠시 후 다시 시도해주세요.");
            return "alert/alertBack";
        }
    }

}
