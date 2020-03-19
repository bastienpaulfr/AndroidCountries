package fr.coppernic.lib.countries

import android.content.Context
import fr.coppernic.lib.countries.internal.CountryParser

@Suppress("MemberVisibilityCanBePrivate")
class CountryManager(context: Context) {

    val countries: Collection<Country> by lazy {
        CountryParser().parse(context.resources.openRawResource(R.raw.countries))
    }

    val countryByAlpha2Code: Map<String, Country> by lazy {
        countries.map { it.alpha2Code to it }.toMap()
    }

    val countryByAlpha3Code: Map<String, Country> by lazy {
        countries.map { it.alpha3Code to it }.toMap()
    }

    val countryByMrzCode: Map<String, Country> by lazy {
        countries.map { it.mrzCode to it }.toMap()
    }

    val countryByName: Map<String, Country> by lazy {
        countries.map { it.name to it }.toMap()
    }
}
