package pl.com.seremak.service;

import io.vavr.collection.List;

public class test {

    public static void main(String[] args) {
        var list1 = List.of(1, 2, 3, 4, 5);

        var list2 = list1.subSequence(1, 2);

        System.out.println(list2);
    }
}
