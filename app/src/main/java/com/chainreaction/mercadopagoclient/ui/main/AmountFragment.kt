package com.chainreaction.mercadopagoclient.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels

import com.chainreaction.mercadopagoclient.databinding.FragmentAmountBinding
import java.text.NumberFormat

class AmountFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentAmountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAmountBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.amount.addTextChangedListener(
            object : TextWatcher {
                var current = "";

                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!text.toString().equals(current)) {
                        binding.amount.removeTextChangedListener(this);
                        var cleanString = text.toString().replace("[$,.]".toRegex(), "");
                        var parsed = cleanString.toDouble();
                        var formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                        current = formatted;
                        binding.amount.setText(formatted)
                        binding.amount.setSelection(formatted.length);
                        binding.amount.addTextChangedListener(this);

                        viewModel.amountFormattedLiveData.value = formatted
                    }
                }
            }
        )

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.continueBtn.setOnClickListener { view ->
            view.findNavController().navigate(AmountFragmentDirections.actionAmountFragmentToPaymentMethodFragment())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AmountFragment()
    }
}

