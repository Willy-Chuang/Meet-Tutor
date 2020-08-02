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
import com.willy.metu.databinding.FragmentPairingResultBinding
import com.willy.metu.ext.excludeUser
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortByTraits
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
    lateinit var adapter: PairingResultAdapter
    private var count = 0
    val myEmail = UserManager.user.email

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPairingResultBinding.inflate(inflater, container, false)
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
            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            layoutManager.setSwipeAnimationSetting(setting)
            binding.stackView.swipe()
        }

        binding.buttonNo.setOnClickListener {

            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            layoutManager.setSwipeAnimationSetting(setting)
            binding.stackView.swipe()

        }

        binding.buttonRewind.setOnClickListener {
            binding.stackView.rewind()
        }



        // Observers

        // Sort all users by selected traits
        viewModel.allUsers.observe(viewLifecycleOwner, Observer {

            viewModel.usersWithMatch.value = it.sortByTraits(viewModel.previousAnswers)

        })

        // Exclude User before submit list
        viewModel.usersWithMatch.observe(viewLifecycleOwner, Observer {
            Logger.w(it.toString())
            val sortedList = it.excludeUser()
            if (sortedList.isEmpty()) {
                binding.layoutNoValue.visibility = View.VISIBLE
                binding.noValueButton.setOnClickListener {
                    findNavController().navigate(NavigationDirections.navigateToPairingFragment())
                }
            } else {
                binding.layoutNoValue.visibility = View.GONE
            }
            adapter.submitList(it.excludeUser())
        })

        viewModel.swiped.observe(viewLifecycleOwner, Observer {
            Logger.v(it.toString() + "changed")
        })

        viewModel.redBg.observe(viewLifecycleOwner, Observer {
            binding.bgRed.alpha = it
            binding.textSkip.alpha = it
        })

        viewModel.blueBg.observe(viewLifecycleOwner, Observer {
            binding.bgBlue.alpha = it
            binding.textLike.alpha = it
        })

        // Handling fetching data state with animation
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
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
        })


        return binding.root
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

        Logger.w("ratio = $ratio")

        when (direction) {
            Direction.Left -> viewModel.redBg.value = ratio
            Direction.Right -> viewModel.blueBg.value = ratio
            else -> {
                viewModel.blueBg.value = 0f
                viewModel.redBg.value = 0f
            }
        }
    }

    override fun onCardSwiped(direction: Direction?) {

        val maxAmount = viewModel.usersWithMatch.value?.size

        Logger.i(count.toString())
        Logger.i(maxAmount.toString())
        viewModel.redBg.value = 0f
        viewModel.blueBg.value = 0f

        if (direction == Direction.Right) {

            viewModel.swiped.value = true
            viewModel.postUserToFollow(myEmail, requireNotNull(viewModel.usersWithMatch.value)[count])

            Toast.makeText(MeTuApplication.appContext, "Add To Follow List", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(MeTuApplication.appContext, "Bye", Toast.LENGTH_SHORT).show()
        }

        count++
        if (count == maxAmount) {
            findNavController().navigate(NavigationDirections.navigateToFollowListFragment())
        }


    }

    override fun onCardCanceled() {

        viewModel.redBg.value = 0f
        viewModel.blueBg.value = 0f

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {
        Logger.w("rewind")
        count--

    }
}