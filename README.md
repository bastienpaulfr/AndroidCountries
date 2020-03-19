[![Download](https://api.bintray.com/packages/bastienpaulfr/maven/AndroidCountries/images/download.svg)](https://bintray.com/bastienpaulfr/maven/AndroidCountries/_latestVersion)
[![Build Status](https://travis-ci.org/bastienpaulfr/AndroidCountries.svg?branch=master)](https://travis-ci.org/bastienpaulfr/AndroidCountries)

# AndroidCountries

Countries resources for Android

## Set Up


```groovy
repositories {
    maven { url 'https://dl.bintray.com/bastienpaulfr/maven/'}
}
```

### For country data

```groovy
dependencies {
    implementation 'fr.bipi.android:countries:0.1.1'
}
```

### For flag resources

```groovy
dependencies {
    implementation 'fr.bipi.android:flags:0.1.1'
}
```

## Presentation

### Country data

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

### Flags

The flags are named by their 2-letter ISO-3166 country code, except for the
constituent countries of Great Britain which have 6-letter codes "GB-ENG" etc).

Kosovo uses the user-assigned country code `XK`, which is not part of the ISO standard, but in use by several multinational organizations.

Flags are standard resources. Name of resource is `ic_{2-letter ISO-3166 country code}`

For example :

```
val resId = R.drawable.ic_fr
```

SVG counterpart of drawable resource is in `data/svg` folder

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

### Flag pictures

The source files were taken from Wikipedia and are not under copyright
protection since flags are effectively in public domain.

### Code

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

