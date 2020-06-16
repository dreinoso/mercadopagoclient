package com.chainreaction.mercadopagoclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chainreaction.mercadopagoclient.databinding.MainActivityBinding
import com.chainreaction.mercadopagoclient.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
