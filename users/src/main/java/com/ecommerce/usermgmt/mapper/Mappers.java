package com.ecommerce.usermgmt.mapper;

import com.ecommerce.usermgmt.entity.User;
import com.ecommerce.usermgmt.model.UserModel;

public class Mappers {
    private Mappers(){}
    public static UserModel map(User u) {
        return new UserModel(u.getId(), u.getName(), u.getEmail(), u.getMobile());
    }

    public static User map(UserModel u) {
        return new User(u.getId(), u.getName(), u.getEmail(), u.getMobile());
    }

}
