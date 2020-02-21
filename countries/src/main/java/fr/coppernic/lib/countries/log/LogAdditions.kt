package fr.coppernic.lib.countries.log

import org.slf4j.Logger
import java.io.PrintWriter
import java.io.StringWriter

fun Logger.trace(t: Throwable) {
    this.trace(getStackTraceString(t))
}

fun Logger.debug(t: Throwable) {
    this.debug(getStackTraceString(t))
}

fun Logger.info(t: Throwable) {
    this.info(getStackTraceString(t))
}

fun Logger.warn(t: Throwable) {
    this.warn(getStackTraceString(t))
}

fun Logger.error(t: Throwable) {
    this.error(getStackTraceString(t))
}

private fun getStackTraceString(t: Throwable): String? { // Don't replace this with Log.getStackTraceString() - it hides
// UnknownHostException, which is not what we want.
    val sw = StringWriter(256)
    val pw = PrintWriter(sw, false)
    t.printStackTrace(pw)
    pw.flush()
    return sw.toString()
}
