package fr.coppernic.lib.interactors;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.fail;

public abstract class TestBase {

    private final AtomicBoolean unblock = new AtomicBoolean(false);

    public void unblock() {
        unblock.set(true);
    }

    public void unblockIn(final long timer, final TimeUnit unit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(unit.toMillis(timer));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                unblock.set(true);
            }
        }).start();
    }

    public void block() {
        await().untilTrue(unblock);
        unblock.set(false);
    }

    public void block(long timeout, TimeUnit unit) {
        await().atMost(timeout, unit).untilTrue(unblock);
        unblock.set(false);
    }

    public void doNotGoHere() {
        fail();
    }

}
