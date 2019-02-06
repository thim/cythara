[![Build Status](https://travis-ci.org/thim/cythara.svg?branch=master)](https://travis-ci.org/thim/cythara)

# Cythara
A musical instrument tuner for Android.

It's a fork from Cythara with same changes:
* Converted the code to Kotlin
* Organized the layers using Clean Architecture
* New layouts
* Removed third-party components

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"
      alt="Get it on Google Play"
      height="80">](https://play.google.com/store/apps/details?id=net.thm.cythara)

## Functionality

* Provides tunings for various instruments and supports chromatic tuning.
* Displays deviations between -30 and 30 cents.
* Supports scientific pitch notation and Solfège.

## Tests

Run `./gradlew test` to run all unit tests. In addition, there are UI tests based on image comparisons which
can be run using `./gradlew connectedCheck`. The reference images are generated using a Nexus 5X emulator
(resolution: 1080 x 1920, 420 dpi) with API level 26.

## Libraries

The Tarsos DSP library (https://github.com/JorenSix/TarsosDSP) is used for pitch detection.

## License

Cythara is licensed under the GPL, version 3. A copy of the license is included in LICENSE.txt.

# Contributors

In chronological order:
* [mtbu](https://github.com/mtbu) added the violing tuning
* [afmachado](https://github.com/afmachado) provided the translation to Brazilian Portuguese
* [tebriz159](https://github.com/tebriz159) created the logo
* [toXel](https://github.com/toXel) provided the translation to German
* [TacoTheDank](https://github.com/TacoTheDank) enabled the installation on external storage, upgraded the language level and updated dependencies
* [thim](https://github.com/thim) added the cello tuning, fixed issues and updated library versions

Thank you all!

## Screenshots

![Main screen](/screenshots/screen1.png?raw=true) <!-- .element height="50%" width="50%" -->
![Tunings](/screenshots/screen2.png?raw=true) <!-- .element height="50%" width="50%" -->
![Main screen dark mode](/screenshots/screen3.png?raw=true) <!-- .element height="50%" width="50%" -->
![Choose frequency](/screenshots/screen4.png?raw=true) <!-- .element height="50%" width="50%" -->
![Choose notation](/screenshots/screen5.png?raw=true) <!-- .element height="50%" width="50%" -->