package fr.coppernic.lib.countries.internal

import android.util.JsonReader
import android.util.JsonToken
import fr.coppernic.lib.countries.Country
import fr.coppernic.lib.countries.log.LogDefines
import fr.coppernic.lib.countries.log.LogDefines.LOG
import fr.coppernic.lib.countries.log.error
import java.io.InputStream
import java.io.InputStreamReader

class CountryParser {

    fun parse(stream: InputStream): List<Country> {
        val reader = JsonReader(InputStreamReader(stream, "UTF-8"))
        return try {
            readCountryArray(reader)
        } catch (e: Exception) {
            LOG.error(e)
            emptyList()
        }
    }

    private fun readCountryArray(reader: JsonReader): List<Country> {
        val countries = ArrayList<Country>()
        reader.beginArray()
        while (reader.hasNext()) {
            countries.add(readCountryObject(reader))
        }
        reader.endArray()
        return countries
    }

    private fun readCountryObject(reader: JsonReader): Country {
        var alpha2Code = ""
        var alpha3Code = ""
        var mrzCode = ""
        var altSpelling = emptyList<String>()
        var flag = ""
        var name = ""
        var nativeName = ""
        var translation = emptyMap<String, String>()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "alpha2Code" -> {
                    alpha2Code = reader.nextString()
                }
                "alpha3Code" -> {
                    alpha3Code = reader.nextString()
                }
                "mrzCode" -> {
                    mrzCode = reader.nextString()
                }
                "altSpellings" -> {
                    altSpelling = readAltSpelling(reader)
                }
                "flag" -> {
                    flag = reader.nextString()
                }
                "name" -> {
                    name = reader.nextString()
                }
                "nativeName" -> {
                    nativeName = reader.nextString()
                }
                "translations" -> {
                    if (LogDefines.verbose) {
                        LOG.trace("Reading translation for {}", name)
                    }
                    translation = readTranslation(reader)
                }
            }
        }
        reader.endObject()
        return Country(alpha2Code, alpha3Code, mrzCode, altSpelling, flag, name, nativeName, translation)
    }

    private fun readAltSpelling(reader: JsonReader): List<String> {
        val list = ArrayList<String>()
        reader.beginArray()
        while (reader.hasNext()) {
            list.add(reader.nextString())
        }
        reader.endArray()
        return list
    }

    private fun readTranslation(reader: JsonReader): Map<String, String> {
        val translations = HashMap<String, String>()
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            val value = when (reader.peek()) {
                JsonToken.STRING -> reader.nextString()
                else -> {
                    reader.skipValue()
                    ""
                }
            }
            translations[name] = value
        }
        reader.endObject()
        return translations
    }

}
