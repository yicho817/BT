package com.example.bt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    // 在某個事件處理方法中，例如按鈕點擊事件
    fun onBluetoothButtonClicked(view: View) {
        val bluetoothIntent = Intent(this, BluetoothActivity::class.java)
        startActivity(bluetoothIntent)
    }

}

