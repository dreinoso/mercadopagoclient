package com.chainreaction.mercadopagoclient.util

import android.R
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.chainreaction.mercadopagoclient.model.ExceptionByCardIssuer

class IssuerAdapter(
    activity: Activity,
    private val mData: List<ExceptionByCardIssuer>,
    amount: Double
) : BaseAdapter() {
    private val mAmount: Double
    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Any {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View,
        parent: ViewGroup
    ): View {
        var vi = convertView
        if (convertView == null) vi =
            mInflater!!.inflate(R.layout.simple_list_item_1, null)
        val label =
            vi.findViewById<View>(R.id.text1) as TextView // label
        val exceptionByCardIssuer = mData[position]
        label.text = exceptionByCardIssuer.cardIssuer!!.name
        return vi
    }

    companion object {
        private var mInflater: LayoutInflater? = null
    }

    init {
        mInflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mAmount = amount
    }
}