package com.tang.floatbottombutton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val fragments: ArrayList<Fragment> = arrayListOf(
        FirstFragment.newInstance("First"),
        FirstFragment.newInstance("Second"),
        FirstFragment.newInstance("Third"),
        FirstFragment.newInstance("Fourth"),
        FirstFragment.newInstance("Fifth")
    )

    val icons: ArrayList<Int> = arrayListOf(
        R.drawable.select_monitor_icon,
        R.drawable.select_monitor_icon,
        R.drawable.select_monitor_icon,
        R.drawable.select_monitor_icon,
        R.drawable.select_monitor_icon
    )

    val titles: ArrayList<String> = arrayListOf(
        "监控",
        "监控",
        "监控",
        "监控",
        "监控"
    )

    lateinit var adapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MainViewPagerAdapter(this, fragments)
        vp_main.adapter = adapter
        CustomTabLayoutMediator(tabLayout, vp_main, CustomTabLayoutMediator.TabConfigurationStrategy {
                tab, position ->
            val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.item_custom_tab, tab.view, false)
            val tvTab = view.findViewById<View>(R.id.tv_tab) as TextView
            tvTab.setText(titles.get(position))
            if (icons.size % 2 == 1 && position == icons.size / 2){
            }else{
                val ivTabIcon = view.findViewById<View>(R.id.iv_tab_icon) as ImageView
                ivTabIcon.setImageResource(icons.get(position))
            }
            tab.customView = view
            tab.view.setPadding(0, 0, 0, 0)
        }).attach()
    }
}
