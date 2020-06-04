# Call-Stalker [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
A Call Stalker which runs a Service to save all call recordings to Firebase Storage on Client Side...


## Install

  - All you need to do is to create a firebase project, and add [google-services.json](https://firebase.google.com/docs/android/setup) file in app module directory.
  
    ```
      server/app/***
    ```
   
  - And Generate a Private Key ( json file ) for local Retrieve and add it in below path.
  
    ```
      localRetrieve/***
    ```
    
    **Note** : Go to this [link](https://console.firebase.google.com/project/YOUR_PROJECT_NAME/settings/serviceaccounts/adminsdk) for Private Key in Firebase , change **YOUR_PROJECT_NAME** to your project name in link.
    
## How to

  - Build the application and install it with below commands .
  
    ```cmd
      > cd server && gradlew installDebug 
    ```
    
  - If you have *Android Studio* you don't need to follow above thing, You know what to do .
  
  - After Installing it just Launch it once and grant permissions which it asked for .
  
  - And that's it , it will do it's things you can even close the app *no issues* .
  
  - You can check your firebase storage console for audio files , which will be periodically uploaded by the application .
  
  - ### Retrieve Localy 💣💣💣💣
    --------------
    - You can retireve it locally by the given tool .
    
      - Run the below commands to download files .
        
        - Change Directory . 
      
          ```cmd
            > cd localRetrieve
          ```
      
        - Install Dependencies .
      
          - yarn
        
            ```cmd
              > yarn 
            ```
        
          - npm
          
            ```cmd
              > npm install
            ```
        - Run
      
          - yarn
        
            ```cmd
              > yarn start
            ```
          - npm
        
            ```cmd
              > npm start
            ```
          
          
      - You can find the audio files in **files** folder .
        
          ```
            localRetrieve/files/***
          ```
        
   - Android Application check for internet connection and uploads the necessary audio files using **Thread Pool** .
   
## License 

  ```
    MIT License

    Copyright (c) 2020 Raja Ravi Prakash

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
  ```
  
          
          
