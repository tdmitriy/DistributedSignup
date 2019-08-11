package com.newage.persistenceservice;

import com.newage.persistenceservice.model.entity.Player;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class PersistenceServiceApplicationTests extends AbstractApplicationTest {

    @Test
    public void playerRepositoryTest() {
        Player player = new Player();
        player.setEmail("xyi");
        player.setPassword("xyi");

        Player saved = playerRepository.save(player);
        assertNotNull(saved);

        Optional<Player> foundById = playerRepository.findById(saved.getId());
        assertTrue(foundById.isPresent());

        Optional<Player> foundByEmail = playerRepository.findById(saved.getId());
        assertTrue(foundByEmail.isPresent());
    }
}
