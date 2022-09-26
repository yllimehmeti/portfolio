package com.ylli.portfolio.ylliportfoliowebpage.domain.dto;

import lombok.Data;

@Data
public class UserView {
    public String email;
    private String firstname;
    private String lastname;
    private String avatar;
    private String jwt;
    private Boolean admin;
}
