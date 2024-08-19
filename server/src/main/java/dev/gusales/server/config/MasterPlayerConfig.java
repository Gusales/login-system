package dev.gusales.server.config;

import dev.gusales.server.entities.Player;
import dev.gusales.server.entities.Role;
import dev.gusales.server.repositories.PlayerRepository;
import dev.gusales.server.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class MasterPlayerConfig implements CommandLineRunner {
    private RoleRepository roleRepository;
    private PlayerRepository playerRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public MasterPlayerConfig(
            RoleRepository roleRepository,
            PlayerRepository playerRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleMaster = roleRepository.findByNameRole(Role.Values.MASTER.name());

        var userMaster = playerRepository.findByNickname("noobmaster69");
        var isExistsUserMaster = userMaster.isPresent();

        if (isExistsUserMaster){
            System.out.println("Jogador mestre já existe");
        }
        else{
            Player player = new Player();

            player.setNickname("noobmaster69");
            player.setPassword(passwordEncoder.encode("123456"));
            player.setRoles(Set.of(roleMaster));

            playerRepository.save(player);
            System.out.printf("%s entrou no banco de dados\n", player.getNickname());
        }
    }
}
