package com.app.login.repository;

import com.app.login.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 * @author Megadotnet
 * @date 2018-03-07
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
