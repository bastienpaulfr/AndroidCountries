package fr.coppernic.lib.interactors.picture

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.coppernic.lib.interactors.common.InteractorsDefines
import fr.coppernic.lib.interactors.common.errors.InteractorException
import fr.coppernic.lib.interactors.picture.PictureIntentMatchers.captureImage
import org.junit.*
import org.junit.runner.RunWith
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class PictureInteractorAndroidTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(PictureActivity::class.java)

    lateinit var interactor: PictureInteractor
    lateinit var context: Context

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            InteractorsDefines.verbose = true
            InteractorsDefines.profile = true
            Timber.plant(Timber.DebugTree())
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Timber.uprootAll()
        }

        private const val DEFAULT_SIZE = 100

        @JvmStatic
        @JvmOverloads
        fun mockAndroidCamera(resCode: Int = Activity.RESULT_OK, width: Int = DEFAULT_SIZE, height: Int = DEFAULT_SIZE) {
            val result = createImageCaptureStub(resCode)
            intending(captureImage(width, height)).respondWith(result)
        }

        @JvmStatic
        private fun createImageCaptureStub(resCode: Int = Activity.RESULT_OK): Instrumentation.ActivityResult {
            val resultBundle = Bundle()

            val resultData = Intent()
            resultData.putExtras(resultBundle)

            return Instrumentation.ActivityResult(resCode, resultData)
        }
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        interactor = PictureInteractor(ApplicationProvider.getApplicationContext())
        intentsTestRule.activity.notifier.add(interactor)
    }

    @After
    fun tearDown() {
        intentsTestRule.activity?.notifier?.remove(interactor)
    }

    @Test
    fun trigFile() {
        mockAndroidCamera()

        val file = context.cacheDir.resolve("pic.jpg")

        val observer = interactor.trig(file, intentsTestRule.activity).test()

        intended(captureImage(DEFAULT_SIZE, DEFAULT_SIZE))

        observer.awaitTerminalEvent(3, TimeUnit.SECONDS)
        observer.assertNoTimeout()
        observer.assertValue(Uri.parse("content://fr.coppernic.lib.interactors.test.fr.coppernic.lib.interactors.provider/cache/pic.jpg"))
    }

    @Test
    fun doubleTrigFile() {
        mockAndroidCamera()

        val file1 = context.cacheDir.resolve("pic.jpg")
        val file2 = context.cacheDir.resolve("error.jpg")

        val observer1 = interactor.trig(file1, intentsTestRule.activity).test()
        val observer2 = interactor.trig(file2, intentsTestRule.activity).test()

        intended(captureImage(DEFAULT_SIZE, DEFAULT_SIZE))

        observer1.awaitTerminalEvent(3, TimeUnit.SECONDS)
        observer2.awaitTerminalEvent(3, TimeUnit.SECONDS)
        observer1.assertNoTimeout()
        observer2.assertNoTimeout()
        observer1.assertValue(Uri.parse("content://fr.coppernic.lib.interactors.test.fr.coppernic.lib.interactors.provider/cache/pic.jpg"))
        observer2.assertError(InteractorException::class.java)
        observer2.assertErrorMessage("Pending request in progress: Request(file=/data/user/0/fr.coppernic.lib.interactors.test/cache/pic.jpg, uri=content://fr.coppernic.lib.interactors.test.fr.coppernic.lib.interactors.provider/cache/pic.jpg, id=10229)")
    }

    @Test
    fun trigNotExistentFile() {
        mockAndroidCamera()

        val file = File("/somewhere/the/rainbow")

        val observer = interactor.trig(file, intentsTestRule.activity).test()

        observer.awaitTerminalEvent(3, TimeUnit.SECONDS)
        observer.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun trigFileAndCancel() {
        mockAndroidCamera(Activity.RESULT_CANCELED)

        val file = context.cacheDir.resolve("pic.jpg")

        val observer = interactor.trig(file, intentsTestRule.activity).test()

        intended(captureImage(DEFAULT_SIZE, DEFAULT_SIZE))

        observer.awaitTerminalEvent(3, TimeUnit.SECONDS)
        observer.assertNoTimeout()
        observer.assertError(InteractorException::class.java)
        observer.assertErrorMessage("Taking picture failed, result ${Activity.RESULT_CANCELED}")
    }

}
