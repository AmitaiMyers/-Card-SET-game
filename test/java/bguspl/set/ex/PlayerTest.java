package bguspl.set.ex;

import bguspl.set.Config;
import bguspl.set.Env;
import bguspl.set.UserInterface;
import bguspl.set.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;
import java.util.Queue;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    Player player;
    @Mock
    Util util;
    @Mock
    private UserInterface ui;
    @Mock
    private Table table;
    @Mock
    private Dealer dealer;
    @Mock
    private Logger logger;


    private Integer[] slotToCard;
    private Integer[] cardToSlot;

    void assertInvariants() {
        assertTrue(player.id >= 0);
        assertTrue(player.getScore() >= 0);
    }

    @BeforeEach
    void setUp() {
        Properties properties = new Properties();
        // purposely do not find the configuration files (use defaults here).
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        player = new Player(env, dealer, table, 0, false);
        assertInvariants();
        TableTest.MockLogger logger = new TableTest.MockLogger();
        Config config = new Config(logger, properties);
        slotToCard = new Integer[config.tableSize];
        cardToSlot = new Integer[config.deckSize];
        table = new Table(env, slotToCard, cardToSlot);
    }

    @AfterEach
    void tearDown() {
        assertInvariants();
    }

    @Test
    void point() {

        // force table.countCards to return 3
        //when(table.countCards()).thenReturn(3); // this part is just for demonstration

        // calculate the expected score for later
        int expectedScore = player.getScore() + 1;

        // call the method we are testing
        player.point();

        // check that the score was increased correctly
        assertEquals(expectedScore, player.getScore());

        // check that ui.setScore was called with the player's id and the correct score
        verify(ui).setScore(eq(player.id), eq(expectedScore));
    }

    @Test
    void cleanQueue() {
        player.fullQueue = true;
        Queue<Integer> queue = player.queueKeyPressed;
        queue.add(1);
        queue.add(2);
        queue.add(3);
        player.queueKeyPressed = queue;
        player.cleanQueue();
        assertEquals(0, player.queueKeyPressed.size());
        assertFalse(player.fullQueue);
    }

    private void fillAllSlots() {
        for (int i = 0; i < slotToCard.length; ++i) {
            table.slotToCard[i] = i;
            table.cardToSlot[i] = i;
        }
    }

    @Test
    void keyPressed() {
        //set up
        Player playerTest = new Player(new Env(logger, new Config(logger, (String) null), ui, util), dealer, table, 10, false);
        fillAllSlots();
        playerTest.fullQueue = false;
        playerTest.penalty = false;
        playerTest.point = false;
        playerTest.keyPressed(0);
        assertEquals(1, playerTest.queueKeyPressed.size()); // add to queue
        playerTest.keyPressed(0);
        assertEquals(0,playerTest.queueKeyPressed.size()); // remove from queue
        playerTest.keyPressed(0);
        playerTest.keyPressed(1);
        assertEquals(2,playerTest.queueKeyPressed.size()); // 2 elements added to queue
        playerTest.keyPressed(2);
        assertEquals(3,playerTest.queueKeyPressed.size()); // 3 elements added to queue
        assertTrue(playerTest.fullQueue); // full queue
        playerTest.keyPressed(2);
        assertFalse(playerTest.fullQueue); // not full queue
        assertEquals(2,playerTest.queueKeyPressed.size()); // 2 elements left in queue
    }
}