package fr.coppernic.lib.countries.internal

import fr.coppernic.lib.countries.Country
import fr.coppernic.lib.test.RobolectricTest
import org.amshove.kluent.`should contain all`
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class CountryParserTest : RobolectricTest() {

    private val countriesString = "[\n" +
        "  {\n" +
        "    \"alpha2Code\": \"AF\",\n" +
        "    \"alpha3Code\": \"AFG\",\n" +
        "    \"mrzCode\": \"AFG\",\n" +
        "    \"ncicCode\": \"AF\", " +
        "    \"altSpellings\": [\n" +
        "      \"AF\",\n" +
        "      \"Afġānistān\"\n" +
        "    ],\n" +
        "    \"flag\": \"https://restcountries.eu/data/afg.svg\",\n" +
        "    \"name\": \"Afghanistan\",\n" +
        "    \"nativeName\": \"افغانستان\",\n" +
        "    \"translations\": {\n" +
        "      \"de\": \"Afghanistan\",\n" +
        "      \"es\": \"Afganistán\",\n" +
        "      \"fr\": \"Afghanistan\",\n" +
        "      \"ja\": \"アフガニスタン\",\n" +
        "      \"it\": \"Afghanistan\",\n" +
        "      \"br\": \"Afeganistão\",\n" +
        "      \"pt\": \"Afeganistão\",\n" +
        "      \"nl\": \"Afghanistan\",\n" +
        "      \"hr\": \"Afganistan\",\n" +
        "      \"fa\": \"افغانستان\"\n" +
        "    }\n" +
        "  },\n" +
        "  {\n" +
        "    \"alpha2Code\": \"AX\",\n" +
        "    \"alpha3Code\": \"ALA\",\n" +
        "    \"mrzCode\": \"ALA\",\n" +
        "    \"ncicCode\": \"\", " +
        "    \"altSpellings\": [\n" +
        "      \"AX\",\n" +
        "      \"Aaland\",\n" +
        "      \"Aland\",\n" +
        "      \"Ahvenanmaa\"\n" +
        "    ],\n" +
        "    \"flag\": \"https://restcountries.eu/data/ala.svg\",\n" +
        "    \"name\": \"Åland Islands\",\n" +
        "    \"nativeName\": \"Åland\",\n" +
        "    \"translations\": {\n" +
        "      \"de\": \"Åland\",\n" +
        "      \"es\": \"Alandia\",\n" +
        "      \"fr\": \"Åland\",\n" +
        "      \"ja\": \"オーランド諸島\",\n" +
        "      \"it\": \"Isole Aland\",\n" +
        "      \"br\": \"Ilhas de Aland\",\n" +
        "      \"pt\": \"Ilhas de Aland\",\n" +
        "      \"nl\": \"Ålandeilanden\",\n" +
        "      \"hr\": \"Ålandski otoci\",\n" +
        "      \"fa\": \"جزایر الند\"\n" +
        "    }\n" +
        "  }]"

    private lateinit var parser: CountryParser

    @Before
    fun setUp() {
        parser = CountryParser()
    }

    @Test
    fun parse() {
        val l = parser.parse(countriesString.byteInputStream())
        Timber.v("$l")
        val ll = listOf(
            Country(
                alpha2Code = "AF",
                alpha3Code = "AFG",
                mrzCode = "AFG",
                ncicCode = "AF",
                altSpellings = listOf(
                    "AF",
                    "Afġānistān"
                ),
                flag = "https://restcountries.eu/data/afg.svg",
                name = "Afghanistan",
                nativeName = "افغانستان",
                translations = mapOf(
                    Pair("de", "Afghanistan"),
                    Pair("es", "Afganistán"),
                    Pair("fr", "Afghanistan"),
                    Pair("ja", "アフガニスタン"),
                    Pair("it", "Afghanistan"),
                    Pair("br", "Afeganistão"),
                    Pair("pt", "Afeganistão"),
                    Pair("nl", "Afghanistan"),
                    Pair("hr", "Afganistan"),
                    Pair("fa", "افغانستان")
                )
            ),
            Country(
                alpha2Code = "AX",
                alpha3Code = "ALA",
                mrzCode = "ALA",
                ncicCode = "",
                altSpellings = listOf(
                    "AX",
                    "Aaland",
                    "Aland",
                    "Ahvenanmaa"
                ),
                flag = "https://restcountries.eu/data/ala.svg",
                name = "Åland Islands",
                nativeName = "Åland",
                translations = mapOf(
                    Pair("de", "Åland"),
                    Pair("es", "Alandia"),
                    Pair("fr", "Åland"),
                    Pair("ja", "オーランド諸島"),
                    Pair("it", "Isole Aland"),
                    Pair("br", "Ilhas de Aland"),
                    Pair("pt", "Ilhas de Aland"),
                    Pair("nl", "Ålandeilanden"),
                    Pair("hr", "Ålandski otoci"),
                    Pair("fa", "جزایر الند")
                )
            )
        )
        l.`should contain all`(ll)
    }

    @Test
    fun test() {
        Child("jean", "dupond")
    }

    infix fun Int.add(x: Int) = this + x

    fun max(a: Int, b: Int) = if (a > b) a else b
    fun max2(a: Int, b: Int): Int {
        if (a > b) return a else return b
    }

    open class Base(name: String) {
        init {
            println("base")
        }

        open val i = name.length.also { println("Base size $it") }
    }

    class Child(name: String, last: String) : Base(name) {
        init {
            println("child")
        }

        override val i = last.length.also { println("Child size $it") }
    }
}
