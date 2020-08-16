package com.willy.metu.pair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentPairingResultBinding
import com.willy.metu.ext.excludeUser
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import com.yuyakaido.android.cardstackview.*

class PairingResultFragment : Fragment(), CardStackListener {

    private val viewModel by viewModels<PairingResultViewModel> {
        getVmFactory(
                PairingResultFragmentArgs.fromBundle(requireArguments()).selectedAnswers
        )
    }

    private lateinit var layoutManager: CardStackLayoutManager
    lateinit var binding: FragmentPairingResultBinding
    lateinit var adapter: PairingResultAdapter
    private var count = 0
    private val myEmail = UserManager.user.email

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPairingResultBinding.inflate(inflater, container, false)

        // Setup card stack recyclerview
        val stackView = binding.stackView
        adapter = PairingResultAdapter(viewModel)
        layoutManager = CardStackLayoutManager(requireContext(), this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
            setMaxDegree(20.0f)
            setStackFrom(StackFrom.Top)
        }
        stackView.layoutManager = layoutManager
        stackView.adapter = adapter
        stackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

        binding.buttonYes.setOnClickListener {

            setupSwipeAnimation(Direction.Left)
            binding.stackView.swipe()

        }

        binding.buttonNo.setOnClickListener {

            setupSwipeAnimation(Direction.Right)
            binding.stackView.swipe()

        }

        binding.buttonRewind.setOnClickListener {
            binding.stackView.rewind()
        }


        // Observers

        // Sort all users by selected traits
        viewModel.allUsers.observe(viewLifecycleOwner, Observer {
            viewModel.createSortedList(it)
        })

        // Exclude User before submit list
        viewModel.usersWithMatch.observe(viewLifecycleOwner, Observer {

            val sortedList = it.excludeUser()

            if (sortedList.isEmpty()) {
                matchValueVisibility(false)
            } else {
                matchValueVisibility(true)
            }
            adapter.submitList(sortedList)
        })

        viewModel.redBg.observe(viewLifecycleOwner, Observer {
            setupSkipAlpha(it)
        })

        viewModel.blueBg.observe(viewLifecycleOwner, Observer {
            setupFollowAlpha(it)
        })

        // Handling fetching data state with animation
        viewModel.status.observe(viewLifecycleOwner, Observer {
            setupSearchAnimation(it)
        })


        return binding.root
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

        when (direction) {
            Direction.Left -> viewModel.setRedBg(ratio)
            Direction.Right -> viewModel.setBlueBg(ratio)
            else -> {
                viewModel.setRedBg(0f)
                viewModel.setBlueBg(0f)
            }
        }
    }

    override fun onCardSwiped(direction: Direction?) {

        val maxAmount = viewModel.usersWithMatch.value?.size

        viewModel.setRedBg(0f)
        viewModel.setBlueBg(0f)

        setupSwipeAction(direction)

        // Add count on every swipe & when count reaches max amount of the list, navigate.
        count++
        if (count == maxAmount) {
            findNavController().navigate(NavigationDirections.navigateToFollowListFragment())
        }

        Logger.i(count.toString())
        Logger.i(maxAmount.toString())

    }

    override fun onCardCanceled() {

        viewModel.setRedBg(0f)
        viewModel.setBlueBg(0f)

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {
        count--
    }

    private fun setupSwipeAnimation(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
        layoutManager.setSwipeAnimationSetting(setting)
    }

    private fun setupSkipAlpha(alpha: Float) {
        binding.bgRed.alpha = alpha
        binding.textSkip.alpha = alpha
    }

    private fun setupFollowAlpha(alpha: Float) {
        binding.bgBlue.alpha = alpha
        binding.textLike.alpha = alpha
    }

    private fun setupSwipeAction(direction: Direction?) {
        if (direction == Direction.Right) {

            viewModel.postUserToFollow(myEmail, requireNotNull(viewModel.usersWithMatch.value)[count])

            Toast.makeText(MeTuApplication.appContext, getString(R.string.toast_add_follow), Toast.LENGTH_SHORT).show()
        }
    }

    private fun matchValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.layoutNoValue.visibility = View.GONE
        } else {
            binding.layoutNoValue.visibility = View.VISIBLE
            binding.noValueButton.setOnClickListener {
                findNavController().navigate(NavigationDirections.navigateToPairingFragment())
            }
        }
    }

    private fun setupSearchAnimation (status: LoadApiStatus) {
        when (status) {
            LoadApiStatus.LOADING -> {
                binding.layoutLoading.visibility = View.VISIBLE
                binding.animSearching.playAnimation()
            }
            LoadApiStatus.DONE -> {
                binding.layoutLoading.visibility = View.GONE
                binding.animSearching.cancelAnimation()
            }
            else -> Toast.makeText(context, "Something Terrible Happened", Toast.LENGTH_SHORT).show()
        }
    }
}