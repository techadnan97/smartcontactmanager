/**
 * 
 */
package com.smart.confuguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.model.User;
import com.smart.repository.UserRepository;

/**
 * @author Adnan
 *
 */
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
	private UserRepository userRepository;
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("User Does Not Exist");
		}
		CustomUserDetail customUserDetail= new CustomUserDetail(user);
		return customUserDetail;
	}

}
