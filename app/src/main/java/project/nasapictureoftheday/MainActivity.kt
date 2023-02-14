package project.nasapictureoftheday

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var imageView: ImageView
    private lateinit var date: TextView
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var image: Bitmap
    private val apiKey: String = "your-api-key";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        button.setOnClickListener {
            lifecycleScope.launch {
                processRequest()
            }
        }
    }

    private fun initViews() {
        apiService = ApiService.create()
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        date = findViewById(R.id.editTextDate)
        button = findViewById(R.id.button)
    }

    private suspend fun processRequest() {
        val dateToString = date.text.toString()
        val pictureOfTheDay = apiService.getPictureOfTheDay(apiKey, dateToString)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val imageURL = pictureOfTheDay.url
            try {
                val stream = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(stream)
                handler.post {
                    imageView.setImageBitmap(image)
                    textView.text = pictureOfTheDay.explanation
                    textView.movementMethod = ScrollingMovementMethod()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}