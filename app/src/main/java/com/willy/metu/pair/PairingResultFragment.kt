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
import com.willy.metu.util.Logger
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_pairing_result.*

class PairingResultFragment : Fragment(), CardStackListener{

    private val viewModel by viewModels<PairingResultViewModel> { getVmFactory() }

    private lateinit var layoutManager: CardStackLayoutManager
    private val adapter = PairingResultAdapter()
    private var count = 0
    val bg = layout_card_bg

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPairingResultBinding.inflate(inflater,container,false)
        val stackView= binding.stackView

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

        viewModel.allUsers.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
            adapter.submitList(it)
        })

        return binding.root
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {


    }

    override fun onCardSwiped(direction: Direction?) {

        val maxAmount = viewModel.allUsers.value?.size
        count++
        Logger.i(count.toString())
        Logger.i(maxAmount.toString())

        if(count == maxAmount) {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }

        if(direction == Direction.Left) {
            Toast.makeText(MeTuApplication.appContext,"Add To Wishlist",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(MeTuApplication.appContext,"Bye",Toast.LENGTH_SHORT).show()
        }


    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {

    }
}