package com.ylli.portfolio.ylliportfoliowebpage.mapper;

import com.ylli.portfolio.ylliportfoliowebpage.config.JwtTokenUtil;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.CreateUserRequest;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.UserView;
import com.ylli.portfolio.ylliportfoliowebpage.domain.models.Users;
import com.ylli.portfolio.ylliportfoliowebpage.repository.UserRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;

    public abstract UserView toUserView(Users user);

    public abstract List<UserView> toUserView(List<Users> users);

    public abstract Users create(CreateUserRequest createUserRequest);

    @AfterMapping
    protected void afterCreate(CreateUserRequest request, @MappingTarget Users user) {
        user.setPassword(jwtTokenUtil.getSHA512(request.getPassword()));
    }
}
