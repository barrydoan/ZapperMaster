package com.temple.editlayout

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var layout: RelativeLayout = findViewById(R.id.relative_layout)
        var button: Button = Button(this)
        button.setText("Click")
        layout.addView(button)

        var width = Resources.getSystem().displayMetrics.widthPixels
        var heigh = Resources.getSystem().displayMetrics.heightPixels



        button.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                if (event?.action== MotionEvent.ACTION_MOVE) {
                    var layoutParam = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                    layoutParam.leftMargin = event.rawX.toInt() - (view!!.width / 2);
                    layoutParam.topMargin = event.rawY.toInt() - (view!!.height / 2) - 180
                    view?.layoutParams = layoutParam
                }
                return true
            }

        })
    }
}