package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;

    @Autowired
    private ApplicationContext context;
    PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(String userName, String password, Collection<Role> roles,
                         String name, String lastName, int age) {
        passwordEncoder = context.getBean(PasswordEncoder.class);
        password = passwordEncoder.encode(password);
        userDao.saveUser(userName, password, roles, name, lastName, age);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updatedUser) {
        passwordEncoder = context.getBean(PasswordEncoder.class);
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userDao.updateUser(id, updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername(String userName) {
        return userDao.findUserByUsername(userName);
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        userDao.removeUserById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findUserByUsername(userName);
        Hibernate.initialize(user.getRoles());
        return user;
    }

}
