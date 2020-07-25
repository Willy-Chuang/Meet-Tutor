package com.willy.metu.pair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortByTraits
import com.willy.metu.login.UserManager
import com.willy.metu.util.Logger
import com.yuyakaido.android.cardstackview.*

class PairingResultFragment : Fragment(), CardStackListener{

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
        val binding = FragmentPairingResultBinding.inflate(inflater,container,false)
        val stackView= binding.stackView
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

        Logger.w(viewModel.previousAnswers.toString())


        viewModel.allUsers.observe(viewLifecycleOwner, Observer {

            viewModel.usersWithMatch.value = it.sortByTraits(viewModel.previousAnswers)

        })

        viewModel.usersWithMatch.observe(viewLifecycleOwner, Observer {
            Logger.w(it.toString())
            adapter.submitList(it)
        })

        viewModel.swiped.observe(viewLifecycleOwner, Observer {
            Logger.v(it.toString() + "changed")
        })

        viewModel.redBg.observe(viewLifecycleOwner, Observer {
            binding.bgRed.alpha = it
        })

        return binding.root
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

        Logger.w("ratio = $ratio")

        if(direction == Direction.Right) {
            viewModel.redBg.value = ratio
        }


    }

    override fun onCardSwiped(direction: Direction?) {

        val maxAmount = viewModel.usersWithMatch.value?.size

        Logger.i(count.toString())
        Logger.i(maxAmount.toString())
        viewModel.redBg.value = 0f

        if(direction == Direction.Left) {

            viewModel.swiped.value = true
            viewModel.postUserToFollow(myEmail, requireNotNull(viewModel.usersWithMatch.value)[count])

            Toast.makeText(MeTuApplication.appContext,"Add To Wishlist",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(MeTuApplication.appContext,"Bye",Toast.LENGTH_SHORT).show()
        }

        count++
        if(count == maxAmount) {
            findNavController().navigate(NavigationDirections.navigateToFollowListFragment())
        }


    }

    override fun onCardCanceled() {

        viewModel.redBg.value = 0f

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {

    }
}