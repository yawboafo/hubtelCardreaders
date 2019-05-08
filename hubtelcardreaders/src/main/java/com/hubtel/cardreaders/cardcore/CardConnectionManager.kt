package com.hubtel.cardreaders.cardcore


import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import com.hubtel.cardreaders.cardModels.CardReader
import java.io.IOException
import java.lang.Exception

class CardConnectionManager()  {



    val REQUEST_ENABLE_BT = 10000
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    var listOfCardReaders = arrayListOf<CardReader>()

    fun ISBLTEnabled() : Boolean{
        return bluetoothAdapter != null
    }








    fun openBlueToothSettings(context: Activity){



        context.startActivity( Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS))

    }

    fun getConnectedCardReader(): String {

        findDevices()


        if(listOfCardReaders.isNullOrEmpty())

            return  "Not connected"
             else
        return  listOfCardReaders.get(0).name
    }


    fun findDevices(){
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        Log.d("debug", "size count "  +  pairedDevices?.size)
        pairedDevices?.forEach { device ->

            if (device.name.contains("Miura")){

                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address

                listOfCardReaders.add(CardReader(deviceName, deviceHardwareAddress))

                Log.d("debug", "Device name " +deviceName + "" +  deviceHardwareAddress)
            }






        }



    }

    private fun connectDevice(reader: CardReader){

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        if(!pairedDevices.isNullOrEmpty()){
            val device = pairedDevices?.filter { it.address == reader.macAdd }?.first()

            Log.d("debug","device name "+ device.name)

            connectBTL(device).start()
        }


    }

    private inner class connectBTL(device: BluetoothDevice) : Thread() {


        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(CPConstant.UUID)
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            try{
                mmSocket?.use { socket ->
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()

                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    // manageMyConnectedSocket(socket)
                }
            }catch (e: Exception){

                Log.e("debugError",e.toString())
            }


        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()

            } catch (e: IOException) {
                Log.e("Debug", "Could not close the client socket", e)
            }
        }
    }
}