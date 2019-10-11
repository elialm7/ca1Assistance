package Modelos.SQLITE.Interfaces;

import Modelos.Pojos.Users.User;

public interface IUser extends genericdomain<User, Integer> {

    boolean exists(User user);

}
