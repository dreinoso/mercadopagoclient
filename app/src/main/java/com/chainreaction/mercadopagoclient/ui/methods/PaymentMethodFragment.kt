package com.chainreaction.mercadopagoclient.ui.methods

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chainreaction.mercadopagoclient.databinding.FragmentPaymentMethodListBinding
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import com.chainreaction.mercadopagoclient.ui.main.MainViewModel

class PaymentMethodFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentPaymentMethodListBinding? = null
    private val binding get() = _binding!!
    private val TAG = "paymentMethodFragment"

    private val listener = object:
        OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(methodType: String?) {
            Log.d(TAG, methodType.toString())
            methodType!!
            view?.findNavController()?.navigate(
                PaymentMethodFragmentDirections.actionPaymentMethodFragmentToPaymentBankFragment(
                    methodType
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentMethodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this!!.requireActivity()).get(MainViewModel::class.java)
        viewModel.paymentMethodsLiveData.observe(viewLifecycleOwner, Observer {
                methods ->
            val adapter =
                PaymentMethodRecyclerViewAdapter(
                    methods as List<PaymentMethod>,
                    listener
                )
            binding.list.adapter = adapter
            binding.list.layoutManager = LinearLayoutManager(context)
            adapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        })
        viewModel.getPaymentMethods()
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(methodType: String?)
    }
}
