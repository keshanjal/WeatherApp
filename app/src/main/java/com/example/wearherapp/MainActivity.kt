package com.example.wearherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.wearherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// api key  0e0f4a6453527bc3d2d0aecbcf346a30
class MainActivity : AppCompatActivity() {

    // view binding
    // to get all the ids of the views in the activity
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // fetch data from api
        fetchWeatherData("Delhi")

        // onSearch from search bar

        searchCity()
    }

    private fun searchCity(){
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityname:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityname, "0e0f4a6453527bc3d2d0aecbcf346a30", "metric")
        response.enqueue(object : Callback<WeatherApp>{
             override fun onResponse(call: Call<WeatherApp>,response: Response<WeatherApp>){
                 val responseBody = response.body()
                 if(response.isSuccessful && responseBody != null){
                     val temperature = responseBody.main.temp.toString()


                      val cityname = responseBody.name
                     val humidity = responseBody.main.humidity
                     val maxtemp = responseBody.main.temp_max
                     val mintemp= responseBody.main.temp_min

                     val windspeed = responseBody.wind.speed
                     val condition= responseBody.weather.firstOrNull()?.main ?: "Unknown"
                     val sunset = responseBody.sys.sunset
                     val sunrise= responseBody.sys.sunrise
                     val sealevel = responseBody.main.pressure


                     binding.temp.text = "$temperature °C"
                     binding.cityname.text = "$cityname"
                     binding.humidity.text = "$humidity %"
                     binding.maxtemp.text = "Max: $maxtemp °C"
                     binding.mintemp.text = "Min: $mintemp °C"
                     binding.day.text =  dayName(System.currentTimeMillis())
                     binding.date.text = date()
                     binding.windspeed.text = "$windspeed m/s"
                     binding.condition.text = "$condition"
                     binding.sunrise.text = "$sunrise"
                     binding.sunset.text = "$sunset"
                     binding.weather.text = "$condition"
                     binding.condition.text = "$condition"
                     binding.sea.text = "$sealevel hpa"




//                        Log.d("TAG", "onResponse: $temperature")

                 }
             }

             override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                 // on fail
                 Log.d("TAG", "onFailure: "+t.message)

             }

        })



        }
    fun dayName(timestamp:Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))

    }
    fun date():String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
}