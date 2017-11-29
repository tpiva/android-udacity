Popular Movies
======
This is Popular Movies Udacity's Android Developer Fast Track. This project was build in two steps as propose by Udacity's course, it has main objective to help user to discover popular and top rated movies on the web. It fetches themoviedb.org API to display the movies data, that contens all information of movies.

Step 1 and 2 of project was build in same repository, but I'll discribe features distinguished by steps.

Step 1 Features
-----
- Grid of movies' poster;
- Sort movies by top rated and popular via Settings;
- Allows user to click on an particular movie to see its details: title, description, date of release and user rating;

Step 2 Features
-----
- User can mark an movie as favorite, this will save an a database to user can see even without internet access;
- More extra details like: user's comments and trailer link (it'll open a youtube or browser, the last in case youtube not installed);
- More one way to sort movies, it was add Favorit sort;
- Handler of no internet access;
- Handler state during rotate;
- Tablet layout and landscape layout;

Screens
-----
- Phone's screen

![alt text](https://github.com/tpiva/Android/blob/master/AssociateAndroidDeveloperFastTrack/PopMovies/pictures/grid_movies.png "Main scrren")
![alt text](https://github.com/tpiva/Android/blob/master/AssociateAndroidDeveloperFastTrack/PopMovies/pictures/movies_details_1.png "Details 1")
![alt text](https://github.com/tpiva/Android/blob/master/AssociateAndroidDeveloperFastTrack/PopMovies/pictures/movie_details_2.png "Details 2")

- Tablet's screen


<p align="center">
<img src="https://github.com/tpiva/Android/blob/master/AssociateAndroidDeveloperFastTrack/PopMovies/pictures/main_screen_tablet.png">
<img src="https://github.com/tpiva/Android/blob/master/AssociateAndroidDeveloperFastTrack/PopMovies/pictures/details_screen_tablet.png">

How run it
------
It's necessary a API of [The movie DB](https://www.themoviedb.org/documentation/api). You can create an key with free account.
After your API key, it's necessary to set variable **MyTheMovieDBKey="your_api_key"** at file [gradle.properties](https://github.com/tpiva/Android/blob/master/AssociateAndroidDeveloperFastTrack/PopMovies/gradle.properties)

Libraries
------
This project use the following libraries:
- [Picasso](http://square.github.io/picasso/)

- [OkHttp](http://square.github.io/okhttp/)

License
------

> Copyright @2017 Thiago Piva Magalhães

> Licensed under the GNU General Public License v3.0; you may not use this file except in compliance with the License. You may obtain a copy of the License at

> http://www.gnu.org/licenses/gpl-3.0.html

> Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
