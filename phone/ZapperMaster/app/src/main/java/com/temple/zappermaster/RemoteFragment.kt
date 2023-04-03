package com.temple.zappermaster

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RemoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var buttonDTOList: MutableList<ButtonDTO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout =  inflater.inflate(R.layout.fragment_remote, container, false)
        var jsonString = getRemoteFile("remote_ui.json", requireContext())
        var jsonObject = JSONObject(jsonString)
        var jsonArray = jsonObject.getJSONArray("buttons")
        // convert to buttonDTO

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

        var buttonList: MutableList<Button> = ArrayList()

        var layout2 = layout.findViewById<View>(R.id.constraint_layout)

        var width = Resources.getSystem().displayMetrics.widthPixels
        var heigh = Resources.getSystem().displayMetrics.heightPixels

        for (buttonDTO in buttonDTOList) {
            var button = MaterialButton(requireContext())
            button.width = 75
            button.height = 150
            button.cornerRadius = 50
            button.iconSize = 75
            button.backgroundTintList = getResources().getColorStateList(R.color.ic_tint_color)
            Log.d("AAA: Button Name", buttonDTO.displayName)

            if (buttonDTO.displayName == "PresetUp") {
                button.icon = getResources().getDrawable(R.drawable.ic_skip_next)
            } else if (buttonDTO.displayName == "Off") {
                button.icon = getResources().getDrawable(R.drawable.ic_power)
            } else if (buttonDTO.displayName == "Up") {
                button.icon = getResources().getDrawable(R.drawable.ic_arrow_up)
            } else if (buttonDTO.displayName == "Down") {
                button.icon = getResources().getDrawable(R.drawable.ic_arrow_down)
            } else if (buttonDTO.displayName == "presetDown") {
                button.icon = getResources().getDrawable(R.drawable.ic_skip_previous)
            } else if (buttonDTO.displayName == "VolUp") {
                button.icon = getResources().getDrawable(R.drawable.ic_volume_up)
            } else if (buttonDTO.displayName == "ChannelUp") {
                button.icon = getResources().getDrawable(R.drawable.ic_add)
            } else if (buttonDTO.displayName == "VolDown") {
                button.icon = getResources().getDrawable(R.drawable.ic_volume_down)
            } else if (buttonDTO.displayName == "ChannelDown") {
                button.icon = getResources().getDrawable(R.drawable.ic_remove)
            } else {
                button.text = buttonDTO.displayName
            }


            Log.d("AAA", "${button.width}")

            var layoutParam = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.leftMargin = (buttonDTO.leftPositionPercent * width).toInt() - 100
            layoutParam.topMargin = (buttonDTO.topPositionPercent * heigh).toInt()

            button.layoutParams = layoutParam
//            ConstraintLayout.addView(button)
            (layout2 as RelativeLayout).addView(button)
        }

        return layout
    }



    fun getRemoteFile(filename: String, context: Context): String {
        var manager : AssetManager = context.assets
        var file = manager.open(filename)
        var bytes = ByteArray(file.available())
        file.read(bytes)
        file.close()
        return String(bytes)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment remoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RemoteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}