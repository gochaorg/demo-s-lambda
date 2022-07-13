package org.example.lazyprop;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Wrapper {
    Object source();

    public static class UnwrapIterator implements Iterator<Object> {
        private final Set<Object> visited = new HashSet<>();
        private final List<Object> workSet;

        public UnwrapIterator(List<Object> workSet){
            this.workSet = workSet!=null ? new ArrayList<>(workSet) : new ArrayList<>();
        }

        public UnwrapIterator(Object from){
            this.workSet = from!=null ? new ArrayList<>(List.of(from)) : new ArrayList<>();
        }

        @Override
        public boolean hasNext() {
            return !workSet.isEmpty();
        }

        @Override
        public Object next() {
            if( workSet.isEmpty() )throw new NoSuchElementException();
            var res = workSet.remove(0);
            if( res instanceof Wrapper ){
                var source = ((Wrapper) res).source();
                if( source!=null && !visited.contains(source) ){
                    workSet.add(0,source);
                }
            }
            visited.add(res);
            return res;
        }
    }
    public static class Unwrap {
        public final Object from;

        public Unwrap(Object from){
            this.from = from;
        }

        public Iterable<Object> toIterable(){
            return new Iterable<Object>() {
                @Override
                public Iterator<Object> iterator() {
                    return new UnwrapIterator(from);
                }
            };
        }

        public List<Object> toList(){
            return StreamSupport
                .stream(toIterable().spliterator(), false)
                .collect(Collectors.toList());
        }
        public Stream<Object> toStream(){
            return StreamSupport
                .stream(toIterable().spliterator(), false);
        }
    }
    public default Unwrap unwrap(){ return new Unwrap(this); }
}
