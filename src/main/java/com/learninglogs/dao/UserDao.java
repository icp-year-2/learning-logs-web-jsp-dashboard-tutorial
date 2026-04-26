package com.learninglogs.dao;

import com.learninglogs.entity.User;

public interface UserDao {

    boolean insertUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
}
