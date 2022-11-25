package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("select a from User a", User.class);
        return query.getResultList();
    }

    @Override
    public void saveUser(String userName, String password, Collection<Role> roles,
                         String name, String lastName, int age) {
        User user = new User(userName, password, roles, name, lastName, age);

        if (!roles.isEmpty()) {
            roles.forEach(r -> entityManager.persist(r));
        }
        entityManager.persist(user);
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
        User userToBeUpdated = findUserById(id);

        userToBeUpdated.setUsername(updatedUser.getUsername());
        userToBeUpdated.setPassword(updatedUser.getPassword());
        userToBeUpdated.setRoles(updatedUser.getRoles());
        userToBeUpdated.getRoles().forEach(r -> entityManager.persist(r));
        userToBeUpdated.setName(updatedUser.getName());
        userToBeUpdated.setLastname(updatedUser.getLastname());
        userToBeUpdated.setAge(updatedUser.getAge());
    }

    @Override
    public User findUserByUsername(String userName) {
        TypedQuery<User> query = entityManager.createQuery
                ("select u from User u where u.username = :userNameParam", User.class);

        query.setParameter("userNameParam", userName);
        List<User> results = query.getResultList();
        if (results.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return results.get(0);
    }

    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void removeUserById(long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }
}
