package com.chainreaction.mercadopagoclient.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.chainreaction.mercadopagoclient.databinding.FragmentPaymentBankListBinding
import com.chainreaction.mercadopagoclient.model.PaymentMethod

import com.chainreaction.mercadopagoclient.ui.main.dummy.DummyContent.DummyItem

class PaymentBankFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentPaymentBankListBinding? = null
    private val binding get() = _binding!!
    val TAG = "PaymentBankFragment"

    private val listener = object: PaymentBankFragment.OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(paymentId: String?) {
            Log.d(TAG, paymentId.toString())
            paymentId!!
//            view?.findNavController()?.navigate(PaymentMethodFragmentDirections.
//            actionPaymentMethodFragmentToPaymentBankFragment(paymentId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBankListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this!!.requireActivity()).get(MainViewModel::class.java)
        val methodType = arguments?.get("paymentType").toString()
        val adapter = BankRecyclerViewAdapter(viewModel.paymentMethodsLiveData.value as List<PaymentMethod>, listener, methodType)
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(paymentId: String?)
    }
}
