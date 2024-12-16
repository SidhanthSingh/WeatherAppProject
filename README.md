Weather App

A weather forecasting app that fetches weather data from the Weather API, allowing users to search for cities and view current weather conditions, including temperature, humidity, UV index, and more. 
Users can also view detailed weather information for a specific city by clicking on the weather card.

Features

Search for a city to get the current weather.
View detailed weather information (humidity, UV index, feels-like temperature, etc.).
Save the last searched city and show detailed weather on app launch.

Setup Instructions

1. Clone the Repository
Start by cloning the repository to your local machine. In your terminal or command prompt, run:

bash
Copy code
git clone https://github.com/your-username/weather-app.git
Navigate to the project directory:

2. Open the Project in Android Studio
Open Android Studio.
Click on Open an existing project and select the folder where you cloned the repository.
3. Set Up API Key
The app fetches weather data using the Weather API. You need an API key to make requests.

Steps:

Go to Weather API and sign up for a free API key.
Copy the API key you receive.
In your project, locate the WeatherRepository class in the repository package.
Find the line where the API key is passed:
kotlin
Copy code
private val apiKey = "your_api_key_here"
Replace "your_api_key_here" with the API key you copied.
4. Build and Run the App
Once you’ve set up the API key, you can build and run the app in Android Studio:

App Features Walkthrough
Home Screen
On app launch, the app displays a list of weather cards showing basic weather information (temperature, condition, etc.) for the last searched city (if saved).
Users can click on a card to view detailed weather information (humidity, UV index, feels-like temperature, etc.).
Searching for a City
Enter a city name in the search field and click the search button to fetch the weather data for the city.
The app will display a weather card with basic information and save the city for later use.
Viewing Detailed Weather
After searching for a city, click the weather card to toggle between the summary view and detailed weather information.

