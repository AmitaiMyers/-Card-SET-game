package bguspl.set.ex;

import bguspl.set.Config;
import bguspl.set.Env;
import bguspl.set.UserInterface;
import bguspl.set.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;
import java.util.Queue;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DealerTest {
    Table table;
    private Integer[] slotToCard;
    private Integer[] cardToSlot;

    Player player;
    @Mock
    Util util;
    @Mock
    private UserInterface ui;

    @Mock
    private Dealer dealer;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {

        Properties properties = new Properties();
        TableTest.MockLogger logger = new TableTest.MockLogger();
        Config config = new Config(logger, properties);
        slotToCard = new Integer[config.tableSize];
        cardToSlot = new Integer[config.deckSize];

        Env env = new Env(logger, config, new TableTest.MockUserInterface(), new TableTest.MockUtil());
        table = new Table(env, slotToCard, cardToSlot);

    }



    @Test
    void queueToArrayTest() {
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        player = new Player(env, dealer, table, 0, false);
        Player[] players = new Player[1];
        players[0] = player;
        Dealer dealerTest = new Dealer(env,table,players);
        Queue<Integer> queue = players[0].queueKeyPressed;
        queue.add(1);
        queue.add(2);
        queue.add(3);
         int[] arrayOfSlots = dealerTest.queueToArray(queue);
        assertEquals(1, arrayOfSlots[0]);
        assertEquals(2, arrayOfSlots[1]);
        assertEquals(3, arrayOfSlots[2]);
    }

    @Test
    void slotsToCardsTest() {
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        player = new Player(env, dealer, table, 0, false);
        Player[] players = new Player[1];
        players[0] = player;
        Dealer dealerTest = new Dealer(env,table,players);
        int[] arrayOfSlots = new int[3];
        arrayOfSlots[0] = 1;
        arrayOfSlots[1] = 2;
        arrayOfSlots[2] = 3;
        table.slotToCard[1] = 1;
        table.slotToCard[2] = 2;
        table.slotToCard[3] = 3;
        int[] threeCards = dealerTest.slotsToCardsInArray(arrayOfSlots);
        assertEquals(1, threeCards[0]);
        assertEquals(2, threeCards[1]);
        assertEquals(3, threeCards[2]);
    }

}