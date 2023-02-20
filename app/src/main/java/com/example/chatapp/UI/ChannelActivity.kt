package com.example.chatapp.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.example.chatapp.databinding.ActivityChannelBinding
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Normal
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Thread
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.State.NavigateUp
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory

class ChannelActivity : AppCompatActivity() {

    companion object {
        private const val CID_KEY = "key:cid"

        fun newIntent(context: Context, channel: Channel): Intent =
            Intent(context, ChannelActivity::class.java).putExtra(CID_KEY, channel.cid)
    }

    private lateinit var binding: ActivityChannelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val channelID = checkNotNull(intent.getStringExtra(CID_KEY)){
            "Specifying a channel_id is required when starting ChannelActivity"
        }
        /* Create three separate ViewModels for the views so it's easy
           to customize them individually   */

        val factory = MessageListViewModelFactory(channelID)
        val messageListHeaderViewModel : MessageListHeaderViewModel by viewModels { factory }
        val messageListViewModel : MessageListViewModel by viewModels { factory }
        val messageInputViewModel: MessageInputViewModel by viewModels { factory }

        //   set custom Imgur attachment factory

        messageListViewModel.mode.observe(this){mode ->
            when(mode){
                 Normal -> {
                     messageListHeaderViewModel.resetThread()
                     messageInputViewModel.resetThread()
                 }
                is Thread -> {
                        messageListHeaderViewModel.setActiveThread(mode.parentMessage)
                        messageInputViewModel.setActiveThread(mode.parentMessage)
                }
            }
        }

        //  Let the message input know when we are editing a message
        binding.channelMessageListView.setMessageEditHandler(messageInputViewModel::postMessageToEdit)

        //   Handle navigate up state
        messageListViewModel.state.observe(this){state ->
            if (state is NavigateUp){
                finish()
            }
        }

        //  Handle back button behaviour correctly when you're in a thread
        val backButtonHandler  = {
            messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
        }
        binding.channelMessageListHeaderView.setBackButtonClickListener(backButtonHandler)
        onBackPressedDispatcher.addCallback(this){
            backButtonHandler()
        }
    }

}