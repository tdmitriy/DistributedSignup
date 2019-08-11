package com.newage.persistenceservice;

import com.newage.persistenceservice.model.entity.Player;
import com.newage.persistenceservice.repository.PlayerRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class PersistenceServiceApplicationTests extends AbstractApplicationTest {

    @Test
    public void tt() {

    }

//    @Test
//    public void playerRepositoryTest() {
//        Player player = new Player();
//        player.setEmail("xyi");
//        player.setPassword("xyi");
//
//        Player saved = playerRepository.save(player);
//        assertNotNull(saved);
//
//        Optional<Player> found = playerRepository.findById(saved.getId());
//        assertTrue(found.isPresent());
//
//        List<Player> all = playerRepository.findAll();
//    }
//
//    @Test
//    public void xx() {
//        Player player = new Player();
//        player.setEmail("sss");
//        player.setPassword("sss");
//
//        Player saved = playerRepository.save(player);
//        assertNotNull(saved);
//
//        Optional<Player> found = playerRepository.findById(saved.getId());
//        assertTrue(found.isPresent());
//
//        List<Player> all = playerRepository.findAll();
//    }

}
