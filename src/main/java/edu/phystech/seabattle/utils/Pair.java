package edu.phystech.seabattle.utils;

import java.util.Objects;

public class Pair{
    private int field1;
    private int field2;

    public Pair(int field1, int field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public int getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return field1 == pair.field1 &&
                field2 == pair.field2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2);
    }
}
