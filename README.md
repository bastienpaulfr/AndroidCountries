[![Download](https://api.bintray.com/packages/coppernic/maven/AndroidCountries/images/download.svg)](https://bintray.com/coppernic/maven/AndroidCountries/_latestVersion)
[![Build Status](https://travis-ci.org/Coppernic/AndroidCountries.svg?branch=master)](https://travis-ci.org/Coppernic/AndroidCountries)

# AndroidCountries

Countries resources for Android

## Set Up


```groovy
repositories {
    maven { url 'https://dl.bintray.com/bastienpaulfr/maven/'}
}

dependencies {
    implementation 'fr.coppernic.lib:countries:0.1.0'
}
```

## Presentation

Data is gotten from https://restcountries.eu/

Data is stored in a json file. This file is parsed during the first access to data.

```kotlin
// Create a countryManager
val countryManager = CountryManager(context) // Context is used for accessing to raw resource where json file is stored.

// getting the full list of countries
val countries: Collection<Country> = countryManager.countries

// Getting a country by name
val france: Country = countryManager.countryByName["France"]

//Getting a country vy alpha3 code
val france: Country = countryManager.countryByAlpha3Code["FRA"]

//Getting a country vy alpha2 code
val france: Country = countryManager.countryByAlpha2Code["FR"]

```

### Logs

This library uses [SLF4J](http://www.slf4j.org/) for logging. Please use android binding to
log into logcat. More info on [Android binding](http://www.slf4j.org/android/)

```groovy
dependencies {
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-android
    implementation 'org.slf4j:slf4j-android:1.7.30'
}
```

You can also bind SLF4J to timber. In this case please use this dependency

```groovy
dependencies {
    implementation 'com.arcao:slf4j-timber:3.1'
}
```

To activate verbose logging, please add this into your code :

```java
LogDefines.setVerbose(true);
```

Sometimes, it can have log for profiling, in this case, to activate them please add
this in code :

```java
LogDefines.setProfile(true);
```

One `TAG` is used for all logging from lib : `AndroidCountries`. It would be easy to filter on this tag if you
want to disable all logging from lib. Filtering can be done with `Timber` and a `Tree`
from [Treessence](https://github.com/bastienpaulfr/Treessence)


```groovy
dependencies {
    implementation 'fr.bipi.treessence:treessence:0.3.0'
}
```

## License

    Copyright (C) 2020 Bastien Paul

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

