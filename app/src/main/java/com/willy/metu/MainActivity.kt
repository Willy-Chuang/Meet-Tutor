package com.willy.metu

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.willy.metu.databinding.ActivityMainBinding
import com.willy.metu.databinding.NavHeaderDrawerBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.CurrentFragmentType
import com.willy.metu.util.DrawerToggleType
import com.willy.metu.util.Logger
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    // get the height of status bar from system
    private val statusBarHeight: Int
        get() {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return when {
                resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
                else -> 0
            }
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // observe current fragment change, only for show info
        viewModel.currentFragmentType.observe(this, Observer {
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Logger.i("[${viewModel.currentFragmentType.value}]")
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        })

        setupToolbar()
        setupDrawer()
        setupNavController()
    }


    /**
     * Set up the layout of [Toolbar], according to whether it has cutout
     */
    private fun setupToolbar() {

        binding.toolbar.setPadding(0, statusBarHeight, 0, 0)

        launch {

            val dpi = resources.displayMetrics.densityDpi.toFloat()
            val dpiMultiple = dpi / DisplayMetrics.DENSITY_DEFAULT

            val cutoutHeight = getCutoutHeight()

            Logger.i("====== ${Build.MODEL} ======")
            Logger.i("$dpi dpi (${dpiMultiple}x)")
            Logger.i("statusBarHeight: ${statusBarHeight}px/${statusBarHeight / dpiMultiple}dp")

            when {
                cutoutHeight > 0 -> {
                    Logger.i("cutoutHeight: ${cutoutHeight}px/${cutoutHeight / dpiMultiple}dp")

                    val oriStatusBarHeight =
                        resources.getDimensionPixelSize(R.dimen.height_status_bar_origin)

                    binding.toolbar.setPadding(0, oriStatusBarHeight, 0, 0)
                    val layoutParams = Toolbar.LayoutParams(
                        Toolbar.LayoutParams.WRAP_CONTENT,
                        Toolbar.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.gravity = Gravity.CENTER
                    layoutParams.topMargin = statusBarHeight - oriStatusBarHeight
                    binding.textToolbarTitle.layoutParams = layoutParams
                }
            }
            Logger.i("====== ${Build.MODEL} ======")
        }
    }

    /**
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment, it better than another design
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView]
     */
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.calendarFragment -> CurrentFragmentType.CALENDAR
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

//                when (UserManager.isLoggedIn) { // check user login status when open drawer
//                    true -> {
//                        viewModel.checkUser()
//                    }
//                    else -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
//                        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                            binding.drawerLayout.closeDrawer(GravityCompat.START)
//                        }
//                    }
//                }

            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        // Set up header of drawer ui using data binding
        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false
        )

        bindingNavHeader.lifecycleOwner = this
        bindingNavHeader.viewModel = viewModel
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)

        // Observe current drawer toggle to set the navigation icon and behavior
        viewModel.currentDrawerToggleType.observe(this, Observer { type ->

            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
            binding.toolbar.setNavigationIcon(
                when (type) {
                    DrawerToggleType.BACK -> R.drawable.toolbar_back
                    else -> R.drawable.toolbar_menu
                }
            )
            actionBarDrawerToggle?.setToolbarNavigationClickListener {
                when (type) {
                    DrawerToggleType.BACK -> onBackPressed()
                    else -> {
                    }
                }
            }
        })
    }

    /**
     * override back key for the drawer design
     */
    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

