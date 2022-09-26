package com.ylli.portfolio.ylliportfoliowebpage.controller;

import com.ylli.portfolio.ylliportfoliowebpage.config.JwtTokenUtil;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.AuthRequest;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.CreateUserRequest;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.UserView;
import com.ylli.portfolio.ylliportfoliowebpage.domain.models.Users;
import com.ylli.portfolio.ylliportfoliowebpage.mapper.UserMapper;
import com.ylli.portfolio.ylliportfoliowebpage.repository.UserRepository;
import com.ylli.portfolio.ylliportfoliowebpage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path ="auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody @Valid AuthRequest request) {
        try {
            Users user = userRepository.findByEmail(request.getEmail()).get();
            if(!user.getPassword().equalsIgnoreCase(jwtTokenUtil.getSHA512(request.getPassword()))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String jwtToken = jwtTokenUtil.generateAccessToken(user);
            UserView userView = userMapper.toUserView(user);
            userView.setJwt(jwtToken);
            userView.setAdmin(user.getAdmin());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .body(userView);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("verify")
    public ResponseEntity<UserView> verify(HttpServletRequest request) {
        try {
            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            final String token = header.split(" ")[1].trim();
            if (!jwtTokenUtil.validate(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Users user = userRepository.findByEmail(jwtTokenUtil.getUsername(token)).get();
            UserView userView = userMapper.toUserView(user);
            userView.setJwt(token);
            userView.setAdmin(user.getAdmin());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, header)
                    .body(userView);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    public ResponseEntity<UserView> register(@RequestBody @Valid CreateUserRequest createUserRequest) {
        try {
            UserView userView = userService.create(createUserRequest);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + userView.getJwt())
                    .body(userView);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
