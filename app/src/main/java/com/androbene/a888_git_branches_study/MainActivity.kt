package com.androbene.a888_git_branches_study

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val geoView = findViewById<TextView>(R.id.geo)
        geoView.text = "in USA: " + isInUSA(this).toString()//getUserCountry(this) ?: "not detected"

    }

    private fun isInUSA(context: Context): Boolean {
        return when (getUserCountry(context)) {
            "us" -> {
                logFB(GeoEv.G_TOTAL_US.name)
                true
            }
            "null" -> {
                false
            }
            else -> {
                logFB(GeoEv.G_TOTAL_OTHER_GEO.name)
                false
            }
        }
    }

    private fun getUserCountry(context: Context): String {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                logFB(GeoEv.G_COUNTRY_CODE_SIM.name)
                return simCountry.lowercase(Locale.US).also {
                    if (it == "us") logFB(GeoEv.G_USA_CODE_BY_SIM.name)
                }
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    logFB(GeoEv.G_COUNTRY_CODE_NET.name)
                    return networkCountry.lowercase(Locale.US).also {
                        if (it == "us") logFB(GeoEv.G_USA_CODE_BY_NET.name)
                    }
                }
            }
        } catch (e: Exception) {
            logFB(GeoEv.G_ERROR.name)
        }
        logFB(GeoEv.G_COUNTRY_CODE_NULL.name)
        return "null"
    }

    private fun logFB(event: String) {
        Log.d("lol", event)
        // firebaseAnalytics.logEvent(event) {}
    }

}

enum class GeoEv {
    G_COUNTRY_CODE_SIM,
    G_COUNTRY_CODE_NET,
    G_COUNTRY_CODE_NULL,
    G_USA_CODE_BY_SIM,
    G_USA_CODE_BY_NET,
    G_TOTAL_US,
    G_TOTAL_OTHER_GEO,
    G_ERROR
}