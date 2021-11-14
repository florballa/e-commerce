package com.ecommerce.shop.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.shop.Model.RoleModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> listUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(defaultValue = "id") String sortBy) {

        List<UserModel> userList = userService.getAllUsers(pageNo, pageSize, sortBy);
        return ResponseEntity.ok().body(userList);
    }

    @PostMapping("/add")
    public ResponseEntity<UserModel> register(@RequestBody UserModel user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/add").toUriString());
        return ResponseEntity.created(uri).body(userService.signUpUser(user));
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

}
