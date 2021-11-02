package com.example.comp3095_assignment1.controllers;

import javax.validation.Valid;

import com.example.comp3095_assignment1.model.User;
import com.example.comp3095_assignment1.services.UserService;
import com.example.comp3095_assignment1.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@SpringBootApplication
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public RegisterDto registerDto() {
        return new RegisterDto();
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid RegisterDto userDto,
                                      BindingResult result){

        User user = userService.findByUsername(userDto.getUsername());

        if (user != null){
            result.rejectValue("username", null, "Username is already exist");
        }

        if(userDto.getPassword().length() < 6  ) {
            result.rejectValue("password", "userDto.class", "The password is too short");

        }
        if (result.hasErrors()){
            return "register";

        }


        userService.save(userDto);
        return "redirect:/register?success";
    }

}