package com.github.shirohata.maikai.model;

/**
 * Created by hirohata on 2016/02/20.
 */
public class Catalog {
    public int page;
    public ThreadInfo[] threads;

    public static class ThreadInfo extends Post {
        public Post[] last_replies;
    }
}
