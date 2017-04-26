package net.bdew.wurm.server.setheight;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SetHeightMod implements WurmServerMod, Initable, PreInitable, PlayerMessageListener {
    private static final Logger logger = Logger.getLogger("SetHeightMod");

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    public static void logWarning(String msg) {
        if (logger != null)
            logger.log(Level.WARNING, msg);
    }

    public static void logInfo(String msg) {
        if (logger != null)
            logger.log(Level.INFO, msg);
    }

    public static String steamName = null;


    @Override
    public void init() {
    }

    @Override
    public void preInit() {
    }

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
        if (message.startsWith("#")) {
            return CommandHandler.onPlayerMessage(communicator, message) ? MessagePolicy.DISCARD : MessagePolicy.PASS;
        }
        return MessagePolicy.PASS;
    }

    @Deprecated
    @Override
    public boolean onPlayerMessage(Communicator communicator, String msg) {
        return false;
    }
}
