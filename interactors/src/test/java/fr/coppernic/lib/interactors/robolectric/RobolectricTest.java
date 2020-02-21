package fr.coppernic.lib.interactors.robolectric;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.TestCase.fail;
import static org.awaitility.Awaitility.await;

/**
 * Base class extended by every Robolectric test in this project.
 * <p>
 * Robolectric tests are done in a single thread !
 */
@RunWith(RobolectricTestRunner.class)
public abstract class RobolectricTest {

    private AtomicBoolean unblock = new AtomicBoolean(false);

    @BeforeClass
    public static void beforeClass() {
        //Configure robolectric
        ShadowLog.stream = System.out;
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unblock() {
        unblock.set(true);
    }

    public void block() {
        await().untilTrue(unblock);
    }

    public void doNotGoHere() {
        fail();
    }

}