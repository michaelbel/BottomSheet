# BottomSheet
BottomSheet dialogs library for Android

[![Platform](https://img.shields.io/badge/Platform-Android-blue.svg)](https://github.com/michaelbel/material)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg)](https://github.com/michaelbel/material)
[![Licence](https://img.shields.io/badge/License-Apache_v2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/michael-bel/maven/bottomsheet/images/download.svg) ](https://bintray.com/michael-bel/maven/bottomsheet/_latestVersion)

## Download
```gradle
dependencies {
    compile 'org.michaelbel:bottomsheet:0.0.3'
}
```

## Usage
```java
new BottomSheet.Builder(this);
    .setTitle(...);
    .setItems(..., ..., ...);
    .setCustomView(...);
    .setContentType(...);
    .setTitleTextColor(...);
    .setItemTextColor(...);
    .setBackgroundColor(...);
    .setIconColor(...);
    .setItemSelector(...);
    .setFullWidth(...);
    .setDarkTheme(...);
    .show();
```

## License

    Copyright 2016 Michael Bel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
