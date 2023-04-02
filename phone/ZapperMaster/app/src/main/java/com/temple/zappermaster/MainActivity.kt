package com.temple.zappermaster

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity(), IrInterface {

    val remoteViewModel: RemoteViewModel by lazy {
        ViewModelProvider(this)[RemoteViewModel::class.java]
    }

    /*
     * Notifications from UsbService will be received here.
     */
    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UsbService.ACTION_USB_PERMISSION_GRANTED -> Toast.makeText(
                    context,
                    "USB Ready",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.ACTION_USB_PERMISSION_NOT_GRANTED -> Toast.makeText(
                    context,
                    "USB Permission not granted",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.ACTION_NO_USB -> Toast.makeText(
                    context,
                    "No USB connected",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.ACTION_USB_DISCONNECTED -> Toast.makeText(
                    context,
                    "USB disconnected",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.ACTION_USB_NOT_SUPPORTED -> Toast.makeText(
                    context,
                    "USB device not supported",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private var usbService: UsbService? = null
    private var mHandler: MyHandler? = null
    private val usbConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            usbService = (arg1 as UsbService.UsbBinder).service
            usbService?.setHandler(mHandler)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            usbService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHandler = MyHandler(this)



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.test -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, TestFragment.newInstance("", ""))
                    .commit()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    public override fun onResume() {
        super.onResume()
        setFilters() // Start listening notifications from UsbService
        startService(
            UsbService::class.java,
            usbConnection,
            null
        ) // Start UsbService(if it was not started before) and Bind it
    }

    public override fun onPause() {
        super.onPause()
        unregisterReceiver(mUsbReceiver)
        unbindService(usbConnection)
    }

    private fun startService(
        service: Class<*>,
        serviceConnection: ServiceConnection,
        extras: Bundle?
    ) {
        if (!UsbService.SERVICE_CONNECTED) {
            val startService = Intent(this, service)
            if (extras != null && !extras.isEmpty) {
                val keys = extras.keySet()
                for (key in keys) {
                    val extra = extras.getString(key)
                    startService.putExtra(key, extra)
                }
            }
            startService(startService)
        }
        val bindingIntent = Intent(this, service)
        bindService(bindingIntent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun setFilters() {
        val filter = IntentFilter()
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED)
        filter.addAction(UsbService.ACTION_NO_USB)
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED)
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED)
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED)
        registerReceiver(mUsbReceiver, filter)
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler(activity: MainActivity) : Handler() {
        private val mActivity: WeakReference<MainActivity>

        init {
            mActivity = WeakReference(activity)
        }

        var command: StringBuilder = java.lang.StringBuilder("")

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    val data = msg.obj as String
                    Log.d("AAA", "message for serial port: $data")
                    if (data.contains('[')) {
                        Log.d("AAA", "Start")
                        command = java.lang.StringBuilder()
                        command.append(data)
                    }
                    else if (!data.contains(']')) {
                        Log.d("AAA", "Middle")
                        command.append(data)
                    }
                    else {
                        Log.d("AAA", "End")
                        command.append(data)
                        mActivity.get()!!.remoteViewModel.setLastIrCode(command.toString())
                    }
                    //mActivity.get()!!.display!!.append(data)
                }
                UsbService.CTS_CHANGE -> Toast.makeText(
                    mActivity.get(),
                    "CTS_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.DSR_CHANGE -> Toast.makeText(
                    mActivity.get(),
                    "DSR_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun sendIrCode(code: String) {
        if (usbService != null) { // if UsbService was correctly binded, Send data
            usbService!!.write(code.toByteArray(StandardCharsets.UTF_8))
        }
    }
}