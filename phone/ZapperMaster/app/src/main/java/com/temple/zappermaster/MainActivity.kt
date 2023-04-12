package com.temple.zappermaster

import android.content.*
import android.content.res.AssetManager
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
import androidx.room.Room
import com.beust.klaxon.Klaxon
import com.temple.zappermaster.database.AppDatabase
import com.temple.zappermaster.database.Remote
import com.temple.zappermaster.database.RemoteConverter
import com.temple.zappermaster.database.RemoteDetail
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets

const val DATABASE_NAME = "database-name"

class MainActivity : AppCompatActivity(),RemoteListFragment.SelectionFragmentInterface, IrInterface {

    val remoteViewModel: RemoteViewModel by lazy {
        ViewModelProvider(this)[RemoteViewModel::class.java]
    }
    private val remoteListFragment = RemoteListFragment()
    private lateinit var db: AppDatabase

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

        // init database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        )
            .allowMainThreadQueries()
            .build()


        val remoteArray = ArrayList<RemoteObj>()
        var jsonString = getRemoteFile("device1.json", this)
        var jsonObject = JSONObject(jsonString)
        var jsonModel = jsonObject.getString("model_number")
        Log.d("AAA",jsonModel)
        var jsonButtons = jsonObject.getString("buttons")
        Log.d("AAA",jsonButtons)
        var jsonShared = jsonObject.getBoolean("shared")
        Log.d("AAA",jsonShared.toString())

        var remoteList =remoteViewModel.getRemoteList().value
        if(remoteList== null){
            remoteList = RemoteList()
        }

        Log.d("AAA", "Write remote list to database");

        var remoteObj = RemoteObj(jsonModel, jsonShared, jsonButtons)
        updateremoteToDatabase(remoteObj)
        // update view model
        var remoteListDao = db.remoteDao().getAll()
        var remoteConverter = RemoteConverter()
        var remoteObjList = remoteConverter.toObjList(remoteListDao)
        remoteArray.addAll(remoteObjList)

        remoteList.addAll(remoteArray)
        remoteViewModel.setRemoteList(remoteList)
        remoteViewModel.setSelectedRemote(null)
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
            R.id.remote -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_view,RemoteListFragment())
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

    private fun updateremoteToDatabase(remote: RemoteObj){
        var remoteConverter = RemoteConverter()
        val remoteDao = remoteConverter.toDao(remote)
        db.remoteDao().insert(remoteDao)
    }

//    private fun updateRemoteListToDatabase(bookArray: ArrayList<RemoteObj>) {
//        // delete the book list first
//        db.remoteDao().deleteAll()
//        // store list to database
//        val bookDaoList = RemoteConverter().toDaoList(bookArray)
//        db.remoteDao().insertAll(bookDaoList)
//    }
//    override fun onRestart() {
//        super.onRestart()
//        // restart the app
//        Log.d("AAA", "App is restarted")
//        if (remoteViewModel.getSelectedRemote().value != null) {
//            loadSelectedRemote(remoteViewModel.get.value)
//        }
//
//    }

    private fun loadSelectedRemote(name: String) {
        val remote = db.remoteDao().loadAllByModel(name);
        val usingRemote = RemoteConverter().toObj(remote)
        remoteViewModel.setSelectedRemote(usingRemote)
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        val remoteArray = ArrayList<RemoteObj>()
//        var jsonString = getRemoteFile("device1.json", this)
//        for (i in jsonString.indices) {
//            val remote = Klaxon().parse<RemoteObj>(jsonString)
//            remote?.run {
//                remoteArray.add(remote)
//            }
//
//        }
//        var remoteList =remoteViewModel.getRemoteList().value
//        if(remoteList== null){
//            remoteList = RemoteList()
//        }
//        remoteList.addAll(remoteArray)
//        remoteViewModel.setRemoteList(remoteList)
//        remoteViewModel.setSelectedRemote(null)
//        Log.d("AAA", "Write remote list to database");
//        updateRemoteListToDatabase(remoteArray)
//
//    }
    fun getRemoteFile(filename: String, context: Context): String {
        var manager : AssetManager = context.assets
        var file = manager.open(filename)
        var bytes = ByteArray(file.available())
        file.read(bytes)
        file.close()
        return String(bytes)
    }

    override fun remoteSelected() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, RemoteFragment())
            .commit()
        val selectedBook = remoteViewModel.getSelectedRemote().value
        if(selectedBook != null){
            val isInDatabase = db.remoteDetailDao().isRowIsExist(selectedBook.model_number)
            val remoteDetail = RemoteDetail(
                selectedBook.model_number,
                selectedBook.shared,
                selectedBook.buttons
            )

        }
        else{

        }


    }
}