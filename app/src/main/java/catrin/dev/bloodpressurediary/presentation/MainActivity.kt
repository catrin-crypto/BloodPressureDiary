package catrin.dev.bloodpressurediary.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import catrin.dev.bloodpressurediary.R
import catrin.dev.bloodpressurediary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavController()
        navController.navigate(R.id.fragment_blood_pressure_list)
    }

    private fun initNavController() {
        try {
            navController = findNavController(R.id.nav_host_fragment_activity_main)
        } catch (t: Throwable) {
            logAndToast(t)
        }
    }

    private fun logAndToast(t:Throwable) = logAndToast(t,this::class.java.toString())

    private fun logAndToast(t: Throwable, TAG:String) {
        try {
            Log.e(TAG, "", t)
            Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
        } catch (_: Throwable) {
        }
    }
}