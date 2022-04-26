package com.osman.materials.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity


abstract class DefaultBaseActivity : AppCompatActivity {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        KeyboardUtils.fixAndroidBug5497(this)
//        KeyboardUtils.fixSoftInputLeaks(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)
        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            if (w == null) return false
            w.getLocationOnScreen(scrcoords)
            val x = event.rawX + w.left - scrcoords[0]
            val y = event.rawY + w.top - scrcoords[1]
            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right
                        || y < w.top || y > w.bottom)
            ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        }
        return ret
    }


}
