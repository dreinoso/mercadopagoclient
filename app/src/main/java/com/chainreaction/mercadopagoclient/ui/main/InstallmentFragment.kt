package com.chainreaction.mercadopagoclient.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.chainreaction.mercadopagoclient.databinding.FragmentInstallmnentListBinding
import com.chainreaction.mercadopagoclient.model.Entity

class InstallmentFragment : Fragment() {

    private val TAG = "InstallmentFragment"
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentInstallmnentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstallmnentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this!!.requireActivity()).get(MainViewModel::class.java)
        viewModel.installmentsLiveData.observe(viewLifecycleOwner, Observer { installments ->
            Log.d(TAG, installments.toString())
            val adapter = InstallmentRecyclerViewAdapter(installments as List<Entity.Installment>)
            binding.list.adapter = adapter
            binding.list.layoutManager = LinearLayoutManager(context)
            adapter.notifyDataSetChanged()
        })
        binding.btnDone.setOnClickListener { resetFlow() }
        viewModel.getInstallments("cencosud")
    }

    fun resetFlow() {
        viewModel.amountFormattedLiveData.value = null
        view?.findNavController()?.navigate(InstallmentFragmentDirections.actionInstallmnentFragmentToAmountFragment())
    }
}
