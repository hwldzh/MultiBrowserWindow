package com.hwldzh.multibrowserwindow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.hwldzh.multibrowserwindow.databinding.ActivityBrowserMainBinding

/**
 * @author huangweiliang
 * @date 2021/12/8
 * 宿主Activity
 */
class BrowserMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrowserMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commit {
            add(R.id.content, MultiStackParentFragment())
        }
    }
}