package net.bdew.wurm.server.setheight;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.Zones;

import java.util.StringTokenizer;

public class CommandHandler {

    public static boolean onPlayerMessage(Communicator communicator, String msg) {
        Player performer = communicator.getPlayer();
        if (msg.startsWith("#setheight ")) {
            if (performer.getPower() < 5) {
                SetHeightMod.logWarning(String.format("Player %s tried to use #terraform command", performer.getName()));
            } else {
                try {
                    final StringTokenizer tokens = new StringTokenizer(msg);
                    tokens.nextToken();
                    if (tokens.hasMoreTokens()) {
                        int height = Integer.parseInt(tokens.nextToken(), 10);
                        if (height > Short.MAX_VALUE || height < Short.MIN_VALUE) {
                            communicator.sendAlertServerMessage("height value out of range");
                        } else {
                            int radius = 0;
                            if (tokens.hasMoreTokens()) {
                                radius = Integer.parseInt(tokens.nextToken(), 10);
                            }
                            SetHeightMod.logInfo(String.format("#setheight at %d,%d by %s, h=%d r=%d", performer.getTileX(), performer.getTileY(), performer.getName(), height, radius));
                            doSetHeight(performer, height, radius);
                            communicator.sendNormalServerMessage("Done");
                        }
                    } else {
                        communicator.sendAlertServerMessage("Usage: #setheight <height> [radius]");
                    }
                    return true;
                } catch (Exception e) {
                    communicator.sendAlertServerMessage("Error: " + e.toString());
                }
            }
        }
        return false;
    }

    private static void doSetHeight(Player performer, int height, int radius) {
        for (int x = performer.getTileX() - radius; x <= performer.getTileX() + radius + 1; x++) {
            for (int y = performer.getTileY() - radius; y <= performer.getTileY() + radius + 1; y++) {
                if (x < 0 || y < 0 || x >= Zones.worldTileSizeX || y >= Zones.worldTileSizeY)
                    continue;
                int sd = Server.surfaceMesh.getTile(x, y);
                int rd = Server.rockMesh.getTile(x, y);
                short sh = Tiles.decodeHeight(sd);
                short rh = Tiles.decodeHeight(rd);
                if (rh > height)
                    Server.rockMesh.setTile(x, y, Tiles.encode((short) height, Tiles.decodeTileData(rd)));
                Server.surfaceMesh.setTile(x, y, Tiles.encode((short) height, Tiles.decodeTileData(sd)));
                Server.setSurfaceTile(x, y, (short) height, Tiles.decodeType(sd), Tiles.decodeData(sd));
                Players.getInstance().sendChangedTile(x, y, true, true);
            }
        }
    }
}
