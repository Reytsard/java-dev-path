package service;

import model.User;
import repository.UserRepository;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validateUser(String username, String password){
        User user = getUserByUsername(username);
        return user.getUsername().equals(username) && user.getPassword().equals(password);
    }
    public User getUserByUsername(String username) {
        return userRepository.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny().orElseThrow();
    }

    public void addUser(User user) {
        userRepository.addUser(user);
    }
}
