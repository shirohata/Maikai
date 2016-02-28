package com.github.shirohata.maikai.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.shirohata.maikai.App
import com.github.shirohata.maikai.R
import com.github.shirohata.maikai.model.Catalog
import com.github.shirohata.maikai.network.getThumbUri
import com.jakewharton.rxbinding.widget.RxAdapterView
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by hirohata on 2016/02/20.
 */

class BoardActivity : RxAppCompatActivity() {
    companion object {
        private const val ARG_BOARD_NAME = "ARG_BOARD_NAME"
        private const val ARG_BOARD_TITLE = "ARG_BOARD_TITLE"
        fun newIntent(context: Context, boardName: String, boardTitle: String): Intent {
            return Intent(context, BoardActivity::class.java).apply {
                putExtra(ARG_BOARD_NAME, boardName)
                putExtra(ARG_BOARD_TITLE, boardTitle)
            }
        }
    }

    private lateinit var boardName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardName = intent.getStringExtra(ARG_BOARD_NAME)

        initToolbar()

        val listView = findViewById(R.id.listView) as ListView
        val adapter = BoardAdapter(this, R.layout.item_board, boardName)
        listView.adapter = adapter

        RxAdapterView.itemClicks(listView)
                .compose(bindToLifecycle<Int>())
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe { i ->
                    val item = adapter.getItem(i)
                    val intent = ThreadActivity.newIntent(this, boardName, item.no, item.sub)
                    startActivity(intent)
                }
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val item = adapter.getItem(i)
            val intent = ThreadActivity.newIntent(this, boardName, item.no, item.sub)
            startActivity(intent)
        }

        App.api.getCatalogRx(boardName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle <Array<Catalog>> ())
                .subscribe ({ catalogs ->
                    val l = catalogs.flatMap { it.threads.asIterable() }
                    adapter.addAll(l)
                    adapter.notifyDataSetChanged()
                }, {
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
                })
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = intent.getStringExtra(ARG_BOARD_TITLE)
        toolbar.inflateMenu(R.menu.board)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_refresh -> {
                    // TODO("refresh")
                    true
                }
                else -> false
            }
        }
    }

    private class BoardAdapter(context: Context, val resource: Int, val boardName: String) : ArrayAdapter<Catalog.ThreadInfo>(context, resource) {
        private val inflater = LayoutInflater.from(context)
        private val width = context.resources.getDimension(R.dimen.image_width).toInt()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(resource, parent, false).apply {
                tag = ViewHolder(this).apply {
                    //                    comment.movementMethod = LinkMovementMethod.getInstance()
                }
            }
            val holder = view.tag as ViewHolder
            val item = getItem(position)

            holder.name.text = item.name
            holder.subject.text = item.sub
            holder.comment.text = item.com?.let { Html.fromHtml(it) }
            if (item.tn_w > 0 && item.tn_h > 0) {
                val filename = getThumbUri(boardName, item.tim)
                val h = width * item.tn_h / item.tn_w
                holder.image.layoutParams = holder.image.layoutParams.apply {
                    height = h
                }
                Picasso.with(context).load(filename).resize(width, h).into(holder.image)
            }
            return view
        }

        private class ViewHolder(view: View) {
            val name = view.findViewById(R.id.name) as TextView
            val subject = view.findViewById(R.id.subject) as TextView
            val comment = view.findViewById(R.id.comment) as TextView
            val image = view.findViewById(R.id.image) as ImageView
        }
    }
}
