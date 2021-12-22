package com.hwldzh.multibrowserwindow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import com.hwldzh.multibrowserwindow.databinding.FragmentMultiStackParentBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.isNullOrEmpty

/**
 * @author huangweiliang
 * @date 2021/12/9
 * 包含多个返回栈的父Fragment
 */
class MultiStackParentFragment: Fragment() {
    private lateinit var binding: FragmentMultiStackParentBinding
    private var windowNum: Int = 0
    /**
     * 当前窗口的Index
     */
    private var curWindowIndex: Int = 0
    /**
     * 记录创建的所有窗口对象
     */
    private val mStackList = ArrayList<NavHostFragment>()
    /**
     * 返回栈顺序,存储返回栈id
     */
    private val mOrderStack = ArrayDeque<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requireActivity().onBackPressedDispatcher.addCallback(
//            this,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    if (!mOrderStack.isNullOrEmpty()) {
//                        //移除栈顶 stackId
//                        mOrderStack.removeFirst()
//                        if (mOrderStack.isNotEmpty()) {
//                            //将新的栈顶 显示
//                            transWindowIndex(mOrderStack.first)
//                            return
//                        }
//                    }
//                    requireActivity().supportFragmentManager.popBackStack()
//                }
//            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_multi_stack_parent, container, false)
        binding = FragmentMultiStackParentBinding.bind(view)
        init()
        return view
    }

    private fun init() {
        addWindow()
        //添加窗口
        binding.addIcon.setOnClickListener {
            addWindow()
        }
        //切换窗口
        binding.ok.setOnClickListener {
            val transWindowIndex = binding.windowIndexEdit.text.toString().toInt()
            transWindowIndex(transWindowIndex)
        }
    }

    /**
     * 切换窗口
     * transWindowIndex: 需要切换窗口的Index
     * todo：//考虑将前一个Fragment隐藏掉，而不是detach掉
     */
    private fun transWindowIndex(transWindowIndex: Int) {
        if (transWindowIndex > windowNum) {
            Toast.makeText(requireContext(), "切换的窗口Index不能大于当前窗口数", Toast.LENGTH_SHORT).show()
            return
        }
//        mOrderStack.remove(transWindowIndex)
//        mOrderStack.push(transWindowIndex)
        childFragmentManager.commitNow {
            mStackList.forEach { item ->
                if (item.windowIndex == transWindowIndex) {
                    setMaxLifecycle(item, Lifecycle.State.RESUMED)
                    show(item)
                    setPrimaryNavigationFragment(item)
                } else {
                    setMaxLifecycle(item, Lifecycle.State.STARTED)
                    hide(item)
                }
            }
        }
    }

    /**
     * 添加窗口，childFragmentManager管理着以该Fragment作为父容器的所有子Fragment
     */
    private fun addWindow() {
        childFragmentManager.commitNow {
            //NavHostFragment代表一个窗口对象
            val navHostFragment = NavHostFragment("窗口${++windowNum}", windowNum)
            curWindowIndex = windowNum
            mStackList.add(navHostFragment)
            add(R.id.content_fragment, navHostFragment) //添加窗口
            binding.windowNum.text = "窗口数$windowNum"
        }
        transWindowIndex(curWindowIndex)
    }
}