// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.navgraph;

import java.util.BitSet;
import java.util.List;

/**
 * @author synopia
 */
public class BitMap {
    public static final int KERNEL_SIZE = 3;
    public static final float SQRT_2 = (float) Math.sqrt(2);
    BitSet map;

    public BitMap() {
        map = new BitSet(getNumberOfNodes());
    }

    public int offset(int x, int y) {
        return x + y * getWidth();
    }

    public void setPassable(int offset) {
        map.set(offset, true);
    }

    public boolean isPassable(int offset) {
        return map.get(offset);
    }

    public void setPassable(int x, int y) {
        setPassable(offset(x, y));
    }

    public boolean isPassable(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return false;
        }
        return isPassable(offset(x, y));
    }

    public void getSuccessors(int offset, List<Integer> successors) {
        int x = getX(offset);
        int y = getY(offset);
        if (y > 0 && isPassable(offset - getWidth())) {
            successors.add(offset - getWidth());
        }
        if (x < getWidth() - 1 && isPassable(offset + 1)) {
            successors.add(offset + 1);
        }
        if (y < getHeight() - 1 && isPassable(offset + getWidth())) {
            successors.add(offset + getWidth());
        }
        if (x > 0 && isPassable(offset - 1)) {
            successors.add(offset - 1);
        }

        if (x < getWidth() - 1 && y > 0 && isPassable(offset + 1 - getWidth())) {
            successors.add(offset + 1 - getWidth());
        }
        if (x < getWidth() - 1 && y < getHeight() - 1 && isPassable(offset + 1 + getWidth())) {
            successors.add(offset + 1 + getWidth());
        }
        if (x > 0 && y < getHeight() - 1 && isPassable(offset - 1 + getWidth())) {
            successors.add(offset - 1 + getWidth());
        }
        if (x > 0 && y > 0 && isPassable(offset - 1 - getWidth())) {
            successors.add(offset - 1 - getWidth());
        }
    }

    public boolean overlap(BitMap other) {
        BitSet temp = (BitSet) map.clone();
        temp.and(other.map);
        return temp.cardinality() > 0;
    }

    public void merge(BitMap other) {
        map.or(other.map);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int count = map.cardinality();
        for (int z = 0; z < getHeight(); z++) {
            for (int x = 0; x < getWidth(); x++) {
                if (map.get(offset(x, z))) {
                    sb.append("X");
                    count--;
                } else {
                    sb.append(" ");
                }
            }
            if (count == 0) {
                break;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getNumberOfNodes() {
        return getWidth() * getHeight();
    }

    public int getWidth() {
        return NavGraphChunk.SIZE_X;
    }

    public int getHeight() {
        return NavGraphChunk.SIZE_Z;
    }

    public float exactDistance(int from, int to) {
        int diff = to - from;
        if (diff == -getWidth() || diff == 1 || diff == getWidth() || diff == -1) {
            return 1;
        }
        if (diff == -getWidth() + 1 || diff == getWidth() + 1 || diff == getWidth() - 1 || diff == -getWidth() - 1) {
            return SQRT_2;
        }
        return 0;
    }

    public float fastDistance(int from, int to) {
        int fromX = getX(from);
        int fromY = getY(from);
        int toX = getX(to);
        int toY = getY(to);

        return (float) Math.abs(fromX - toX) + Math.abs(fromY - toY);
//        return (float) Math.sqrt( (fromX-toX)*(fromX-toX)+ (fromY-toY)*(fromY-toY));

    }

    public int getY(int id) {
        return id / getWidth();
    }

    public int getX(int id) {
        return id % getWidth();
    }
}
