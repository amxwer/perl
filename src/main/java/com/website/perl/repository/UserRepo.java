package com.website.perl.repository;

import com.website.perl.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo  extends JpaRepository<User,Long> {
        User findByUsername(String username);

}
