package com.chainreaction.mercadopagoclient.ui.home

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.chainreaction.mercadopagoclient.R
import com.chainreaction.mercadopagoclient.databinding.FragmentAmountBinding
import com.chainreaction.mercadopagoclient.ui.main.MainViewModel
import com.google.android.material.snackbar.Snackbar
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

        viewModel = ViewModelProviders.of(this!!.requireActivity()).get(MainViewModel::class.java)

        binding.continueBtn.setOnClickListener { view ->
            if (binding.amount.text.toString().isEmpty() || binding.amount.text.toString() == "$0.00") {
                val snack = Snackbar.make(view,getString(R.string.amount_error),Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                hideKeyboard()
                view.findNavController()
                    .navigate(AmountFragmentDirections.actionAmountFragmentToPaymentMethodFragment())
            }
        }
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AmountFragment()
    }
}

