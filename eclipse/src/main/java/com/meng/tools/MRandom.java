package com.meng.tools;

import java.util.Random;

public class MRandom extends Random {
    @Override
    public int nextInt() {
        return new Random().nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return new Random().nextInt(bound);
    }
}
