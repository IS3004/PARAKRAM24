package com.explore.parakram24

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.explore.parakram24.databinding.ActivityMainBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.appBar.videoView
        val videoPath = """android.resource://$packageName/${R.raw.backgroundvideo}"""
        videoView.setVideoPath(videoPath)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.setVolume(0f, 0f)
        }
        videoView.start()

        //notification
        askNotificationAndSmsPermission()
        FirebaseMessaging.getInstance().subscribeToTopic("parakram").addOnCompleteListener{
            if (it.isSuccessful) {
                Log.d("SUBSCRIBE", "subscribed")
            } else {
                Log.d("SUBSCRIBE", "subscription failed")
            }
        }.addOnFailureListener {
            Log.d("failure",it.toString())
        }

        // password dialog for admins
//        val alertDialog = LayoutInflater.from(this).inflate(R.layout.password_dialog, null)
//        val builder = AlertDialog.Builder(this)
//        builder.setView(alertDialog)
//        builder.setTitle("Enter Password")
//        builder.setCancelable(false)
//        val button = alertDialog.findViewById<AppCompatButton>(R.id.btn_check)
//        val buildd= builder.create()
//        button.setOnClickListener {
//            val editText = alertDialog.findViewById<AppCompatEditText>(R.id.et_password).text.toString()
//            Firebase.database.reference.child("password").get().addOnCompleteListener {
//                if(editText== it.result.value){
//                    buildd.dismiss()
//                }
//                else{
//                    Toast.makeText(this,"Reenter password",Toast.LENGTH_SHORT).show()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this,"Reenter password",Toast.LENGTH_SHORT).show()
//            }
//        }
//        buildd.show()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.aboutUsFragment,
                R.id.sponsorsFragment,
                R.id.eventsFragment,
                R.id.indiEventFragment,
                R.id.coreTeamFragment,
                R.id.notificationFragment,
                R.id.announcementFragment
            ), binding.drawerLayout
        )


        binding.appBar.btnMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START, true)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START, true)
            }

        }

        binding.drawerLayout.setScrimColor(getResources().getColor(R.color.transparent))

//        binding.drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
//
//                // Scale the View based on current slide offset
//                val diffScaledOffset: Float = slideOffset * (1 - 0.64f)
//                val offsetScale = 1 - diffScaledOffset
//                binding.appBar.appBarConstraintLayout.scaleX = offsetScale
//                binding.appBar.appBarConstraintLayout.scaleY = offsetScale
//
//                // Translate the View, accounting for the scaled width
//                val xOffset = drawerView.width * slideOffset
//                val xOffsetDiff: Float =
//                    binding.appBar.appBarConstraintLayout.width * diffScaledOffset / 2
//                val xTranslation = xOffset - xOffsetDiff
//                binding.appBar.appBarConstraintLayout.translationX = xTranslation
//            }
//
//        }
//        )


        navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {

                binding.appBar.videoView.visibility = View.VISIBLE
                videoView.start()
            } else {
                binding.appBar.videoView.visibility = View.GONE
                videoView.stopPlayback()
            }
            when (destination.id) {
                R.id.homeFragment -> binding.navView.setCheckedItem(R.id.homeFragment)
                R.id.aboutUsFragment -> binding.navView.setCheckedItem(R.id.aboutUsFragment)
                R.id.sponsorsFragment -> binding.navView.setCheckedItem(R.id.sponsorsFragment)
                R.id.eventsFragment -> binding.navView.setCheckedItem(R.id.eventsFragment)
                R.id.merchandiseFragment -> binding.navView.setCheckedItem(R.id.merchandiseFragment)
                R.id.coreTeamFragment -> binding.navView.setCheckedItem(R.id.coreTeamFragment)
                R.id.indiEventFragment -> binding.navView.setCheckedItem(R.id.eventsFragment)
                R.id.EditableIndividualEventFragment -> binding.navView.setCheckedItem(R.id.eventsFragment)
                R.id.notificationFragment -> binding.navView.setCheckedItem(R.id.notificationFragment)
                R.id.announcementFragment -> binding.navView.setCheckedItem(R.id.announcementFragment)

                else -> binding.navView.setCheckedItem(R.id.homeFragment)
            }
            val fragmentId = destination.id
            var fragmentTitle = ""

            if (fragmentId == R.id.homeFragment) {
                fragmentTitle = "Home"
            } else if (fragmentId == R.id.sponsorsFragment) {
                fragmentTitle = "Sponsors"
            } else if (fragmentId == R.id.aboutUsFragment) {
                fragmentTitle = "About Us"
            } else if (fragmentId == R.id.eventsFragment) {
                fragmentTitle = "Events"
            } else if (fragmentId == R.id.coreTeamFragment) {
                fragmentTitle = "Core Team"
            } else if (fragmentId == R.id.merchandiseFragment) {
                fragmentTitle = "Merchandise"
            } else if (fragmentId == R.id.announcementFragment){
                fragmentTitle = "Announcements"
            }
            // change 1


            binding.appBar.tvTitle.text = fragmentTitle


        }
        binding.navView.setupWithNavController(navController)
        binding.navView.setCheckedItem(R.id.homeFragment)


    }

    private fun shareApp() {
        val playStoreLink = "https://play.google.com/store/apps/details?id=com.explore.parakram24"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: $playStoreLink")

        startActivity(Intent.createChooser(intent, "Share App via"))
    }
    override fun onResume() {
        super.onResume()
        videoView.start()

    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
    }

    private fun askNotificationAndSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = android.Manifest.permission.POST_NOTIFICATIONS

            if (ContextCompat.checkSelfPermission(
                    this,
                    notificationPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(notificationPermission)
            }
        }
    }

}

