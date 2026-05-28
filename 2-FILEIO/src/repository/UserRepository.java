package repository;

import model.User;

import java.util.List;

public class UserRepository {
    List<User> users;
    public UserRepository(){
        users = readFromDataFile("users.csv");
    }

    public void addUser(User user){

    }

}
