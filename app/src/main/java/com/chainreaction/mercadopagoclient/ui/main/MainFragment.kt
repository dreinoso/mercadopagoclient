package com.chainreaction.mercadopagoclient.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.chainreaction.mercadopagoclient.MainActivity
import com.chainreaction.mercadopagoclient.Mercadopago
import com.chainreaction.mercadopagoclient.databinding.MainFragmentBinding
import com.chainreaction.mercadopagoclient.model.Card
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import com.chainreaction.mercadopagoclient.model.Token
import com.chainreaction.mercadopagoclient.util.InstallmentAdapter
import com.chainreaction.mercadopagoclient.util.RetrofitUtil
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mp = context?.let { Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa", it) }
        setInputs()
        //Get payment methods and show installments spinner
        handleInstallments()
    }

    private var cardNumber: EditText? = null
    private var month: EditText? = null
    private var year: EditText? = null
    private var securityCode: EditText? = null
    private var name: EditText? = null
    private var docType: EditText? = null
    private var docNumber: EditText? = null
    private var mp: Mercadopago? = null
    var dialog: ProgressDialog? = null

    fun submitForm(view: View?) {
        //Create card object with card data
        val card = Card(
            getCardNumber(),
            getMonth(),
            getYear(),
            getSecurityCode(),
            getName(),
            getDocType(),
            getDocNumber()
        )

        //Callback handler
        val callback: Callback<*> = object : Callback<Token> {
            override fun success(o: Token, response: Response) {
                dialog!!.dismiss()
                Toast.makeText(context,
                    o.id,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun failure(error: RetrofitError) {
                dialog!!.dismiss()
                Toast.makeText(
                    context,
                    RetrofitUtil.parseErrorBody(error).toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //Check valid card data
        if (card.validateCard()) {
            //Send card data to get token id
            dialog = ProgressDialog.show(
                context,
                "",
                "Loading. Please wait...",
                true
            )
            mp?.createToken(card, callback)
        } else {
            Toast.makeText(context, "Invalid data", Toast.LENGTH_LONG).show()
        }
    }

    fun handleInstallments() {
        cardNumber!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.length == 4) {
                    cardNumber!!.append(" ")
                }
                if (s.length == 6) {
                    val cb: Callback<*> =
                        object : Callback<List<PaymentMethod>> {
                            override fun success(
                                o: List<PaymentMethod>,
                                response: Response
                            ) {
                                val adapter = o[0].getPayerCosts()?.let {
                                    InstallmentAdapter(
                                        activity as MainActivity,
                                        it,
                                        100.0
                                    )
                                }
                                binding.spinner.adapter = adapter
                            }

                            override fun failure(error: RetrofitError) {
                                Toast.makeText(
                                    context,
                                    RetrofitUtil.parseErrorBody(error).toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    mp?.getPaymentMethodByBin(getCardNumber().substring(0, 6),
                        cb as Callback<MutableList<PaymentMethod>>?
                    )
                }
            }
        })
    }

    fun getCardNumber(): String {
        return cardNumber!!.text.toString()
    }

    fun getMonth(): Int {
        return getInteger(month)
    }

    fun getYear(): Int {
        return getInteger(year)
    }

    fun getSecurityCode(): String? {
        return securityCode!!.text.toString()
    }

    fun getName(): String? {
        return name!!.text.toString()
    }

    fun getDocType(): String? {
        return docType!!.text.toString().toUpperCase()
    }

    fun getDocNumber(): String? {
        return docNumber!!.text.toString()
    }

    private fun getInteger(text: EditText?): Int {
        return try {
            text!!.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    //Handle inputs
    fun setInputs() {
        cardNumber = _binding?.addCardFormCardNumber
        month = _binding?.addCardFormMonth
        year = _binding?.addCardFormYear
        securityCode = _binding?.addCardFormSecurityCode
        name = _binding?.addCardFormFullName
        docType = _binding?.addCardFormDocumentType
        docNumber = _binding?.addCardFormDocumentNumber
        binding.addCardFormContinue.setOnClickListener(this::submitForm)
    }
}
