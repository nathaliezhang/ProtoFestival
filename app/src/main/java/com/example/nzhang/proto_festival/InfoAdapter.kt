package com.example.nzhang.proto_festival

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.DecelerateInterpolator


class InfoAdapter(private val activity: Activity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_PRICE: Int = 0
    private val TYPE_ABOUT: Int = 1
    private val TYPE_PARTNER: Int = 2
    private var mExpandedPosition: Int = -1
    private var previousExpandedPosition: Int = -1
    val animation = RotateAnimation(0f, 90.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)


    override fun getItemCount(): Int = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)

        if (viewType == TYPE_PRICE) {
            val view = layoutInflater.inflate(R.layout.fragment_info_price, parent, false)
            return InfoAdapter.PriceViewHolder(view)
        } else if (viewType == TYPE_ABOUT) {
            val view = layoutInflater.inflate(R.layout.fragment_info_about, parent, false)
            return InfoAdapter.AboutViewHolder(view)
        } else if (viewType == TYPE_PARTNER) {
            val view = layoutInflater.inflate(R.layout.fragment_info_partner, parent, false)
            return InfoAdapter.PartnerViewHolder(view)
        }
        throw RuntimeException("Not match type for" + viewType + ".")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val isExpanded = position == mExpandedPosition
        val context = holder?.itemView?.context
        val lightFont: Typeface = Typeface.createFromAsset(context?.assets, "fonts/Raleway-Light.ttf")
        val semiBoldFont: Typeface = Typeface.createFromAsset(context?.assets, "fonts/Raleway-SemiBold.ttf")

        if (holder is PriceViewHolder){
            holder.details.visibility = (if (isExpanded) View.VISIBLE else View.GONE)
            holder.itemView.isActivated = isExpanded
            holder.title.typeface = lightFont

            if (isExpanded) {
                holder.icon.setImageResource(R.drawable.picto_tarifs_cliquey)
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.repeatCount = 0
                animation.fillAfter = true
                animation.duration = 600
                holder.arrow.startAnimation(animation)
                previousExpandedPosition = position
            } else {
                holder.icon.setImageResource(R.drawable.picto_tarifs)
            }

            holder.itemView.setOnClickListener {
                mExpandedPosition = if (isExpanded) -1 else position
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }
        }
        if (holder is AboutViewHolder){
            holder.details.visibility = (if (isExpanded) View.VISIBLE else View.GONE)
            holder.itemView.isActivated = isExpanded
            holder.title.typeface = lightFont
            holder.linkAFCA.typeface = semiBoldFont
            holder.linkFNFA.typeface = semiBoldFont

            if (isExpanded) {
                holder.icon.setImageResource(R.drawable.picto_festival_cliquey)
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.repeatCount = 0
                animation.fillAfter = true
                animation.duration = 600
                holder.arrow.startAnimation(animation)
                previousExpandedPosition = position
            } else {
                holder.icon.setImageResource(R.drawable.picto_festival)
            }

            holder.linkFNFA.setOnClickListener {
                browserAboutPage("FNFA")
            }

            holder.linkAFCA.setOnClickListener {
                browserAboutPage("AFCA")
            }

            holder.itemView.setOnClickListener {
                mExpandedPosition = if (isExpanded) -1 else position
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }
        }
        if (holder is PartnerViewHolder){
            holder.details.visibility = (if (isExpanded) View.VISIBLE else View.GONE)
            holder.itemView.isActivated = isExpanded
            holder.title.typeface = lightFont

            if (isExpanded) {
                holder.icon.setImageResource(R.drawable.picto_partenaires_cliquey)
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.repeatCount = 0
                animation.fillAfter = true
                animation.duration = 600
                holder.arrow.startAnimation(animation)
                previousExpandedPosition = position
            } else {
                holder.icon.setImageResource(R.drawable.picto_partenaires)
            }

            holder.itemView.setOnClickListener {
                mExpandedPosition = if (isExpanded) -1 else position
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> TYPE_PRICE
            1 -> TYPE_ABOUT
            2 -> TYPE_PARTNER
            else -> -1
        }
    }

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val group = view.findViewById<ConstraintLayout>(R.id.item_group_price)
        val details = view.findViewById<ConstraintLayout>(R.id.item_details_price)
        val title = view.findViewById<TextView>(R.id.info_price_button)
        val icon = view.findViewById<ImageView>(R.id.info_price_image)
        val arrow = view.findViewById<ImageView>(R.id.info_price_button_arrow)
    }

    class AboutViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val group = view.findViewById<ConstraintLayout>(R.id.item_group_about)
        val details = view.findViewById<ConstraintLayout>(R.id.item_details_about)
        var title = view.findViewById<TextView>(R.id.info_about_button)
        val linkFNFA = view.findViewById<TextView>(R.id.info_about_link_FNFA)
        val linkAFCA = view.findViewById<TextView>(R.id.info_about_link_AFCA)
        val icon = view.findViewById<ImageView>(R.id.info_about_image)
        val arrow = view.findViewById<ImageView>(R.id.info_about_button_arrow)
        val places = view.findViewById<TextView>(R.id.info_about_places)
    }

    class PartnerViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val group = view.findViewById<ConstraintLayout>(R.id.item_group_partner)
        val details = view.findViewById<ConstraintLayout>(R.id.item_details_partner)
        val title = view.findViewById<TextView>(R.id.info_partners_button)
        val icon = view.findViewById<ImageView>(R.id.info_partners_image)
        val arrow = view.findViewById<ImageView>(R.id.info_partners_button_arrow)
    }

    private fun browserAboutPage(type: String) {
        if (type == "FNFA") {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://festival-film-animation.fr/qui-sommes-nous.html"))
            activity.startActivity(browserIntent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.afca.asso.fr/"))
            activity.startActivity(browserIntent)
        }

    }
}
