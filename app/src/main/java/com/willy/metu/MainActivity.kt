package com.willy.metu

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
import java.util.*


class MainActivity() : BaseActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    //Setup bottom navigation destination
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
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
        when (item.itemId) {
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

    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.drawerNavView.setNavigationItemSelectedListener(onDrawerItemSelectedListener)

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

        viewModel.navigateToPairingByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_pairing
                viewModel.onPairingNavigated()
            }
        })

        viewModel.setupUser(UserManager.user)

        setupDrawer()
        setupBottomNav()
        setupNavController()
    }

    // Setup side menu for an icon of calendar

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        // If the current fragment is -- ,calendar button won't inflate
        viewModel.currentFragmentType.observe(this, Observer { type ->
            type?.let {
                menu.findItem(R.id.talent_pool).isVisible = when (it) {
                    CurrentFragmentType.TALENTPOOL -> true
                    else -> false
                }
                menu.findItem(R.id.calendarFragment).isVisible = when (it) {
                    CurrentFragmentType.CALENDAR,
                    CurrentFragmentType.PROFILE,
                    CurrentFragmentType.EDITPROFILE -> false
                    else -> true
                }
                val profileModeMenu = menu.findItem(R.id.profile_mode)
                profileModeMenu.isVisible = when (it) {
                    CurrentFragmentType.PROFILE -> {
                        profileModeMenu.title = "EDIT"
                        true
                    }
                    CurrentFragmentType.EDITPROFILE -> {
                        profileModeMenu.title = "SAVE"
                        true
                    }
                    else -> false
                }
                viewModel.saveIsPressed.value = profileModeMenu.isVisible
            }
        })

        return viewModel.currentFragmentType.value != CurrentFragmentType.CALENDAR
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.talent_pool -> findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToPostArticleDialog())
            R.id.calendarFragment -> findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCalendarFragment())
            R.id.profile_mode -> {
                when (item.title.toString().toLowerCase(Locale.ROOT)) {
                    "edit" -> viewModel.editIsPressed.value = true
                    "save" -> viewModel.saveIsPressed.value = true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView]
     */
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {

                // Setup BottomNav selected as well as fragment title

                R.id.startPairingFragment -> {
                    val pairing = binding.bottomNavView.menu.findItem(R.id.navigation_pairing)
                    pairing.isChecked = true
                    CurrentFragmentType.PAIR
                }
                R.id.homeFragment -> {
                    val home = binding.bottomNavView.menu.findItem(R.id.navigation_home)
                    home.isChecked = true
                    CurrentFragmentType.HOME
                }
                R.id.chatListFragment -> {
                    val chatList = binding.bottomNavView.menu.findItem(R.id.navigation_chat_list)
                    chatList.isChecked = true
                    CurrentFragmentType.CHATLIST
                }
                R.id.talentPoolFragment -> {
                    val talentPool = binding.bottomNavView.menu.findItem(R.id.navigation_talent_pool)
                    talentPool.isChecked = true
                    CurrentFragmentType.TALENTPOOL
                }
                R.id.notifyFragment -> {
                    val notify = binding.bottomNavView.menu.findItem(R.id.navigation_notification)
                    notify.isChecked = true
                    CurrentFragmentType.NOTIFY
                }

                // Setup fragment title

                R.id.calendarFragment -> CurrentFragmentType.CALENDAR
                R.id.questionnaireOneFragment -> CurrentFragmentType.PAIRONE
                R.id.questionnaireTwoFragment -> CurrentFragmentType.PAIRTWO
                R.id.questionnaireThreeFragment -> CurrentFragmentType.PAIRTHREE
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.editProfileFragment -> CurrentFragmentType.EDITPROFILE
                R.id.followListFragment -> CurrentFragmentType.FOLLOW
                R.id.userDetailFragment -> CurrentFragmentType.USERPROFILE
                R.id.chatRoomFragment -> CurrentFragmentType.CHAT
                R.id.post_article_dialog -> CurrentFragmentType.POSTARTICLE
                R.id.eventDetailFragment -> CurrentFragmentType.EVENTDETAIL
                R.id.newChatFragment -> CurrentFragmentType.NEWCHAT
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    private fun setupBottomNav() {
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
            val manager = supportFragmentManager
            val count = manager.findFragmentById(R.id.myNavHostFragment)!!.childFragmentManager.backStackEntryCount
            if (count == 0) {
                setDialog()
            } else {
                super.onBackPressed()
            }

        }
    }


    private fun setDialog() {

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setTitle("Sure To Leave?")
        alertDialogBuilder.setMessage("Leaving will close the app")
        alertDialogBuilder.setPositiveButton("Sure", DialogInterface.OnClickListener { _, _ -> finish()})
        alertDialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { which, _ -> which.dismiss() })
        alertDialogBuilder.show()

    }
}

