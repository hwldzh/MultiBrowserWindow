package com.hwldzh.multibrowserwindow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import com.hwldzh.multibrowserwindow.databinding.FragmentNavHostBinding

/**
 * @author huangweiliang
 * @date 2021/12/8
 * 多窗口中，每一个窗口都有自己的返回栈
 * 每个返回栈都有自己的 fragmentManager，因此这里使用一个无ui的Fragment作为返回栈的载体
 */
class NavHostFragment(var name: String, var windowIndex: Int): Fragment() {
    private lateinit var binding: FragmentNavHostBinding
    private val TAG: String = "NavHostFragment"
    var curSelectFragment: Fragment? = null
    var curChildFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //点击返回键，将当前栈的Fragment作出栈处理
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = childFragmentManager.backStackEntryCount > 0
                if (isEnabled) childFragmentManager.popBackStackImmediate()
//                else requireActivity().onBackPressedDispatcher.onBackPressed()
                else requireActivity().finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nav_host, container, false)
        binding = FragmentNavHostBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tag = name
        //创建窗口的第一个页面，即首页Fragment
        if (childFragmentManager.findFragmentByTag(tag) == null) {
//            Log.i(TAG, "commit now, $name")
            //这里使用的是childFragmentManager
            childFragmentManager.commitNow {
                val multiChildFragment = MultiChildFragment(name, 1, this@NavHostFragment)
                add(R.id.content, multiChildFragment, tag) //不能使用replace，否则每次返回都要重建
            }
        }
        Log.i(TAG, "$name: onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "$name: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "$name: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "$name: onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "$name: onDestroy")
    }
}