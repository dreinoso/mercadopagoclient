package com.chainreaction.mercadopagoclient.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import com.chainreaction.mercadopagoclient.R
import com.chainreaction.mercadopagoclient.model.PaymentMethod


import com.chainreaction.mercadopagoclient.ui.main.PaymentBankFragment.OnListFragmentInteractionListener
import com.chainreaction.mercadopagoclient.ui.main.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_payment_bank.view.*

import kotlinx.android.synthetic.main.fragment_payment_method.view.*
import kotlinx.android.synthetic.main.fragment_payment_method.view.name

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
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
            .inflate(R.layout.fragment_payment_bank, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.name.text = item.name
        if (!item.status?.equals("active")!!) {
            holder.active.visibility = View.GONE
        }
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val name: TextView = mView.name
        val active: AppCompatImageView = mView.active
    }
}