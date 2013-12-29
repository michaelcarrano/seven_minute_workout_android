7 Minute Workout Android Application
=======

This application was developed to guide users through the [7 Minute Workout that was mentioned in the May/June 2013 issue of ACSM's Health & Fitness Journal](http://journals.lww.com/acsm-healthfitness/Fulltext/2013/05000/HIGH_INTENSITY_CIRCUIT_TRAINING_USING_BODY_WEIGHT_.5.aspx).

The application was developed using [Android Studio](https://developer.android.com/sdk/installing/studio.html) and supports Android 4.x devices. It makes use of the [Youtube Android API](https://developers.google.com/youtube/android/player/) to display videos natively within the application.

This application can be found on Google Play: [7 Minute Workout](https://play.google.com/store/apps/details?id=com.michaelcarrano.seven_min_workout).

Roadmap
=======
There is a lot that can be done to improve this application and below is just a short list of improvements I will be working on in the future.

* Add sound cue in between workout/rest so the user does not need to always look at the app.
* Improve layouts
	* Test layouts on all different screen sizes and densities.
	* Allow orientation change for [WorkoutCountdownActivity](https://github.com/michaelcarrano/seven_minute_workout_android/blob/master/7%20Minutes/src/main/java/com/michaelcarrano/seven_min_workout/WorkoutCountdownActivity.java)
	* Improve [CircularProgressBar](https://github.com/michaelcarrano/seven_minute_workout_android/blob/master/7%20Minutes/src/main/java/com/michaelcarrano/seven_min_workout/widget/CircularProgressBar.java) widget.
		* It skips a tick in the beginning (30 seconds to 28 seconds when exercising or 10 seconds to 8 seconds during rest)
* Handle orientation change for Youtube videos.
	* Videos play from beginning when orientation changes, instead of continuing with the video.
* Add ability to customize the exercise and rest countdown time.
* Add ability to randomize the workout order.
* Add ability to log your workout.
* Explore supporting Android 2.3.x devices.


License
=======

   	Copyright 2013 Michael Carrano

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
