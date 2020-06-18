package com.chainreaction.mercadopagoclient.ui.banks

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.chainreaction.mercadopagoclient.R.*
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import kotlinx.android.synthetic.main.fragment_payment_bank.view.*

class BankRecyclerViewAdapter(
    private val allMethods: List<PaymentMethod>,
    private val mListener: PaymentBankFragment.OnListFragmentInteractionListener?,
    private val methodTypeFilter: String
    ) : RecyclerView.Adapter<BankRecyclerViewAdapter.ViewHolder>() {

    val TAG: String = "PaymentMethodRecyclerViewAdapter"
    private var mValues: List<PaymentMethod>

    private val mOnClickListener: View.OnClickListener

    init {
        mValues = allMethods.filter {it.paymentTypeId.equals(methodTypeFilter)}
        mOnClickListener = View.OnClickListener { v ->
            val paymentMethod = v.tag as PaymentMethod
            mListener?.onListFragmentInteraction(paymentMethod.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.fragment_payment_bank, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.btnBank.text = item.name
        if (item.status?.equals("active")!!) {
            holder.btnBank.setBackgroundResource(drawable.ic_round_btn_active)
        } else {
            holder.btnBank.setBackgroundResource(drawable.ic_round_btn_off)
        }
        with(holder.btnBank) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        val btnBank: AppCompatButton = mView.btn_bank
    }
}