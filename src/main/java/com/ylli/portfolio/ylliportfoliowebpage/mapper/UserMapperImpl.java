package com.ylli.portfolio.ylliportfoliowebpage.mapper;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.CreateUserRequest;
import com.ylli.portfolio.ylliportfoliowebpage.domain.dto.UserView;
import com.ylli.portfolio.ylliportfoliowebpage.domain.models.Users;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2022-09-05T17:27:34+0200",
        comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public UserView toUserView(Users user) {
        if ( user == null ) {
            return null;
        }

        UserView userView = new UserView();

        userView.setEmail( user.getEmail() );
        userView.setFirstname( user.getFirstname() );
        userView.setLastname( user.getLastname() );
        userView.setAvatar( user.getAvatar() );
        userView.setAdmin( user.getAdmin() );

        return userView;
    }

    @Override
    public List<UserView> toUserView(List<Users> users) {
        if ( users == null ) {
            return null;
        }

        List<UserView> list = new ArrayList<UserView>( users.size() );
        for ( Users users1 : users ) {
            list.add( toUserView( users1 ) );
        }

        return list;
    }

    @Override
    public Users create(CreateUserRequest createUserRequest) {
        if ( createUserRequest == null ) {
            return null;
        }

        Users users = new Users();

        users.setEmail( createUserRequest.getEmail() );
        users.setFirstname( createUserRequest.getFirstname() );
        users.setLastname( createUserRequest.getLastname() );
        users.setPassword( createUserRequest.getPassword() );
        users.setAvatar( createUserRequest.getAvatar() );

        afterCreate( createUserRequest, users );

        return users;
    }
}

