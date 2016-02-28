package com.github.shirohata.maikai.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.github.shirohata.maikai.R

/**
 * Created by hirohata on 2016/02/20.
 * TODO 板名表示　板追加　並び替え
 */
class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Boards"

        listView = findViewById(R.id.listView) as ListView
        val adapter = MainAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val boardName = adapter.getItem(i)
            val intent = BoardActivity.newIntent(this, boardName.first, boardName.second)
            startActivity(intent)
        }
    }

    class MainAdapter(context: Context, val resource: Int) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        override fun getCount(): Int {
            return boardNames.count()
        }

        override fun getItem(p0: Int): Pair<String, String> {
            return boardNames.get(p0)
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong();
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view = convertView ?: inflater.inflate(resource, parent, false)
            val item = getItem(position)
            (view as TextView).text = item.second
            return view
        }

        // FIXME hardcode
        val boardNames = listOf(
                "a".to("Anime & Manga"),
                "c".to("Anime/Cute"),
                "w".to("Anime/Wallpapers"),
                "m".to("Mecha"),
                "cgl".to("Cosplay & EGL"),
                "cm".to("Cute/Male"),
                "f".to("Flash"),
                "n".to("Transportation"),
                "jp".to("Otaku Culture"),
                "v".to("Video Games"),
                "vg".to("Video Game Generals"),
                "vp".to("Pokémon"),
                "vr".to("Retro Games"),
                "co".to("Comics & Cartoons"),
                "g".to("Technology"),
                "tv".to("Television & Film"),
                "k".to("Weapons"),
                "o".to("Auto"),
                "an".to("Animals & Nature"),
                "tg".to("Traditional Games"),
                "sp".to("Sports"),
                "asp".to("Alternative Sports"),
                "sci".to("Science & Math"),
                "his".to("History & Humanities"),
                "int".to("International"),
                "out".to("Outdoors"),
                "toy".to("Toys"),
                "i".to("Oekaki"),
                "po".to("Papercraft & Origami"),
                "p".to("Photography"),
                "ck".to("Food & Cooking"),
                "ic".to("Artwork/Critique"),
                "wg".to("Wallpapers/General"),
                "mu".to("Music"),
                "fa".to("Fashion"),
                "3".to("3DCG"),
                "gd".to("Graphic Design"),
                "diy".to("Do-It-Yourself"),
                "wsg".to("Worksafe GIF"),
                "biz".to("Business & Finance"),
                "trv".to("Travel"),
                "fit".to("Fitness"),
                "x".to("Paranormal"),
                "lit".to("Literature"),
                "adv".to("Advice"),
                "lgbt".to("LGBT"),
                "mlp".to("Pony"),
                "news".to("Current News"),
                "wsr".to("Worksafe Requests"),
                "b".to("Random"),
                "r9k".to("ROBOT9001"),
                "pol".to("Politically Incorrect"),
                "soc".to("Cams & Meetups"),
                "s4s".to("Shit 4chan Says"),
                "s".to("Sexy Beautiful Women"),
                "hc".to("Hardcore"),
                "hm".to("Handsome Men"),
                "h".to("Hentai"),
                "e".to("Ecchi"),
                "u".to("Yuri"),
                "d".to("Hentai/Alternative"),
                "y".to("Yaoi"),
                "t".to("Torrents"),
                "hr".to("High Resolution"),
                "gif".to("Adult GIF"),
                "aco".to("Adult Cartoons"),
                "r".to("Adult Requests")
        )
    }
}
