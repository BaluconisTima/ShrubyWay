package com.shrubyway.game;

public class Pair<F, S> {
    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void dispose() {
        first = null;
        second = null;
    }
}
