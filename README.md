# The Cat in the Hat

![can in the hat
](https://s1.piq.land/2016/03/08/j5dS33FDOWBd1Xor61JwCaL7_400x400.png)

> the sun did not shine.  
> it was too wet to play.  
> so we sat in the house.   
> all that cold, cold, wet day.  

[The cat in the hat, Dr. Seuss.](http://paulandlizdavies.com/poems/cat.htm)

### The goal of this project is to build a simple and lightweight weather mobile application that provides the weather for all the capital cities of the world.

To build this app you will have to use [Climacell](https://climacell.co) [Api](https://developer.climacell.co/docs)
Explore the api to understand which service suits you best.

Use https://restcountries.eu/rest/v2/all to get the needed value about all the capitals.

#### Required solution:
* Create a mobile app that uses [Climacell Api](https://developer.climacell.co/docs) to get the weather value needed for a weather app

#### UI:
* Home Screen - UITableView with search-bar containing the weather for all the capitals. Each cell should contain the following :
  * Name of the city
  * Max and Min temperature
  * Precipitation
* City details screen - Small map showing where this city is & UITableView with the weather for the next 5 days. Each cell should contain : 
  * Day
  * Max and Min temperature
  * Precipitation

#### Bonus points:
##### Small bonus
* Caching - cache locally (use db or in memory) the responses from the api and use them when navigating between screens
* Fahrenheit <-> Celsius - Add a button (right nav bar button) to toggle between the different metrics

##### Big bonus
* MapView - In the main screen add a button switch to a map view. show a pin of every capital with the name and temperature. enable selection of capital and navigation to details screen. 
* In the city forecast screen : add a scrollable graph displaying minute by minute temperature for the next 24 hours. (Applicable only for cities in the US)


#### Guidelines:
* Pay attention to SW design and clean code (We are believers of the [SOLID](https://en.wikipedia.org/wiki/SOLID) SW design principals).
* You are free to use any tool / library of your desire.
* We encourage using swift or kotlin

#### Before you start:
* Create a branch with your name from the `master` branch
* Request for an `apikey` to work with [Climacell Api](https://developer.climacell.co/docs)


Don't hesitate to contact with any question.
#### Good Luck!!!
