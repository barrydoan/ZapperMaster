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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.temple.zappermaster.database.AppDatabase
import com.temple.zappermaster.database.Remote
import com.temple.zappermaster.database.RemoteConverter
import org.json.JSONArray
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets


const val DATABASE_NAME = "database-name"

class MainActivity : AppCompatActivity(), RemoteListFragment.SelectionFragmentInterface,
    IrInterface, DbInterface {

    val remoteViewModel: RemoteViewModel by lazy {
        ViewModelProvider(this)[RemoteViewModel::class.java]
    }
    private val remoteListFragment = RemoteListFragment()
    private val editorFragment = EditorFragment()
    private lateinit var db: AppDatabase
    private lateinit var tabLayout: TabLayout
    private lateinit var buttonObj: ButtonObj

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
        // load remote to data base
        loadRemoteList()
        loadRemoteListFromLocalDatabase()

        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("AAA", "onTabSelected")
                // show local remote list
                if (tab?.text == resources.getString(R.string.tab_local)) {
                    loadRemoteListFromLocalDatabase()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, remoteListFragment)
                        .commit()
                } else if (tab?.text == resources.getString(R.string.tab_server)) {
                    loadRemoteListFromApi()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, remoteListFragment)
                        .commit()

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("AAA", "onTabUnselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("AAA", "onTabReselected")
            }
        })
        // load remote list fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, remoteListFragment)
            .commit()


    }

    private fun loadRemoteListFromLocalDatabase() {
        var remoteList = remoteViewModel.getRemoteList().value
        if (remoteList == null) {
            remoteList = RemoteList()
        }

        // update view model
        var remoteListDao = db.remoteDao().getAll()
        var remoteConverter = RemoteConverter()
        var remoteObjList = remoteConverter.toObjList(remoteListDao)
        Log.d("AAA", "Remote Array - $remoteObjList")

        remoteList.addAll(remoteObjList)
        remoteViewModel.setRemoteList(remoteList)
        remoteViewModel.setSelectedRemote(null)
    }

    private fun loadRemoteListFromApi() {
        Helper.api.getRemoteList(this, "", object : Helper.api.Response {
            override fun processResponse(response: JSONObject) {
                Log.d("AAA", response.toString())
                // extract
                var remoteListJson = response.getJSONArray("results")
                var remoteObjList = ArrayList<RemoteObj>()
                if (remoteListJson.length() > 0) {
                    for (i in 1..remoteListJson.length()) {
                        var remoteJson = remoteListJson.getJSONObject(i - 1)
                        var remoteObj = RemoteObj(
                            remoteJson.getString(Constants.REMOTE_MODEL_NUMBER),
                            remoteJson.getBoolean(Constants.REMOTE_SHARED),
                            remoteJson.getString(Constants.REMOTE_BUTTONS),
                            "TV",
                            "Samsung"
                        )
                        Log.d("AAA","remote obj $remoteObj")
                        remoteObjList.add(remoteObj)
                    }
                }
                // set remote list to view model

                var remoteList = remoteViewModel.getRemoteList().value
                if (remoteList == null) {
                    remoteList = RemoteList()
                }
                remoteList.removeAll()
                remoteList.addAll(remoteObjList)
                remoteViewModel.setRemoteList(remoteList)
                remoteViewModel.setSelectedRemote(null)
            }
        })
    }

    private fun loadRemoteList() {
        //Load device 1
        var remoteObj = loadRemote("device1.json")
        updateremoteToDatabase(remoteObj)
        remoteObj = loadRemote("device2.json")
        updateremoteToDatabase(remoteObj)
        remoteObj = loadRemote("device3.json")
        updateremoteToDatabase(remoteObj)
    }

    private fun loadRemote(filename: String): RemoteObj {
        var jsonString = getRemoteFile(filename, this)
        var jsonObject = JSONObject(jsonString)
        var model = jsonObject.getString(Constants.REMOTE_MODEL_NUMBER)
        var buttons = jsonObject.getString(Constants.REMOTE_BUTTONS)
        var shared = jsonObject.getBoolean(Constants.REMOTE_SHARED)
        var type = jsonObject.getString(Constants.REMOTE_TYPE)
        var manufacture = jsonObject.getString(Constants.REMOTE_MANUFACTURE)
        return RemoteObj(model, shared, buttons, type, manufacture)
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
                    .replace(R.id.fragment_container_view, remoteListFragment)
                    .commit()
                true
            }
            R.id.design_remote -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, editorFragment)
                    .commit()
                true
            }
            R.id.newRemote -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, NewRemoteFragment.newInstance("", ""))
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
                    } else if (!data.contains(']')) {
                        Log.d("AAA", "Middle")
                        command.append(data)
                    } else {
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

    private fun updateremoteToDatabase(remoteDto: RemoteObj) {
        var remoteConverter = RemoteConverter()
        val remote = remoteConverter.toDao(remoteDto)
        db.remoteDao().insert(remote)

    }

    override fun saveRemote(
        name: String,
        type: String,
        manufacture: String,
        shared: Boolean,
        buttonExtendedList: MutableList<ButtonExtended>
    ) {
        Log.d("AAA","save remote called")

        var jArray = JSONArray()
        buttonExtendedList.forEach { item ->
            var jObject = JSONObject()
            jObject.put("background_color","white")
            jObject.put("size","normal")
            jObject.put("top_position_percent",item.topPositionPercent)
            jObject.put("left_position_percent",item.leftPositionPercent)
            jObject.put("display_name",item.text.toString())
            jObject.put("code",item.code)


            Log.d("AAA","Json Object $jObject")
            jArray.put(jObject)

        }
        var remote: Remote = Remote(name, shared, jArray.toString(), false, type, manufacture)
        db.remoteDao().insert(remote)
    }


    private fun loadSelectedRemote(name: String) {
        val remote = db.remoteDao().loadAllByModel(name);
        val usingRemote = RemoteConverter().toObj(remote)
        remoteViewModel.setSelectedRemote(usingRemote)
    }

    fun getRemoteFile(filename: String, context: Context): String {
        var manager: AssetManager = context.assets
        var file = manager.open(filename)
        var bytes = ByteArray(file.available())
        file.read(bytes)
        file.close()
        return String(bytes)
    }

    override fun remoteSelected() {

        val selectedBook = remoteViewModel.getSelectedRemote().value
        if (selectedBook != null) {
            Log.d("AAA", "atMain-${selectedBook.buttons}")
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, RemoteFragment())
            .commit()
    }

}