package jm.SpringBootCrudApp.service;

import jm.SpringBootCrudApp.dao.UserDAO;
import jm.SpringBootCrudApp.model.Role;
import jm.SpringBootCrudApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserDAO userDao;

    @Autowired
    public UserServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

//    @Autowired
//    public void encoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {

        return userDao.read();
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {

        return userDao.readRole();
    }

    @Transactional(readOnly = true)
    public Set<Role> getRoles(String[] ids) {
        return userDao.getRoles(ids);
    }

    @Transactional
    public void insert(User user) {
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.insert(user);
    }

    @Transactional
    public void update(User user) {
        userDao.update(user);
    }

    @Transactional(readOnly = true)
    public User getUser(Integer id) {
        return userDao.read(id);
    }

    @Transactional
    public void deleteUser(Integer id) {
        userDao.delete(id);
    }

    public List<List<String>> getUserRoles(List<Role> allRoles, User user) {
        List<List<String>> newMap1 = new ArrayList<>();
        allRoles.forEach(role -> {
            List<String> newMap = new ArrayList<>();
            newMap.add(String.valueOf(role.getId()));
            newMap.add(role.getRole());
            newMap.add(user.isRoleInUser(role) ? "true" : "false");
            newMap1.add(newMap);
        });
        return newMap1;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username);
    }
}
