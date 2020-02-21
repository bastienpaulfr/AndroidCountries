package fr.coppernic.lib.interactors.agrident

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import fr.coppernic.lib.interactors.TestBase
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AgridentInteractorAndroidTest : TestBase() {

    private lateinit var interactor: AgridentInteractor
    private var disposable: Disposable? = null
    private lateinit var context: Context


    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        interactor = AgridentInteractor(context)
        Timber.plant(Timber.DebugTree())
    }

    @Test
    fun retry() {
        val nbRetry = 3

        val observer = TestObserver<String>()
        Timber.d("listen")
        interactor.listen()
                // Catch error before retry
                .doOnEach { stringNotification ->
                    Timber.d(stringNotification.toString())
                    unblockIn(1, TimeUnit.SECONDS)
                }
                // subscribe again in case of error
                .retry(nbRetry.toLong())
                .subscribe(observer)

        for (i in 0..nbRetry) {
            Timber.d("trig")
            interactor.trig()
            block(5, TimeUnit.SECONDS)
        }

        Timber.d("assert")
        observer.assertNoErrors()
        observer.dispose()
    }

    @Test
    fun continuousRead() {
        val observer = TestObserver<String>()
        Timber.d("listen")
        interactor.listen()
                .doOnEach {
                    if (it.isOnNext) {
                        Timber.d("Tag found ${it.value}")
                    } else if (it.isOnError) {
                        Timber.d(it.error)
                    }
                    interactor.trig()
                }
                .subscribe(observer)

        interactor.trig()

        SystemClock.sleep(10000)
        observer.assertNoErrors()
        observer.dispose()
    }

    @Test
    fun listenEmpty() {
        val observer = interactor.listen().test()
        observer.assertValueCount(0)
    }

    private val disposeObserver = object : Observer<String> {
        override fun onSubscribe(d: Disposable) {
            Timber.d(d.toString())
            disposable = d
            unblockIn(300, TimeUnit.MILLISECONDS)
        }

        override fun onNext(s: String) {
            Timber.d(s)
            unblockIn(50, TimeUnit.MILLISECONDS)
        }

        override fun onError(e: Throwable) {
            Timber.d(e.toString())
        }

        override fun onComplete() {
            Timber.d("onComplete")
        }
    }

    // Read a tag to make this test succeed
    @Test(timeout = 15000)
    fun listen() {
        val observer = TestObserver<String>()

        Timber.d("listen")
        interactor.listen()
                .doOnEach { stringNotification ->
                    Timber.d(stringNotification.toString())
                    unblock()
                }
                .subscribe(observer)

        Timber.d("trig")
        interactor.trig()

        block()

        Timber.d("assert")
        observer.assertValueCount(1)
        observer.assertNoErrors()

        observer.dispose()
    }

    @Test
    fun stopServive() {
        Timber.d("listen")

        interactor.listen().subscribe(disposeObserver)
        block()

        Timber.d("trig")
        interactor.trig()
        // block(5, TimeUnit.SECONDS)
        Timber.d("stopService")
        interactor.stopService()
        disposable?.dispose()
    }

    @Test
    fun dispose() {
        Timber.d("listen")
        interactor.listen().subscribe(disposeObserver)
        block()
        disposable?.dispose()

        interactor.listen().subscribe(disposeObserver)
        block()

        Timber.d("trig")
        interactor.trig()
        block(5, TimeUnit.SECONDS)

        disposable?.dispose()
    }

    @Test
    fun stopStartService() {
        interactor.stopService()
        SystemClock.sleep(2000)
        interactor.startService()
    }
}
