package com.hwldzh.multibrowserwindow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import com.hwldzh.multibrowserwindow.databinding.FragmentMultiChildBinding

/**
 * @author huangweiliang
 * @date 2021/12/9
 * 多窗口中，具体展示的每一个Fragment
 */
class MultiChildFragment(var name: String, var depth: Int, var hostFragment: Fragment) : Fragment() {
    private lateinit var binding: FragmentMultiChildBinding
    val TAG = "MultiChildFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "$name-$depth: onCreateView")
        val view = inflater.inflate(R.layout.fragment_multi_child, container, false)
        binding = FragmentMultiChildBinding.bind(view)
        binding.fragmentName.text = "$name-$depth"
        init()
        return view
    }

    private fun init() {
        binding.preFragmentBtn.setOnClickListener {
            var childFragmentManager = hostFragment.childFragmentManager
            var isEnabled = childFragmentManager.backStackEntryCount > 0
            if (isEnabled) childFragmentManager.popBackStackImmediate()
//            else requireActivity().onBackPressedDispatcher.onBackPressed()
            else requireActivity().finish()
        }

        binding.nextFragmentBtn.setOnClickListener {
            if (depth < 13) {
                parentFragmentManager.commit {
                    addToBackStack(name)
                    val curSelectFragment = (hostFragment as NavHostFragment).curSelectFragment
                    val multiChildFragment = MultiChildFragment(name, depth + 1, hostFragment)
                    if (multiChildFragment != curSelectFragment) {
                        curSelectFragment?.let {
//                            Log.i(TAG, "hide curFragment")
                            setMaxLifecycle(it, Lifecycle.State.STARTED)
                            hide(it) //隐藏上一个Fragment
                        }
                        add(R.id.content, multiChildFragment)
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "该窗口的Fragment层级已达到上限",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "$name-$depth: onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "$name-$depth: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "$name-$depth: onResume")
        (hostFragment as NavHostFragment).curSelectFragment = this
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "$name-$depth: onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "$name-$depth: onDestroy")
    }
}