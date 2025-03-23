package ca.mcmaster.se2aa4.island.team011.Drone;

public interface BatteryTrackListener {
    void onBatteryUpdate(int newBatteryLevel);
    void onBatteryDepleted();
}
