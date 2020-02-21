package fr.coppernic.lib.interactors.barcode;

import android.os.SystemClock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import androidx.test.core.app.ApplicationProvider;
import fr.coppernic.lib.interactors.BuildConfig;
import fr.coppernic.lib.interactors.TestBase;
import fr.coppernic.lib.interactors.common.rx.TimeoutRetryPredicate;
import fr.coppernic.sdk.utils.debug.L;
import io.reactivex.Notification;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import timber.log.Timber;

public class BarcodeInteractorAndroidTest extends TestBase {

    private static final String TAG = "BarcodeInteractorTest";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private BarcodeInteractor interactor;
    private Disposable disposable;
    private final Observer<String> disposeObserver = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            L.mt(TAG, DEBUG, d.toString());
            disposable = d;
            unblockIn(300, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onNext(String s) {
            L.m(TAG, DEBUG, s);
            unblockIn(50, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onError(Throwable e) {
            L.m(TAG, DEBUG, e.toString());
        }

        @Override
        public void onComplete() {
            L.m(TAG, DEBUG);
        }
    };

    @Before
    public void setUp() {
        interactor = new BarcodeInteractor(ApplicationProvider.getApplicationContext());

        Timber.plant(new Timber.DebugTree());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void listenEmpty() {
        final TestObserver<String> observer = interactor.listen().test();

        observer.assertValueCount(0);
    }

    @Test
    public void retry() {
        int nbRetry = 3;

        TestObserver<String> observer = new TestObserver<>();
        Timber.d("listen");
        interactor.listen()
            // Catch error before retry
            .doOnEach(new Consumer<Notification<String>>() {
                @Override
                public void accept(Notification<String> stringNotification) {
                    L.mt(TAG, DEBUG, stringNotification.toString());
                    unblockIn(1, TimeUnit.SECONDS);
                }
            })
            // subscribe again in case of error
            .retry(nbRetry)
            .subscribe(observer);

        for (int i = 0; i < nbRetry; i++) {
            Timber.d("trig");
            interactor.trig();
            block(5, TimeUnit.SECONDS);
        }

        Timber.d("assert");
        observer.assertNoErrors();
        observer.dispose();
    }

    @Test
    public void retryWhenTimeout() {
        int nbRetry = 3;

        TestObserver<String> observer = new TestObserver<>();
        Timber.d("listen");
        interactor.listen()
            // Catch error before retry
            .doOnEach(new Consumer<Notification<String>>() {
                @Override
                public void accept(Notification<String> stringNotification) {
                    L.mt(TAG, DEBUG, stringNotification.toString());
                    unblockIn(1, TimeUnit.SECONDS);
                }
            })
            // subscribe again in case of error
            .retry(new TimeoutRetryPredicate())
            .subscribe(observer);

        for (int i = 0; i < nbRetry; i++) {
            Timber.d("trig");
            interactor.trig();
            block(5, TimeUnit.SECONDS);
        }

        Timber.d("assert");
        observer.assertNoErrors();
        observer.dispose();
    }

    // Scan a barcode to make this test succeed
    @Test(timeout = 15000)
    public void listen() {
        TestObserver<String> observer = new TestObserver<>();

        Timber.d("listen");
        interactor.listen()
            .doOnEach(new Consumer<Notification<String>>() {
                @Override
                public void accept(Notification<String> stringNotification) {
                    L.mt(TAG, DEBUG, stringNotification.toString());
                    unblock();
                }
            })
            .subscribe(observer);

        Timber.d("trig");
        interactor.trig();

        block();

        Timber.d("assert");
        observer.assertValueCount(1);
        observer.assertNoErrors();

        observer.dispose();
    }

    @Test
    public void dispose() {
        Timber.d("listen");
        interactor.listen().subscribe(disposeObserver);
        block();
        if (disposable != null) {
            disposable.dispose();
        }

        interactor.listen().subscribe(disposeObserver);
        block();

        Timber.d("trig");
        interactor.trig();
        block(5, TimeUnit.SECONDS);

        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Test
    public void stopStartService() {
        interactor.stopService();
        SystemClock.sleep(2000);
        interactor.startService();
    }

}
