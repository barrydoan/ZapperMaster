package com.temple.zappermaster

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewRemoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewRemoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var txtName:TextView? = null
    private var txtCode:TextView? = null
    private var btnAdd:Button? = null
    private var btnDelete:Button? = null
    private var btnSave:Button? = null
    private var editorLayout:RelativeLayout? = null
    private var buttonList:MutableList<ButtonExtended> = ArrayList()
    private lateinit var remoteViewModel: RemoteViewModel
    private var width:Int? = null
    private var height:Int? = null
    private var modelNumber: String? = null
    private var type: String? = null
    private var manufacturer: String? = null

    private var selectedButton:ButtonExtended? = null


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
        var layout = inflater.inflate(R.layout.fragment_new_remote, container, false)
        txtName = layout.findViewById(R.id.txtButtonName)
        txtCode = layout.findViewById(R.id.txtButtonCode)
        btnAdd = layout.findViewById(R.id.btnAdd)
        btnDelete = layout.findViewById(R.id.btnDelete)
        btnSave = layout.findViewById(R.id.btnSave)
        editorLayout = layout.findViewById(R.id.editorLayout)
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
//        val edittext = EditText(requireContext())
//        val edittext2 = EditText(requireContext())
//        val edittext3 = EditText(requireContext())
        alert.setMessage("Please enter your remote information.")
        alert.setTitle("Creating new Remote")

        val alertDialogLayout = LinearLayout(requireContext())
        alertDialogLayout.orientation = LinearLayout.VERTICAL

        val model = EditText(requireContext())
        model.setSingleLine()
        model.hint = "Model Number"
        alertDialogLayout.addView(model)


        val typeOfRemote = EditText(requireContext())
        typeOfRemote.setSingleLine()
        typeOfRemote.hint = "Type"
        alertDialogLayout.addView(typeOfRemote)

        val brand = EditText(requireContext())
        brand.setSingleLine()
        brand.hint = "Manufacturer"
        alertDialogLayout.addView(brand)

        alertDialogLayout.setPadding(50, 40, 50, 10)
        alert.setView(alertDialogLayout)



        alert.setPositiveButton("Confirm"
        ) { _, _ -> //What ever you want to do with the value
            modelNumber = model.text.toString()
            type = typeOfRemote.text.toString()
            manufacturer = brand.text.toString()
        }

        alert.setNegativeButton("Cancel"
        ) { _, _ ->
            val intent = Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            requireContext().startActivity(intent)

        }

        alert.show()
        return layout
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        width = Resources.getSystem().displayMetrics.widthPixels
        height = Resources.getSystem().displayMetrics.heightPixels

        txtName?.addTextChangedListener {
            if (selectedButton != null) {
                selectedButton!!.text = it.toString()
            }
        }

        btnDelete?.setOnClickListener {
            if (selectedButton != null) {
                buttonList.remove(selectedButton)
                editorLayout!!.removeView(selectedButton)
            }
        }

        ViewModelProvider(requireActivity())[RemoteViewModel::class.java].getLastIrCode().observe(requireActivity()) {
            if (selectedButton != null) {
                var convertedCode = it.replace("[", "<")
                convertedCode = convertedCode.replace("]", ">")
                selectedButton?.code = convertedCode
                txtCode?.text = convertedCode
            }
        }

        btnSave?.setOnClickListener {
            (activity as DbInterface).saveRemote(modelNumber.toString(),type.toString(),manufacturer.toString(),false,buttonList)

        }

        btnAdd?.setOnClickListener {
            var button = ButtonExtended(requireContext())
            button.text = "New button"
            button.background = AppCompatResources.getDrawable(requireContext(),R.color.mid_blue)
            var layoutParam = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            button.setOnTouchListener { button, event ->
                resetBackgroundColorForButtonList()
                selectedButton = (button as ButtonExtended)
                selectedButton!!.setBackgroundColor(Color.BLUE)
                txtName?.text = selectedButton!!.text
                txtCode?.text = selectedButton!!.code

                if (event?.action == MotionEvent.ACTION_MOVE) {
                    layoutParam.leftMargin = event.rawX.toInt() - (button.width / 2)
                    layoutParam.topMargin = event.rawY.toInt() - (button.height / 2) - 400
                    if (layoutParam.leftMargin > 0 && layoutParam.leftMargin < width!!
                        && layoutParam.topMargin > 0 && layoutParam.topMargin < height!!) {
                        button.layoutParams = layoutParam
                    }
                    Log.d("AAA","Button save - left: ${button.leftPositionPercent}, top: ${button.topPositionPercent})")

                }
                button.background = AppCompatResources.getDrawable(requireContext(),R.color.mid_blue)
                // update location to buttonextended
                button.leftPositionPercent = layoutParam.leftMargin / (width!!.toDouble())
                button.topPositionPercent = layoutParam.topMargin / (height!!.toDouble())
                true
            }

            layoutParam.leftMargin = (width!! / 2).toInt() - 100
            layoutParam.topMargin = (height!! / 2).toInt()
            button.layoutParams = layoutParam
            editorLayout?.addView(button)
            buttonList.add(button)

        }
    }

    private fun resetBackgroundColorForButtonList() {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewRemoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewRemoteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}