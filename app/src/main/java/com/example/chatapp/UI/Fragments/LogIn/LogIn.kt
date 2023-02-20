package com.example.chatapp.UI.Fragments.LogIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.Models.ChatUser
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentChannelBinding
import com.example.chatapp.databinding.FragmentLogInBinding
import com.google.android.material.textfield.TextInputLayout

class LogIn : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        binding.goBTN.setOnClickListener {
            authenticateUser()
        }

        return binding.root
    }

    private fun authenticateUser(){
        val name = binding.nameET.text.toString()
        val userName = binding.userNameET.text.toString()

        val chatUser = ChatUser(name, userName)
        if (validateInput(name) && validateInput(userName)){
            val action = LogInDirections.actionLogInToChannel(chatUser)
            findNavController().navigate(action)
        }
    }
    private fun validateInput(inputText: String): Boolean{
        return inputText.length > 3
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}