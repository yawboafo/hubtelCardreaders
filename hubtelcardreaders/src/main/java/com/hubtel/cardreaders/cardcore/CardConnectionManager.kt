package com.hubtel.cardreaders.cardcore


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log

class CardConnectionManager  {

    val REQUEST_ENABLE_BT = 10000
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()


    fun getCardDevices(){
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }else{
            EnableBluetooth()
        }
    }


    fun EnableBluetooth(){
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            //context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }



    fun findDevices(){
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        Log.d("debug", "size count "  +  pairedDevices?.size)
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address




            Log.d("debug", "Device name " +deviceName + "" +  deviceHardwareAddress)
        }
    }
}