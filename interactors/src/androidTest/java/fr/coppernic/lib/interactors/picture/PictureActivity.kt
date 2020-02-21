package fr.coppernic.lib.interactors.picture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import fr.coppernic.lib.interactors.common.ui.ActivityResultNotifier
import timber.log.Timber

class PictureActivity : Activity() {

    var notifier: ActivityResultNotifier = ActivityResultNotifier()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.i("onActivityResult, code: $requestCode, result: $resultCode, intent: $data")
        notifier.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
