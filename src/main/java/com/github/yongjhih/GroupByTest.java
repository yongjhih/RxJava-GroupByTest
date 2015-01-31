package com.github.yongjhih;

import rx.schedulers.*;
import rx.Observable;
import rx.functions.*;
import rx.observables.*;
import rx.util.*;
import rx.internal.operators.*;

public class GroupByTest {
    public static void main(String[] args) {
        /* original */
        Observable.range(1, 10)
            .groupBy(n -> n % 2 == 0)
            .flatMap((GroupedObservable<Boolean, Integer> g) -> {
                return Observable.just(g).flatMap(ObservableUtils.<Boolean, Integer>flatGroup()).groupBy(n -> n > 5);
            })
        .subscribe((final GroupedObservable<Boolean, Integer> g) -> {
            Observable.just(g).flatMap(ObservableUtils.<Boolean, Integer>flatGroup()).forEach(n -> System.out.println(g + ": " + n));
        });

        /* OperatorGroupByGroup */
        Observable.range(1, 10)
            .lift(new OperatorGroupByGroup<Integer, Boolean, Integer>(n -> n % 2 == 0))
            .lift(new OperatorGroupByGroup<GroupedObservable<Boolean, Integer>, Boolean, Integer>(n -> n > 5))
            .subscribe((final GroupedObservable<Boolean, Integer> g) -> {
                Observable.just(g).flatMap(ObservableUtils.<Boolean, Integer>flatGroup()).forEach(n -> System.out.println(g + ": " + n));
            });
    }
}
