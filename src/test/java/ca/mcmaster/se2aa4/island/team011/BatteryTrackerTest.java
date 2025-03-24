package ca.mcmaster.se2aa4.island.team011;

import ca.mcmaster.se2aa4.island.team011.Drone.BatteryTracker;
import ca.mcmaster.se2aa4.island.team011.Drone.BatteryTrackListener;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;


class BatteryTrackerTest {
    private BatteryTracker batteryTracker;
    private TestBatteryListener testListener;

    @BeforeEach
    void setUp() {
        batteryTracker = new BatteryTracker(100); 
        testListener = new TestBatteryListener();
        batteryTracker.setListener(testListener);
    }

    @Test
    void testInitialization() {
        assertEquals(100, batteryTracker.getBatteryLevel(), "Battery should initialize with given value");
    }

    @Test
    void testConsumeBattery() {
        batteryTracker.consumeBattery(30);
        assertEquals(70, batteryTracker.getBatteryLevel(), "Battery should decrease by cost");

        batteryTracker.consumeBattery(40);
        assertEquals(30, batteryTracker.getBatteryLevel(), "Battery should decrease further");
    }

    @Test
    void testConsumeBatteryNotEnough() {
        batteryTracker.consumeBattery(120); 
        assertEquals(100, batteryTracker.getBatteryLevel(), "Battery should not change if cost is too high");
    }

    @Test
    void testBatteryDepletionWarning() {
        batteryTracker.consumeBattery(60); 
        assertTrue(testListener.depletionTriggered, "Battery depletion warning should be triggered");
        assertEquals(40, testListener.lastBatteryLevel, "Listener should receive correct battery update");
    }

    @Test
    void testGetCostValidJson() {
        String json = "{\"cost\": 20}";
        assertEquals(20, batteryTracker.getCost(json), "Should extract correct cost");
    }

    @Test
    void testGetCostInvalidJson() {
        String json = "{\"wrong_key\": 20}";
        assertThrows(org.json.JSONException.class, () -> batteryTracker.getCost(json), "Should throw exception for missing 'cost' key");
    }


    private static class TestBatteryListener implements BatteryTrackListener {
        boolean depletionTriggered = false;
        int lastBatteryLevel = -1;

        @Override
        public void onBatteryUpdate(int newLevel) {
            lastBatteryLevel = newLevel;
        }

        @Override
        public void onBatteryDepleted() {
            depletionTriggered = true;
        }
    }
}

