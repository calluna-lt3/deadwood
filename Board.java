public class Board {
    Room[] rooms;

    public void resetShotMarkers() {
        for (Room room : rooms) {
            if (room instanceof SoundStage) {
                ((SoundStage)room).setShotMarkers(((SoundStage)room).getMaxShotMarkers());
            }
        }
    }

    private void calculateAdjacency() {}
}
