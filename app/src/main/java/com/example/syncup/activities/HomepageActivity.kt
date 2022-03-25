package com.example.syncup.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.syncup.R
import com.example.syncup.databinding.ActivityHomepageBinding
import com.example.syncup.databinding.NavHeaderMainBinding
import com.example.syncup.firebase.FireStoreClass
import com.example.syncup.models.Users
import com.example.syncup.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class HomepageActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityHomepageBinding? = null
    private var headBinding: NavHeaderMainBinding? = null

    companion object{
        const val MY_PROFILE_REQUEST_CODE = 11
    }

    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
        binding?.navView?.setNavigationItemSelectedListener(this)

        binding?.floatingBtn?.setOnClickListener {
            val i = Intent(this,CreateBoardActivity::class.java)
            i.putExtra(Constants.NAME,mUserName)
            startActivity(i)

        }

        FireStoreClass().baseUserDetails(this)
    }

    fun updateNavigationUserDetails(user: Users){
        mUserName = user.name
        val headerView: View = binding?.navView!!.getHeaderView(0)
        headBinding = NavHeaderMainBinding.bind(headerView)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(headBinding?.navUserImage!!)
        headBinding?.tvUsername?.text = user.name
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FireStoreClass().baseUserDetails(this)
        }else{
            Log.e("Cancelled","Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                val intent = Intent(this,MyProfileActivity::class.java)
                startActivityForResult(intent, MY_PROFILE_REQUEST_CODE)
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