package com.example.chatapp.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.chatapp.Models.ChatUser
import com.example.chatapp.R
import com.example.chatapp.UI.Fragments.LogIn.LogInDirections
import com.example.chatapp.databinding.ActivityMainBinding
import io.getstream.chat.android.client.ChatClient

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val client = ChatClient.instance()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.fragmentContainerView)

        if (navController.currentDestination?.label.toString().contains("log_in")) {
            val currentUser = client.getCurrentUser()
            if (currentUser != null) {
                val user = ChatUser(currentUser.name, currentUser.id)
                val action = LogInDirections.actionLogInToChannel(user)
                navController.navigate(action)
            }
        }


    }
}