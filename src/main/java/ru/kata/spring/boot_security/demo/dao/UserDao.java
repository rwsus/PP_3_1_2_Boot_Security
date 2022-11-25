package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void saveUser(String userName, String password, Collection<Role> roles, String name, String lastName, int age);

    void updateUser(Long id, User updatedUser);

    User findUserById(Long id);

    User findUserByUsername(String userName);

    void removeUserById(long id);

}
