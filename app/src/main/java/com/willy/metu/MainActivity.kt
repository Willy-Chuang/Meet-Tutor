package com.willy.metu

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.willy.metu.databinding.ActivityMainBinding
import com.willy.metu.databinding.BadgeBottomBinding
import com.willy.metu.databinding.NavHeaderDrawerBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.util.CurrentFragmentType
import com.willy.metu.util.DrawerToggleType
import com.willy.metu.util.Logger
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    //Setup bottom navigation destination
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when (item.itemId){
            R.id.navigation_home -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pairing -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToPairingFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat_list -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToChatList())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_talent_pool -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToTalentPool())
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_notification -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToNotify())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //Setup drawer navigation destination
    private val onDrawerItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.profileFragment -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToProfile())
                return@OnNavigationItemSelectedListener true
            }
            R.id.followListFragment -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToFollowListFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }


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
        binding.drawerNavView.setNavigationItemSelectedListener(onDrawerItemSelectedListener)

        binding.buttonToolbarEdit.setOnClickListener {
            viewModel.editIsPressed.value = true
        }
        binding.buttonToolbarSave.setOnClickListener {
            viewModel.saveIsPressed.value = true
        }


        // observe current fragment change, only for show info
        viewModel.currentFragmentType.observe(this, Observer {
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Logger.i("[${viewModel.currentFragmentType.value}]")
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        })

        viewModel.navigateToHomeByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_home
                viewModel.onHomeNavigated()
            }
        })

        viewModel.navigateToNotificationByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_notification
                viewModel.onNotifyNavigated()
            }
        })

        viewModel.navigateToTalentPoolByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_talent_pool
                viewModel.onTalentPoolNavigated()
            }
        })



        viewModel.navigateToChatListByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_chat_list
                viewModel.onChatNavigated()
            }
        })

        viewModel.navigateToTalentPoolByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_talent_pool
                viewModel.onTalentPoolNavigated()
            }
        })

        viewModel.setupUser(UserManager.user)

        setupToolbar()
        setupDrawer()
        setupBottomNav()
        setupNavController()
    }

    // Setup side menu for an icon of calendar

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        // If the current fragment is -- ,calendar button won't inflate
        viewModel.currentFragmentType.observe(this, Observer{ type ->
            type?.let {

                when (it) {
                    CurrentFragmentType.CALENDAR -> menu.findItem(R.id.calendarFragment).isVisible = false
                    CurrentFragmentType.PROFILE -> menu.findItem(R.id.calendarFragment).isVisible = false
                    CurrentFragmentType.EDITPROFILE -> menu.findItem(R.id.calendarFragment).isVisible = false
                    else ->  menu.findItem(R.id.calendarFragment).isVisible = true
                }
            }

        })

        viewModel.currentFragmentType.observe(this, Observer {type ->
            type?.let {
                when(it) {
                    CurrentFragmentType.TALENTPOOL -> menu.findItem(R.id.talent_pool).isVisible = true
                    else -> menu.findItem(R.id.talent_pool).isVisible = false
                }
            }
        })
        return viewModel.currentFragmentType.value != CurrentFragmentType.CALENDAR
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {
            R.id.talent_pool -> findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToPostArticleDialog())
            R.id.calendarFragment -> findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCalendarFragment())
        }

//            findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCalendarFragment())


        return super.onOptionsItemSelected(item)
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
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView]
     */
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.calendarFragment -> CurrentFragmentType.CALENDAR
                R.id.startPairingFragment -> CurrentFragmentType.PAIR
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.questionnaireOneFragment -> CurrentFragmentType.PAIRONE
                R.id.questionnaireTwoFragment -> CurrentFragmentType.PAIRTWO
                R.id.questionnaireThreeFragment -> CurrentFragmentType.PAIRTHREE
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.editProfileFragment -> CurrentFragmentType.EDITPROFILE
                R.id.followListFragment -> CurrentFragmentType.FOLLOW
                R.id.userDetailFragment -> CurrentFragmentType.USERPROFILE
                R.id.chatListFragment -> CurrentFragmentType.CHATLIST
                R.id.chatRoomFragment -> CurrentFragmentType.CHAT
                R.id.talentPoolFragment -> CurrentFragmentType.TALENTPOOL
                R.id.post_article_dialog -> CurrentFragmentType.POSTARTICLE
                R.id.notifyFragment -> CurrentFragmentType.NOTIFY
                R.id.eventDetailFragment -> CurrentFragmentType.EVENTDETAIL
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    private fun setupBottomNav(){
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(4) as BottomNavigationItemView
        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
        bindingBadge.lifecycleOwner = this
        bindingBadge.viewModel = viewModel
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
            R.string.navigation_drawer_close
        ) {
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
                    DrawerToggleType.BACK -> {

                        when (viewModel.currentFragmentType.value) {
                            CurrentFragmentType.PROFILE -> findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToHomeFragment())
                            CurrentFragmentType.FOLLOW -> findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToHomeFragment())
                            else -> onBackPressed()
                        }

                    }
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

