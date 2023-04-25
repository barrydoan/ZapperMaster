package com.temple.zappermaster

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

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
    private var editorLayout:RelativeLayout? = null
    private var buttonList:MutableList<Button> = ArrayList()
    private var width:Int? = null
    private var height:Int? = null

    private var selectedButton:Button? = null


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
        editorLayout = layout.findViewById(R.id.editorLayout)
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

        btnAdd?.setOnClickListener {
            var button = Button(requireContext())
            button.text = "New button"
            button.setOnTouchListener { button, event ->
                selectedButton = (button as Button)
                txtName?.text = (button as Button).text
                if (event?.action == MotionEvent.ACTION_MOVE) {
                    val layoutParam = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParam.leftMargin = event.rawX.toInt() - (button!!.width / 2);
                    layoutParam.topMargin = event.rawY.toInt() - (button!!.height / 2) - 400
                    button.layoutParams = layoutParam
                }
                true
            }
            var layoutParam = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.leftMargin = (width!! / 2).toInt() - 100
            layoutParam.topMargin = (height!! / 2).toInt()
            button.layoutParams = layoutParam
            editorLayout?.addView(button)
        }
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