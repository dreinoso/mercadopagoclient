package com.chainreaction.mercadopagoclient.ui.main

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
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
            val paymentMethod = v.tag as PaymentGroup
            mListener?.onListFragmentInteraction(paymentMethod.type)
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
        holder.btnMethod.text = item.type.plus(" (").plus(item.count).plus(")")
        with(holder.btnMethod) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val btnMethod: AppCompatButton = mView.name

        override fun toString(): String {
            return super.toString() + " '" + btnMethod.text + "'"
        }
    }
}
