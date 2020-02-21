package fr.coppernic.lib.interactors.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class InteractorsDefines {

    private InteractorsDefines() {
    }

    // Log
    public static final String TAG = "Interactors";
    public static final Logger LOG = LoggerFactory.getLogger(TAG);

    /**
     * True to activate verbose logging in all lib
     */
    public static Boolean verbose = false;

    /**
     * True to activate profiler in all lib
     */
    public static Boolean profile = false;

    public static void setVerbose(Boolean b) {
        verbose = b;
    }

    public static void setProfile(Boolean b) {
        profile = b;
    }
}
