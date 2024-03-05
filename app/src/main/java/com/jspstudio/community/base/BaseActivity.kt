package com.jspstudio.community.base

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int,
    tag : String
) : AppCompatActivity() {

    protected val TAG = tag
    lateinit var binding : T
    private val callback = object : OnBackPressedCallback(true) { override fun handleOnBackPressed() { finish() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //context = this
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        //this.onBackPressedDispatcher.addCallback(this, callback)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            android.R.id.home ->{
//                finish()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
}