package com.temple.dynanicui

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var jsonString = getRemoteFile("remote_ui.json", this)
        var jsonObject = JSONObject(jsonString)
        var jsonArray = jsonObject.getJSONArray("buttons")
        // convert to buttonDTO
        var buttonDTOList: MutableList<ButtonDTO> = ArrayList()
        for (i in 1..jsonArray.length()) {
            var json = jsonArray.getJSONObject(i - 1)
            var buttonDTO = ButtonDTO(
                json.getString("background_color"),
                json.getString("size"),
                json.getDouble("top_position_percent"),
                json.getDouble("left_position_percent"),
                json.getString("display_name"),
                json.getString("code")
            )
            buttonDTOList.add(buttonDTO)
        }
        // create the ui
        var layout: RelativeLayout = findViewById(R.id.constraint_layout)
        var buttonList: MutableList<Button> = ArrayList()


        var width = Resources.getSystem().displayMetrics.widthPixels
        var heigh = Resources.getSystem().displayMetrics.heightPixels

        for (buttonDTO in buttonDTOList) {
            var button = MaterialButton(this)
            button.width = 75
            button.height = 150
            button.cornerRadius = 50
            button.iconSize =  75
            button.backgroundTintList = getResources().getColorStateList(R.color.ic_tint_color);

//
            if(buttonDTO.displayName == "PresetUp"){
                button.icon = getResources().getDrawable(R.drawable.ic_skip_next)
            }

            else if(buttonDTO.displayName == "Off"){
                button.icon = getResources().getDrawable(R.drawable.ic_power)
            }
            else if (buttonDTO.displayName == "Up"){
                button.icon = getResources().getDrawable(R.drawable.ic_arrow_up)
            }
            else if (buttonDTO.displayName == "Down"){
                button.icon = getResources().getDrawable(R.drawable.ic_arrow_down)
            }

            else if (buttonDTO.displayName == "presetDown"){
                button.icon = getResources().getDrawable(R.drawable.ic_skip_previous)
            }

            else if (buttonDTO.displayName == "VolUp"){
                button.icon = getResources().getDrawable(R.drawable.ic_volume_up)
            }
            else if (buttonDTO.displayName == "ChannelUp"){
                button.icon = getResources().getDrawable(R.drawable.ic_add)
            }else if (buttonDTO.displayName == "VolDown"){
                button.icon = getResources().getDrawable(R.drawable.ic_volume_down)
            }
            else if (buttonDTO.displayName == "ChannelDown"){
                button.icon = getResources().getDrawable(R.drawable.ic_remove)
            }
            else{
                button.text = buttonDTO.displayName
            }


            Log.d("AAA", "${button.width}")

            var layoutParam = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParam.leftMargin = (buttonDTO.leftPositionPercent * width).toInt() - 100
            layoutParam.topMargin = (buttonDTO.topPositionPercent * heigh).toInt()

            button.layoutParams = layoutParam
            layout.addView(button)

        }

    }

    fun getRemoteFile(filename: String, context: Context): String {
        var manager : AssetManager = context.assets
        var file = manager.open(filename)
        var bytes = ByteArray(file.available())
        file.read(bytes)
        file.close()
        return String(bytes)
    }
}