<!-- PROJECT LOGO -->
<a name="readme-top"></a>

## Hisaab
 <p align="left">
A lightweight minimalistic spending tracker to helps us keep monitoring our expenses.
 </p>

## This aap has two screen
1) Profile screen where user set his name and mention daily, weekly and monthly max spending targets. 
2) Main screen that constantly shows user his daily, weekly, monthly spending details and whether the limits set is exceeded or not...

* Typically MVP approach is perfect match for building this small two screens only app.
* We also know MVVM is even better when dealing with remote data sources yet it is good only when app is relatively small (between 5- 10 screen max)or code management goes beyond..
* I kept this project small  so that it will be easy to understand and implement CLEAN architecture pattern.

### To understand this project You must be familiar with...

* Kotlin
* Generics
* HILT2
* Flow basics
* RoomDB
* Navigation Component
* Android Studio IDE

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### The code is divided in to five layers of CLEAN architecture pattern.


<b>1. Presentation.</b> This layer is used to interact with UI classes/elements(Activity, Fragment etc..)for respective App screens/views and their view models, adapters and other required classes to generate and work with and on UI thread. This layer also interact with UseCases layer for fulfilment of more deeper actions.

<b>2. Use cases.</b> This layer also called Interactors too. These are mainly set of predefined actions that are mostly triggered by user interactions like button clicks etc... Generally this is the point where we should switch from main UI thread to background thread to perform the processing by interacting with Domain and or Data layer.

<b>3. Domain.</b> This layer also referred as business logic layer. These are the rules of your business logic that is core functioning of our app. It also contains all the data models defined too. <i>Ideally, this should be the biggest layer in traditional software development, but ironically Android Apps usually tend to just draw an API based data on the screen of a phone</i>, so most of the core logic will consist of requesting and persisting data (defined in data layer), reducing this layer to much much smaller or almost empty code portion in Android development. This is virtually innermost layer, anything you defined here can be used in all the others layers but this layer should not be dependent on classes & interfaces of other layers.

<b>4. Data.</b> This layer keeps the abstract definitions and implementations of specific data usage. A place that hold repositories of remote/local data sources that uses respective Frameworks.

<b>5. Framework.</b> Encapsulates an interaction with framework so its code can be agnostic & reusable if we want the same framework to be used in other projects/apps this may consist of Room connection, Retrofit or device sensors initialization code. This must be as simple as possible for a framework to build as all the specific usages logic for it is placed in other layers mostly in data & other layers if necessary in rare scenario.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### License
```
   Copyright (C) 2023 HARSHAL TILAY

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Developer Info
Name: Harshal Tilay</br>
Country: India (GMT+5.30)</br></br>
Android Studio | VS Code | Unity | Sublimetext</br>
Kotlin | Java | C# | C++ JNI (Working knowledge)</br>
HTML | JavaScript | PHP <br>
NoSQL| MySQL | SQLite | Google App Engine | Firebase/FireStore</br>
REST | JSON | GraphQL | XML</br>
SmartFox Server | Photon Cloud/Server | Custom Socket Servers | Amazon Image Rekognition API</br>
Git | Jira</br>
Windows | MacOS</br>


## Disclaimer
This is my hobby project mostly completed in spare time and has been tested on Emulator only.


<p align="right">(<a href="#readme-top">back to top</a>)</p>

