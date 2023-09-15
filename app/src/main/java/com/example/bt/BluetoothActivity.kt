package com.example.bt

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.UUID

class BluetoothActivity : AppCompatActivity() {

    // 用於藍牙連接權限請求的請求代碼
    private val permissionRequestBluetoothConnect = 1

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var enableBtLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        // 初始化 BluetoothAdapter
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        // 註冊 ActivityResultLauncher 以處理啟用藍牙的結果
        enableBtLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 藍牙已啟用，可以在這裡繼續建立藍牙連線
                connectToBluetoothDevice()
            } else {
                // 用戶拒絕啟用藍牙或出現其他錯誤，可以顯示錯誤消息或採取其他措施
            }
        }

        // 檢查設備是否支援藍芽
        if (bluetoothAdapter == null) {
            // 設備不支援藍芽，可以在這裡處理錯誤或提示用戶
        } else {
            // 檢查藍芽是否已啟用
            if (!bluetoothAdapter.isEnabled) {
                // 請求啟用藍芽
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                // 檢查藍牙權限
                val bluetoothPermission = Manifest.permission.BLUETOOTH
                val bluetoothAdminPermission = Manifest.permission.BLUETOOTH_ADMIN
                if (ContextCompat.checkSelfPermission(
                        this,
                        bluetoothPermission
                    ) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this,
                        bluetoothAdminPermission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // 如果缺少藍牙權限，請求權限
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(bluetoothPermission, bluetoothAdminPermission),
                        permissionRequestBluetoothConnect
                    )
                    return
                }
                // 啟動藍牙啟用請求
                enableBtLauncher.launch(enableBtIntent)
            } else {
                // 藍牙已啟用，可以在此繼續建立藍牙連線
                connectToBluetoothDevice()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionRequestBluetoothConnect) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用戶授予了藍牙權限，繼續檢查藍牙狀態和啟用
                connectToBluetoothDevice()
            } else {
                // 用戶拒絕了藍牙權限，可以顯示錯誤消息或採取其他措施
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == permissionRequestBluetoothConnect) {
            if (resultCode == Activity.RESULT_OK) {
                // 藍牙已啟用，可以在這裡繼續建立藍牙連線
                connectToBluetoothDevice()
            } else {
                // 用戶拒絕啟用藍牙，可以顯示錯誤消息或採取其他措施
            }
        }
    }

    private fun connectToBluetoothDevice() {
        // 假設你已經確定要連接的藍牙設備的 MAC 地址
        val deviceAddress = "00:00:00:00:00:00"
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 使用高級的藍牙權限
            val bluetoothPermission = Manifest.permission.BLUETOOTH_CONNECT

            if (ContextCompat.checkSelfPermission(this, bluetoothPermission) != PackageManager.PERMISSION_GRANTED) {
                // 如果缺少藍牙連接權限，請求權限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(bluetoothPermission),
                    permissionRequestBluetoothConnect
                )
                return
            }
        } else {
            // 使用兼容的藍牙權限
            val bluetoothPermission = Manifest.permission.BLUETOOTH

            if (ContextCompat.checkSelfPermission(this, bluetoothPermission) != PackageManager.PERMISSION_GRANTED) {
                // 如果缺少藍牙權限，請求權限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(bluetoothPermission),
                    permissionRequestBluetoothConnect
                )
                return
            }
        }

        // 使用 UUID 創建一個藍牙連線
        val socket = device.createRfcommSocketToServiceRecord(UUID.fromString("your_uuid_here"))

        // 開始連線
        socket.connect()

        // 在這裡可以使用 socket 來傳送和接收資料
    }
}
