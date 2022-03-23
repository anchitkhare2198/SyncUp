package com.example.syncup.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.syncup.R
import com.example.syncup.databinding.ActivityHomepageBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class HomepageActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityHomepageBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
        binding?.navView?.setNavigationItemSelectedListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarHomepage)
        binding?.toolbarHomepage?.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        binding?.toolbarHomepage?.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        if(binding?.drawerLayout!!.isDrawerOpen(GravityCompat.START)){
            binding?.drawerLayout!!.closeDrawer(GravityCompat.START)
        }else{
            binding?.drawerLayout!!.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(binding?.drawerLayout!!.isDrawerOpen(GravityCompat.START)){
            binding?.drawerLayout!!.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_sign_out ->{
                showDialog("Signing Out!!")
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                hideProgressDialog()
                startActivity(intent)
                finish()
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}