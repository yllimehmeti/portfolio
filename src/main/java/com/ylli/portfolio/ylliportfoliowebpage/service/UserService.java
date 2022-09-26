package com.ylli.portfolio.ylliportfoliowebpage.service;


import com.ylli.portfolio.ylliportfoliowebpage.config.JwtTokenUtil;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.CreateUserRequest;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.UserView;
import com.ylli.portfolio.ylliportfoliowebpage.domain.models.Users;
import com.ylli.portfolio.ylliportfoliowebpage.mapper.UserMapper;
import com.ylli.portfolio.ylliportfoliowebpage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public UserView create(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        Users user = userMapper.create(request);
        user.setAdmin(true);
        user = userRepository.save(user);

        String jwtToken = jwtTokenUtil.generateAccessToken(user);
        UserView userView = userMapper.toUserView(user);
        userView.setJwt(jwtToken);
        userView.setAdmin(user.getAdmin());
        return userView;
    }

    public List<Users> findAllUsers(){
        return userRepository.findAll();
    }
}
