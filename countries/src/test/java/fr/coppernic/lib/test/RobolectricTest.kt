package fr.coppernic.lib.test

import android.os.Build
import fr.bipi.tressence.console.SystemLogTree
import fr.coppernic.lib.countries.log.LogDefines
import org.awaitility.Awaitility.await
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Base class extended by every Robolectric test in this project.
 * <p>
 * Robolectric tests are done in a single thread !
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
abstract class RobolectricTest {
    companion object {
        private val tree = SystemLogTree()

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            Timber.plant(tree)
            ShadowLog.stream = System.out
            LogDefines.setVerbose(true)
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Timber.uproot(tree)
        }
    }

    private val unblock = AtomicBoolean(false)

    fun sleep(ms: Long) {
        try {
            Thread.sleep(ms)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun unblock() {
        unblock.set(true)
    }

    fun block() {
        unblock.set(false)
        await().untilTrue(unblock)
    }
}
