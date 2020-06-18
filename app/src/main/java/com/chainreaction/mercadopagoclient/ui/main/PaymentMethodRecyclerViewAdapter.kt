package com.chainreaction.mercadopagoclient.ui.main

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chainreaction.mercadopagoclient.R
import com.chainreaction.mercadopagoclient.model.PaymentGroup
import com.chainreaction.mercadopagoclient.model.PaymentMethod


import com.chainreaction.mercadopagoclient.ui.main.PaymentMethodFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_payment_method.view.*

class PaymentMethodRecyclerViewAdapter(
    private val allMethods: List<PaymentMethod>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<PaymentMethodRecyclerViewAdapter.ViewHolder>() {

    val TAG: String = "PaymentMethodRecyclerViewAdapter"
    private var mValues: List<PaymentGroup>

    private fun groupMethods(): List<PaymentGroup> {
        var methods: List<PaymentGroup> = mutableListOf()
        val groups : Map<String?, Int> = allMethods.groupingBy { it.paymentTypeId }.eachCount()
        groups.forEach({(k,v) -> methods += PaymentGroup(k, v)})
        return methods
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            mListener?.onListFragmentInteraction(v.name.text.toString())
        }

        mValues = groupMethods()
        Log.d(TAG.toString(), mValues.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_payment_method, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.type
        holder.mContentView.text = item.count.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.name
        val mContentView: TextView = mView.count

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
