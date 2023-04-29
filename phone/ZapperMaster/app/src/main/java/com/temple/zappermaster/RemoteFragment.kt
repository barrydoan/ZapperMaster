package com.temple.zappermaster

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import org.json.JSONArray


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
    lateinit var remoteViewModel: RemoteViewModel
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
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        remoteViewModel = ViewModelProvider(requireActivity()).get(RemoteViewModel::class.java)
        // Inflate the layout for this fragment
        var layout =  inflater.inflate(R.layout.fragment_remote, container, false)


        var remote = remoteViewModel.getSelectedRemote().value
        Log.d("AAA","remote-${remote.toString()}")

        var jsonString = remote?.buttons
        Log.d("AAA","Json String in RemoteFragment $jsonString")
        var jsonArray = JSONArray()

        if( remote != null){
            jsonArray = JSONArray(jsonString)

        }
        else{
            Log.d("AAA","Remote Not Exist")
        }

        Log.d("AAA", jsonString.toString())

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
        var layout2 = layout.findViewById<View>(R.id.constraint_layout)

        var width = Resources.getSystem().displayMetrics.widthPixels
        var heigh = Resources.getSystem().displayMetrics.heightPixels



        Log.d("AAA","created layout: width $width, height: $heigh")

        for (buttonDTO in buttonDTOList) {
            var button = MaterialButton(requireContext())
            button.width = 75
            button.height = 150
            button.cornerRadius = 50
            button.iconSize = 100
            button.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_TOP)
            button.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.ic_tint_color)
            Log.d("AAA: Button Name", buttonDTO.displayName)

            if (buttonDTO.displayName == Constants.ICON_PRESET_UP) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_skip_next)
            } else if (buttonDTO.displayName == Constants.ICON_OFF) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_power)
            } else if (buttonDTO.displayName == Constants.ICON_UP) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up)
            } else if (buttonDTO.displayName == Constants.ICON_Down) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
            } else if (buttonDTO.displayName == Constants.ICON_PRESET_DOWN) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_skip_previous)
            } else if (buttonDTO.displayName == Constants.ICON_VOL_UP) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_volume_up)
            } else if (buttonDTO.displayName == Constants.ICON_CHANNEL_UP) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add)
            } else if (buttonDTO.displayName == Constants.ICON_VOL_DOWN) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_volume_down)
            } else if (buttonDTO.displayName == Constants.ICON_CHANNEL_DOWN) {
                button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_remove)
            } else {
                button.text = buttonDTO.displayName
            }


            Log.d("AAA", "${button.width}")

            var layoutParam = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.leftMargin = (buttonDTO.leftPositionPercent * width).toInt()
            layoutParam.topMargin = (buttonDTO.topPositionPercent * heigh).toInt()

            button.layoutParams = layoutParam
            button.setOnClickListener {
                var button = it as Button
                var code = findCommandByText(button.text.toString(), buttonDTOList)
                Log.d("AAA", "Send code $code")
                (activity as IrInterface).sendIrCode(code)
            }

            (layout2 as RelativeLayout).addView(button)
        }

        return layout
    }



    fun findCommandByText(text: String, buttonList: List<ButtonDTO>): String {
        var result = "";
        for (buttonDTO in buttonList) {
            if (buttonDTO.displayName == text) {
                result = buttonDTO.code
                break;
            }
        }
        return result;
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