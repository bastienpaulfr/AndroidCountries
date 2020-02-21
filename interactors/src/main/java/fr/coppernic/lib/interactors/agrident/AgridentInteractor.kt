package fr.coppernic.lib.interactors.agrident

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import fr.coppernic.lib.interactors.ReaderInteractor
import fr.coppernic.lib.interactors.common.InteractorsDefines.LOG
import fr.coppernic.sdk.core.Defines
import fr.coppernic.sdk.utils.core.CpcResult
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

const val AGRIDENT_WEDGE = "fr.coppernic.tools.cpcagridentwedge"

class AgridentInteractor @Inject constructor(private val context: Context) : ReaderInteractor<String> {

    private var emitter: ObservableEmitter<String>? = null
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            processIntent(intent)
        }
    }

    private val observableOnSubscribe = ObservableOnSubscribe<String> { e ->
        setEmitter(e)
        registerReceiver()
    }

    override fun trig() {
        // Starts Agrident wedge
        val scanIntent = Intent()
        scanIntent.setPackage(AGRIDENT_WEDGE)
        scanIntent.action = Defines.IntentDefines.ACTION_AGRIDENT_READ
        scanIntent.putExtra(Defines.Keys.KEY_PACKAGE, context.packageName)
        val info = context.startService(scanIntent)
        if (info == null) {
            handleError(CpcResult.ResultException(CpcResult.RESULT.SERVICE_NOT_FOUND))
        }
    }

    override fun listen(): Observable<String> {
        return Observable.create(observableOnSubscribe)
    }

    private fun setEmitter(e: ObservableEmitter<String>) {
        LOG.debug(e.toString())

        // End previous observer and start new one
        emitter?.apply {
            if (!isDisposed) {
                onComplete()
            }
        }

        emitter = e.apply {
            setDisposable(object : Disposable {
                private val disposed = AtomicBoolean(false)

                override fun dispose() {
                    LOG.debug("unregister")
                    unregisterReceiver()
                    disposed.set(true)
                }

                override fun isDisposed(): Boolean {
                    return disposed.get()
                }
            })
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(Defines.IntentDefines.ACTION_AGRIDENT_SUCCESS)
        filter.addAction(Defines.IntentDefines.ACTION_AGRIDENT_ERROR)
        context.registerReceiver(receiver, filter)
    }

    private fun unregisterReceiver() {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: Exception) {
            LOG.trace(e.toString())
        }
    }

    private fun processIntent(intent: Intent) {
        val action = intent.action
        if (action == null) {
            LOG.error("Action of {} is null", intent)
            return
        }

        val localEmitter = if (emitter == null) {
            unregisterReceiver()
            return
        } else {
            emitter!!
        }

        if (action == Defines.IntentDefines.ACTION_AGRIDENT_SUCCESS) {
            val extras = intent.extras
            if (extras == null) {
                LOG.error("No extras for ACTION_AGRIDENT_SUCCESS")
                return
            }
            val data = extras.getString(Defines.Keys.KEY_BARCODE_DATA, "")
            localEmitter.onNext(data)
        } else if (intent.action == Defines.IntentDefines.ACTION_AGRIDENT_ERROR) {
            val res = intent.getIntExtra(Defines.Keys.KEY_RESULT, CpcResult.RESULT.ERROR.ordinal)
            val result = CpcResult.RESULT.values()[res]
            handleError(result.toException())
        }
    }

    override fun startService() {
        val scanIntent = Intent()
        scanIntent.setPackage(AGRIDENT_WEDGE)
        scanIntent.action = Defines.IntentDefines.ACTION_AGRIDENT_SERVICE_START
        scanIntent.putExtra(Defines.Keys.KEY_PACKAGE, context.packageName)
        context.startService(scanIntent)
    }

    override fun stopService() {
        val scanIntent = Intent()
        scanIntent.setPackage(AGRIDENT_WEDGE)
        scanIntent.action = Defines.IntentDefines.ACTION_AGRIDENT_SERVICE_STOP
        scanIntent.putExtra(Defines.Keys.KEY_PACKAGE, context.packageName)
        context.startService(scanIntent)
    }

    private fun handleError(t: Throwable) {
        unregisterReceiver()
        emitter?.apply {
            if (!isDisposed) {
                onError(t)
            }
        }
    }
}
