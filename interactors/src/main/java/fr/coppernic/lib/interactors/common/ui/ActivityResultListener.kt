package fr.coppernic.lib.interactors.common.ui

import android.content.Intent
import java.util.concurrent.ConcurrentLinkedQueue

interface ActivityResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

class ActivityResultNotifier {

    private val list = ConcurrentLinkedQueue<ActivityResultListener>()

    fun add(listener: ActivityResultListener) {
        list.add(listener)
    }

    fun remove(listener: ActivityResultListener) {
        list.remove(listener)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        list.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }
}
