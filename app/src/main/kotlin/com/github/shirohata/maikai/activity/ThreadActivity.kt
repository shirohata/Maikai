package com.github.shirohata.maikai.activity

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.shirohata.maikai.App
import com.github.shirohata.maikai.R
import com.github.shirohata.maikai.model.Post
import com.github.shirohata.maikai.model.Thread
import com.github.shirohata.maikai.network.getImageUri
import com.github.shirohata.maikai.network.getThumbUri
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by hirohata on 2016/02/21.
 */

class ThreadActivity : RxAppCompatActivity() {
    companion object {
        private const val ARG_BOARD_NAME = "ARG_BOARD_NAME"
        private const val ARG_THREAD_NUMBER = "ARG_THREAD_NUMBER"
        private const val ARG_SUBJECT = "ARG_SUBJECT"
        fun newIntent(context: Context, boardName: String, threadNumber: Long, subject: String?): Intent {
            return Intent(context, ThreadActivity::class.java).apply {
                putExtra(ARG_BOARD_NAME, boardName)
                putExtra(ARG_THREAD_NUMBER, threadNumber)
                putExtra(ARG_SUBJECT, subject ?: "")
            }
        }
    }

    private lateinit var boardName: String
    private var threadNumber: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardName = intent.getStringExtra(ARG_BOARD_NAME)
        threadNumber = intent.getLongExtra(ARG_THREAD_NUMBER, 0)

        initToolbar()

        val listView = findViewById(R.id.listView) as ListView
        val adapter = ThreadAdapter(this, R.layout.item_thread, boardName)
        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, i, l ->
            // TODO show image
            val item = adapter.getItem(i)
            if (item.tim > 0 && item.ext != null)
                requestDownload(item.tim, item.ext)
        }

        App.api.getThreadRx(boardName, threadNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle <Thread> ())
                .subscribe ({ thread ->
                    adapter.addAll(thread.posts.asList())
                    adapter.notifyDataSetChanged()
                }, {
                    Log.e("get thread failed", boardName + "-" + threadNumber, it)
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
                }, {
                    Log.d("retrofit", "complete")
                })
    }

    private fun requestDownload(tim: Long, ext: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO show dialog
            request = createDownloadRequest(tim, ext)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
        } else {
            download(createDownloadRequest(tim, ext))
        }
    }

    private var request: DownloadManager.Request? = null

    private fun createDownloadRequest(tim: Long, ext: String): DownloadManager.Request {
        val uri = Uri.parse(getImageUri(boardName, tim, ext))
        return DownloadManager.Request(uri).apply {
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/$tim$ext")
        }
    }

    private fun download(request: DownloadManager.Request) {
        Toast.makeText(this, "Download start", Toast.LENGTH_SHORT).show()
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            10 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    request?.let { download(it) }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // TODO
                }
                request = null
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = intent.getStringExtra(ARG_SUBJECT).let {
            if (it.isBlank()) "No title" else it
        }
        toolbar.inflateMenu(R.menu.thread)
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

    private class ThreadAdapter(context: Context, val resource: Int, val boardName: String) : ArrayAdapter<Post>(context, resource) {
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
            holder.comment.text = item.com?.let {
                Html.fromHtml(it, {
                    null
                }, { b, s, editable, xmlReader ->

                })
                Html.fromHtml(it).apply {
                    getSpans(0, it.length, URLSpan::class.java).forEach {
                        it.url
                    }
                }
            }
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
            val comment = view.findViewById(R.id.comment) as TextView
            val image = view.findViewById(R.id.image) as ImageView
        }
    }

}
