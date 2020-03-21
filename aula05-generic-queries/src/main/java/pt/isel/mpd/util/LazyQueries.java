package pt.isel.mpd.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class LazyQueries {
     /**
     * Versao 4
     */
    public static <T> Iterable<T> filter(Iterable<T> src, Predicate<T> pred) {
            if(!src.hasNext()) throw new IllegalArgumentException("Source sequence is empty!");

            //Ir buscar o primeiro valor com o while e por no next
            //HasNext vai buscar o proximo valor e poe no next. se não encontrar retorna false
            //next retorna next e mete null

            return () -> new Iterator<T>{
            T next = src.next();

            @Override
            public boolean hasNext(){
                if(next == null)
                {
                    while((src.hasNext() && pred.test(next = src.next())));
                    return src.hasNext();
                }
                return true;
            }

            @Override
            public T next() {
                if(hasNext()){
                    var aux = next;
                    next = null;
                    return aux;
                }
                throw new NoSuchElementException("No more elements available");
            }
        }
    }




    public static <T> Iterable<T> skip(Iterable<T> src, int nr) {
        return () -> {
            Iterator<T> iter = src.iterator();
            int idx = nr;
            while(idx-- > 0 && iter.hasNext()) iter.next();
            return iter;
        };
    }
    public static <T, R> Iterable<R> map(Iterable<T> src, Function<T, R> mapper) {
        return () -> new Iterator<R>() {
            Iterator<T> iter = src.iterator();
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }
            @Override
            public R next() {
                return mapper.apply(iter.next());
            }
        };
    }

    /**
     * Terminal operation
     */
    public static <T> int count(Iterable<T> src) {
        int nr = 0;
        for (T item: src) { nr++; }
        return nr;
    }
    /**
     * Terminal operation
     */
    public static <T extends Comparable<T>> T max(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        if(!iter.hasNext()) throw new IllegalArgumentException("Source sequence is empty!");
        T first = iter.next();
        while(iter.hasNext()) {
            T curr = iter.next();
            if(curr.compareTo(first) > 0)
                first = curr;
        }
        return first;
    }

    public static <T> Iterable<T> limit(Iterable<T> source,int number){
        if(!source.hasNext()) throw new IllegalArgumentException("Source sequence is empty!");

        return new () -> Iterator<T>(){
            int nmr = number;
            Iterator<T> iter = source.iterator;
            public bool hasNext(){
                return iter.hasNext() && nmr > 0;
            }

            public T next(){
                if(!hasNext())  throw new NoSuchElementException("No more elements available");
                nmr--;
                return iter.next();
            }
        }
    }


}
