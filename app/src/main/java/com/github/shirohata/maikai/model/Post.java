package com.github.shirohata.maikai.model;

import java.util.Map;

/**
 * Created by hirohata on 2016/02/20.
 */
public class Post {
    public long no;     // Post number 	1-9999999999999 	9001
    public long resto;  // Reply to 	0 (is a thread OP), 1-9999999999999 	0
    public int sticky; // Stickied thread? 	0 (no), 1 (yes) 	1
    public int closed; //  Closed thread? 	0 (no), 1 (yes) 	1
    public int archived;    // Archived thread? 	0 (no), 1 (yes) 	1
    public String now; // Date and time 	MM\/DD\/YY(Day)HH:MM (:SS on some boards), EST/EDT timezone 	08\/08\/12(Wed)01:11
    public long time; // UNIX timestamp 	UNIX timestamp 	1344570123
    public String name; // Name 	text 	moot
    public String trip; // Tripcode 	text (format: !tripcode!!securetripcode) 	!Ep8pui8Vw2
    public String id; // ID 	text (8 characters), Mod, Admin, Developer, Founder 	Admin
    public String capcode; // Capcode 	none, mod, admin, admin_highlight, developer, founder 	admin
    public String country; // Country code 	text (2 characters, ISO 3166-1 alpha-2), XX (unknown) 	XX
    public String country_name; // Country name 	text 	Unknown
    public String sub; // Subject 	text 	This is a subject
    public String com; // Comment 	text (includes escaped HTML) 	This is a comment
    public long tim; // Renamed filename 	UNIX timestamp + milliseconds 	1344402680740
    public String filename; // Original filename 	text 	OPisa
    public String ext; /// File extension 	.jpg, .png, .gif, .pdf, .swf, .webm 	.jpg
    public long fsize; // File size 	0-10485760 	2500
    public String md5; // File MD5 	text (24 character, packed base64 MD5 hash) 	NOetrLVnES3jUn1x5ZPVAg==
    public int w; // Image width 	1-10000 	500
    public int h; // Image height 	1-10000 	500
    public int tn_w; // Thumbnail width 	1-250 	250
    public int tn_h; // Thumbnail height 	1-250 	250
    public int filedeleted; // File deleted? 	0 (no), 1 (yes) 	0
    public int spoiler; // Spoiler image? 	0 (no), 1 (yes) 	0
    public int custom_spoiler; // Custom spoilers? 	1-99 	3
    public int omitted_posts; // # replies omitted 	1-10000 	33
    public int omitted_images; // # image replies omitted 	1-10000 	21
    public int replies; // # replies total 	0-99999 	231
    public int images; // # images total 	0-99999 	132
    public int bumplimit; // Bump limit met? 	0 (no), 1 (yes) 	0
    public int imagelimit; // Image limit met? 	0 (no), 1 (yes) 	1
    public Map<String, Integer> capcode_replies; // Capcode user replies? 	array of capcode type and post IDs 	{"admin":[1234,1267]}
    public long last_modified; // Time when last modified 	UNIX timestamp 	1344571233
    public String tag; // Thread tag 	text 	Loop
    public String semantic_url; // Thread URL slug 	text 	daily-programming-thread
}
