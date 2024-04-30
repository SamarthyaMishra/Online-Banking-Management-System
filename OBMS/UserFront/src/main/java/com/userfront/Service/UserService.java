package com.userfront.Service;

import java.util.Set;

import com.userfront.Domain.User;
import com.userfront.Domain.Security.UserRole;

public interface UserService {

	User findByUserName(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUserNameExists(String username);

    boolean checkEmailExists(String email);
  
    void save (User user);
    
    User createUser(User user, Set<UserRole> userRoles);
    
    User saveUser(User user);

	

    
    
}