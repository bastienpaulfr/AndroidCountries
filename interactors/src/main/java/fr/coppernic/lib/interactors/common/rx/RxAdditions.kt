package fr.coppernic.lib.interactors.common.rx

import fr.coppernic.lib.interactors.common.InteractorsDefines.LOG
import fr.coppernic.lib.interactors.common.InteractorsDefines.verbose
import io.reactivex.SingleEmitter

fun <T> SingleEmitter<T>.success(obj: T) {
    if (!isDisposed) {
        onSuccess(obj)
    } else if (verbose) {
        LOG.trace("Emitter is disposed, cannot do onSuccess")
    }
}


fun <T> SingleEmitter<T>.error(obj: Throwable) {
    if (!isDisposed) {
        onError(obj)
    } else if (verbose) {
        LOG.trace("Emitter is disposed, cannot do onError $obj")
    }
}

