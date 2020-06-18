package com.chainreaction.mercadopagoclient.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chainreaction.mercadopagoclient.databinding.FragmentInstallmnentListBinding
import com.chainreaction.mercadopagoclient.model.Entity


class InstallmentFragment : Fragment() {

    private lateinit var adapter: InstallmentRecyclerViewAdapter
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
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, installments.toString())
            if (installments.isNullOrEmpty()) {
                showFailureDialog()
            } else {
                adapter = InstallmentRecyclerViewAdapter(installments as List<Entity.Installment>)
                binding.list.adapter = adapter
                binding.list.layoutManager = LinearLayoutManager(context)
                val dividerItemDecoration = DividerItemDecoration(
                    binding.list.context,
                    (binding.list.layoutManager as LinearLayoutManager).orientation
                )
                binding.list.addItemDecoration(dividerItemDecoration)
                adapter.notifyDataSetChanged()
                showSuccessfulDialog()
            }
        })
        binding.btnDone.setOnClickListener { resetFlow() }
        val paymentMethodId = arguments?.get("paymentMethodId").toString()
        viewModel.getInstallments(paymentMethodId)
    }

    fun resetFlow() {
        viewModel.amountFormattedLiveData.value = null
        adapter.clear()
        view?.findNavController()?.navigate(InstallmentFragmentDirections.actionInstallmnentFragmentToAmountFragment())
    }

    fun showSuccessfulDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(com.chainreaction.mercadopagoclient.R.string.dialog_success_title)
                setPositiveButton(
                    com.chainreaction.mercadopagoclient.R.string.btn_ok
                ) { _, _ -> }
            }
            builder.create()
        }?.show()
    }

    fun showFailureDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(com.chainreaction.mercadopagoclient.R.string.dialog_failure_title)
                setMessage(com.chainreaction.mercadopagoclient.R.string.dialog_failure_message)
                setPositiveButton(
                    com.chainreaction.mercadopagoclient.R.string.btn_start_again
                ) {dialog, id -> resetFlow() }
        }
            builder.create()
        }?.show()
    }

}
