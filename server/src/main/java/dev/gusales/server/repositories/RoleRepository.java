package dev.gusales.server.repositories;

import dev.gusales.server.entities.Player;
import dev.gusales.server.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByNameRole(String name);
}
