package com.example.arduinocontroll

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.Job


class MainActivity : AppCompatActivity() {
    lateinit var port: UsbSerialPort
    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        initializeDevice.setOnClickListener {
            initDevice()

//            job = scope.launch {
//                while(true) {
//                    getData() // the function that should be ran every second
//                    delay(1000)
//                }
//            }
        }

        innerLed.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                val data = "1".toByteArray()
                port.write(data, 1000*5)
            }else{
                val data = "2".toByteArray()
                port.write(data, 1000*5)
            }
        }
    }


    fun initDevice(){
        // Find all available drivers from attached devices.
        // Find all available drivers from attached devices.
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val availableDrivers: List<UsbSerialDriver> =
            UsbSerialProber.getDefaultProber().findAllDrivers(manager)
        if (availableDrivers.isEmpty()) {
            return
        }

        // Open a connection to the first available driver.
        // Open a connection to the first available driver.
        val driver: UsbSerialDriver = availableDrivers[0]

        //manager.requestPermission(driver.getDevice(), mPermissionIntent);

        val connection = manager.openDevice(driver.getDevice())
            ?: // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return

        port = driver.getPorts().get(0) // Most devices have just one port (port 0)

        port.open(connection)
        port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        port.close()
    }
}