package com.ausy_technologies.demospring.Repository;

import com.ausy_technologies.demospring.Model.DAO.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Override
    List<Role> findAll();

    @Override
    Optional<Role> findById(Integer integer);

    Role findByName(String name);
}
