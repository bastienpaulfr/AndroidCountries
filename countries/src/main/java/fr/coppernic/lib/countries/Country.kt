package fr.coppernic.lib.countries

data class Country(
    /**
     * Alpha 2 Code
     */
    val alpha2Code: String,
    /**
     * Alpha 3 Code
     */
    val alpha3Code: String,
    /**
     * Code used in machine readable passport
     */
    val mrzCode: String,
    /**
     * Codes from the National Crime and Information Center (NCIC) 2000 standard.
     */
    val ncicCode: String,
    /**
     * Alternative spelling of country name
     */
    val altSpellings: List<String>,
    /**
     * Flag URL
     */
    val flag: String,
    /**
     * Name of this country
     */
    val name: String,
    /**
     * Native name of this country
     */
    val nativeName: String,
    /**
     * Translation of this country name in
     * - de
     * - es
     * - fr
     * - ja
     * - it
     * - br
     * - pt
     * - nl
     * - hr
     * - fa
     */
    val translations: Map<String, String>
)
