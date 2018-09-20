package server;

import java.util.TreeMap;
import java.util.UUID;

import game.GameController;

public abstract class PlayController {
    private static TreeMap<String, GameController> gameControllerArray = new TreeMap<>();

    public static String createNewGame() {
        String uuid = null;

        try {
            GameController gameController = new GameController();
            uuid = UUID.randomUUID().toString();
            gameControllerArray.put(uuid, gameController);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuid;
    }

    public static GameController getOldGame(String uuid) {
        if (gameControllerArray.containsKey(uuid)) {
            return gameControllerArray.get(uuid);
        } else {
            return null;
        }
    }

    public static boolean delGame(String uuid) {
        if (gameControllerArray.containsKey(uuid)) {
            gameControllerArray.remove(uuid);
            return true;
        } else {
            return false;
        }
    }

}
