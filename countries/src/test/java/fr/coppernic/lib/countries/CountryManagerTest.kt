package fr.coppernic.lib.countries

import androidx.test.core.app.ApplicationProvider
import fr.coppernic.lib.test.RobolectricTest
import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test

class CountryManagerTest : RobolectricTest() {

    private lateinit var countryManager: CountryManager

    @Before
    fun setUp() {
        countryManager = CountryManager(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun getCountries() {
        val countries = countryManager.countries
        countries.size.`should be equal to`(250)
    }

    @Test
    fun getCountriesByAlpha2Code() {
        (countryManager.countryByAlpha2Code["FR"] ?: error("")).name.`should be equal to`("France")
    }

    @Test
    fun getCountriesByAlpha3Code() {
        (countryManager.countryByAlpha3Code["FRA"] ?: error("")).name.`should be equal to`("France")
    }

    @Test
    fun getCountriesByName() {
        (countryManager.countryByName["France"] ?: error("")).name.`should be equal to`("France")
    }
}