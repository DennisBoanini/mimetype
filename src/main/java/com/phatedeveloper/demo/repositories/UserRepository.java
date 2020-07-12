package com.phatedeveloper.demo.repositories;

import com.phatedeveloper.demo.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

	ApplicationUser findByUsername(String username);
}
