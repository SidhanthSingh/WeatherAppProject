package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.model.WeatherState
import com.example.weatherapp.model.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherState by viewModel.weatherState.collectAsState()
    var cityInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()) {

        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            placeholder = { Text("Search Location") },

            trailingIcon = {
                IconButton(onClick = { cityInput.let {
                    viewModel.fetchWeather(it, false)
                } }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth().padding(bottom = 8.dp),

            colors = textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            )
        )

        when (weatherState) {
            is WeatherState.Error -> {
                val error = (weatherState as WeatherState.Error).error
                Text(text = "Error: ${error.message}")
            }
            WeatherState.Loading -> {
                //TODO
            }
            is WeatherState.Success -> {
                val weatherResponse = (weatherState as WeatherState.Success).weatherResponse
                val showingDetails = (weatherState as WeatherState.Success).showingDetails

                val cityName = weatherResponse.location.name
                val temperature = weatherResponse.current.temp_c
                val conditionIcon = weatherResponse.current.condition.icon

                if (showingDetails) {
                   WeatherIcon(conditionIcon)
                   TemperatureView(location = cityName, temperature = "${temperature}°")
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        WeatherDetailItem(value = "${weatherResponse.current.humidity}%", title = "Humidity")
                        WeatherDetailItem(value = "${weatherResponse.current.uv}%", title = "UV Index")
                        WeatherDetailItem(value = "${weatherResponse.current.feelslike_c}%", title = "Feels Like")
                    }
                } else {
                    WeatherCard(
                        city = cityName,
                        temp = "${temperature}°",
                        conditionIcon = conditionIcon,
                        onClick = {
                            viewModel.toggleDetails()
                            viewModel.saveCityToPreferences(cityName)
                        }
                    )
                }
            }
            WeatherState.Default -> {
                DefaultView()
            }
        }
    }
}

@Composable
fun WeatherCard(city: String, temp: String, conditionIcon: String
                , onClick: () -> Unit) {
    val imageUrl = "https:$conditionIcon"

    Box(modifier = Modifier.clickable(onClick = onClick)) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 12.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = city,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = temp,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }
    }
}



@Composable
fun DefaultView() {
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No City Selected",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please Search For A City",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                color = Color.Black
            )
        }
}

@Composable
fun WeatherIcon(iconUrl: String) {
    val imageUrl = "https:$iconUrl"
    Column(
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun TemperatureView(location: String, temperature: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = location,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = temperature,
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
    }
}


@Composable
fun WeatherDetailItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
            color = Color.Gray
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
