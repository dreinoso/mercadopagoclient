package com.chainreaction.mercadopagoclient.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chainreaction.mercadopagoclient.R
import com.chainreaction.mercadopagoclient.model.Entity
import kotlinx.android.synthetic.main.fragment_installment.view.*

class InstallmentRecyclerViewAdapter(
    private val allInstallments: List<Entity.Installment>
) : RecyclerView.Adapter<InstallmentRecyclerViewAdapter.ViewHolder>() {

    private lateinit var mValues: List<Entity.PlayerCosts>

    init {
        mValues  = allInstallments.get(0).payer_costs!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_installment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.info.text = item.recommended_message
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val info: TextView = mView.info

        override fun toString(): String {
//            return super.toString() + " '" + mContentView.text + "'"
            return ""
        }
    }
}
