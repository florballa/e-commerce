package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.RoleModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public ModelAndView listUsers(Model model,
                                  @RequestParam(defaultValue = "0") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "id") String sortBy) {

        List<UserModel> userList = userService.getAllUsers(pageNo, pageSize, sortBy);
        model.addAttribute("users", userList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_users");
        return modelAndView;

    }

    @GetMapping("/register")
    public ModelAndView registerForm(Model model) {
        model.addAttribute("user", new UserModel());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView register(UserModel user) {

        userService.signUpUser(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_success");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id, Model model) {
        UserModel user = userService.findById(id);
        List<RoleModel> listRoles = userService.getRoles();

        model.addAttribute("roles", listRoles);
        model.addAttribute("user", user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user_form");
        return modelAndView;
    }

    @PostMapping("/save")
    public ModelAndView saveUser(UserModel user) {

        userService.saveUser(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_users");
        return modelAndView;
    }

}
