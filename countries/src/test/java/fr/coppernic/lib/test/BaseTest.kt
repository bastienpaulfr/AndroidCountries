package fr.coppernic.lib.test

import fr.bipi.tressence.console.SystemLogTree
import org.junit.AfterClass
import org.junit.BeforeClass
import timber.log.Timber

abstract class BaseTest {
    companion object {
        private val tree = SystemLogTree()

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            Timber.plant(tree)
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Timber.uproot(tree)
        }
    }
}
