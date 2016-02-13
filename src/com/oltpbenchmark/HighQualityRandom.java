package com.oltpbenchmark;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class HighQualityRandom extends Random {

    private Lock l = new ReentrantLock();
    private long u;
    private long v = 4101842887655102017L;
    private long w = 1;

    public HighQualityRandom() {
        this(System.nanoTime());
    }

    public HighQualityRandom(long seed) {
        l.lock();
        u = seed ^ v;
        nextLong();
        v = u;
        nextLong();
        w = v;
        nextLong();
        l.unlock();
    }

    public long nextLong() {
        l.lock();
        try {
            u = u * 2862933555777941757L + 7046029254386353087L;
            v ^= v >>> 17;
            v ^= v << 31;
            v ^= v >>> 8;
            w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
            long x = u ^ (u << 21);
            x ^= x >>> 35;
            x ^= x << 4;
            long ret = (x + v) ^ w;
            return ret;
        } finally {
            l.unlock();
        }
    }

    protected int next(int bits) {
        return (int) (nextLong() >>> (64 - bits));
    }

    public static int SHUFFLE_THRESHOLD = 10;

    public static void shuffle(Object arr[], Random rnd) {
        // Shuffle array
        int size = arr.length;
        for (int i = size; i > 1; i--) {
            swap(arr, i - 1, rnd.nextInt(i));
        }
    }

    public static void shuffle(int[] arr, Random rnd) {
        // Shuffle array
        int size = arr.length;
        for (int i = size; i > 1; i--) {
            swap(arr, i - 1, rnd.nextInt(i));
        }
    }

    public static void shuffle(List<?> list, Random rnd) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--) {
                Collections.swap(list, i - 1, rnd.nextInt(i));
            }
        } else {
            Object arr[] = list.toArray();

            shuffle(arr, rnd);

            // Dump array back into list
            ListIterator it = list.listIterator();
            for (int i = 0; i < arr.length; i++) {
                it.next();
                it.set(arr[i]);
            }
        }
    }

    public static final <T> void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static final void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}