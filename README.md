# BottomSheet
BottomSheet dialogs library for Android

[![Platform](https://img.shields.io/badge/Platform-Android-blue.svg)](https://github.com/michaelbel/material)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg)](https://github.com/michaelbel/material)
[![Licence](https://img.shields.io/badge/License-Apache_v2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/michael-bel/maven/bottomsheet/images/download.svg) ](https://bintray.com/michael-bel/maven/bottomsheet/_latestVersion)

<div style="dispaly:flex;">
    <img style="margin-left:0px;" src="/screens/light_1.png" width="24%">
    <img style="margin-left:0px;" src="/screens/light_2.png" width="24%">
    <img style="margin-left:0px;" src="/screens/light_3.png" width="24%">
    <img style="margin-left:0px;" src="/screens/light_4.png" width="24%">
    <img style="margin-left:0px;" src="/screens/dark_1.png" width="24%">
    <img style="margin-left:0px;" src="/screens/dark_2.png" width="24%">
    <img style="margin-left:0px;" src="/screens/dark_3.png" width="24%">
    <img style="margin-left:0px;" src="/screens/dark_4.png" width="24%">
</div>

<img style="margin-left:0px;" src="/screens/light_landscape.png" width="49%">
<img style="margin-left:0px;" src="/screens/dark_landscape.png" width="49%">

## Download
```gradle
dependencies {
    implementation 'org.michaelbel:bottomsheet:1.1.0'
}
```

## Usage
```java
new BottomSheet.Builder(this)
    .setTitle(...)
    .setItems(..., ..., ...)
    .setCustomView(...)
    .setContentType(...)
    .setTitleTextColor(...)
    .setItemTextColor(...)
    .setBackgroundColor(...)
    .setIconColor(...)
    .setItemSelector(...)
    .setFullWidth(...)
    .setCellHeight(...)
    .setDividers(...)
    .setDarkTheme(...)
    .setCallback(new BottomSheet.Callback() {
        @Override
        public void onOpen() {
        }
        
        @Override
        public void onClose() {
        }
    })
    .show();
```

## License

    Copyright 2016-2017 Michael Bel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
