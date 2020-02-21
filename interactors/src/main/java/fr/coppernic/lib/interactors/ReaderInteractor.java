package fr.coppernic.lib.interactors;

import io.reactivex.Observable;

/**
 * Created by michael on 18/01/18.
 */

public interface ReaderInteractor<T> {
    /**
     * Trig a read.
     * <p>
     * Data is received on {@link Observable} got from {@link ReaderInteractor#listen()} method
     * <p>
     * If this method is called before {@link ReaderInteractor#listen()}, then, it is a no op.
     * <p>
     * Calling this method does not always imply a {@link io.reactivex.Observer#onNext(Object)} call of the
     * object given to subscribe of {@link Observable} given by  {@link ReaderInteractor#listen()}.
     * <p>
     * {@link Observable} may give data without trig made by calling this method.
     */
    void trig();

    /**
     * Register and listen for incoming data.
     * <p> This is useful for barcode scan that can be triggered by side buttons.
     *
     * @return An {@link Observable} providing data
     */
    Observable<T> listen();

    /**
     * Stop service for barcode and Agrident
     */
    void stopService();

    /**
     * Start service for barcode and Agrident
     */
    void startService();
}
