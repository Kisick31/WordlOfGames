package com.example.worldofgames

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.worldofgames.fragments.FavouriteFragment
import com.example.worldofgames.fragments.SettingsFragment
import com.example.worldofgames.fragments.TopFragment
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.internal.NavigationMenuView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    companion object {
        val API_LVL = Build.VERSION.SDK_INT
        var wasLoaded = true
        var gameID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topFragment = TopFragment()
        val favouriteFragment = FavouriteFragment()
        val settingsFragment = SettingsFragment()

        makeCurrentFragment(topFragment)

        if(API_LVL<23){
            bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_video_game -> makeCurrentFragment(topFragment)
                R.id.ic_favorite -> makeCurrentFragment(favouriteFragment)
                else -> makeCurrentFragment(settingsFragment)
            }
            true
            }
        } else {
            navigationBar.setOnItemSelectedListener {
                when (it) {
                    R.id.ic_video_game -> makeCurrentFragment(topFragment)
                    R.id.ic_favorite -> makeCurrentFragment(favouriteFragment)
                    else -> makeCurrentFragment(settingsFragment)
                }
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

//  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val menuInflater = menuInflater
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.itemMain -> {
//                val intentToMainActivity = Intent(this, MainActivity::class.java)
//                startActivity(intentToMainActivity)
//            }
//            R.id.itemFavourite -> {
//                val intentToFavouriteActivity = Intent(this, FavouriteActivity::class.java)
//                startActivity(intentToFavouriteActivity)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

}