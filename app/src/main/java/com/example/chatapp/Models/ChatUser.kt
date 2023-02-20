package com.example.chatapp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ChatUser(
    val firstName: String,
    val userName: String
):  Parcelable
