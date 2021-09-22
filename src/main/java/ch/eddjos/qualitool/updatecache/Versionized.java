package ch.eddjos.qualitool.updatecache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Versionized<E> {
    private List<E> data;
    private int versionNr;

    public List<E> getData() {
        return data;
    }

    public int getVersionNr() {
        return versionNr;
    }

    public Versionized(int cacheNr, E value){
        this(cacheNr, Collections.singletonList(value));
    }
    public Versionized(int cacheNr, List<E> values){
        data = values;
        this.versionNr = cacheNr;
    }
    public static <E> Versionized<E> fromCacheEntry(int cacheNr, List<CacheEntry<E>> cacheEntries){
        return new Versionized<>(cacheNr,cacheEntries.stream().map(e->e.getValue()).collect(Collectors.toList()));
    }

    public Versionized<E> combine(Versionized<E> a){
        return combine(this,a);
    }

    public static <E> Versionized<E> combine(Versionized<E> a, Versionized<E> b){
        int max = a.getVersionNr()>b.getVersionNr()?a.getVersionNr():b.getVersionNr();
        List<E> data = new ArrayList<>(a.getData());
        data.addAll(b.getData());
        return new Versionized<E>(max,data);
    }

    @Override
    public String toString() {
        return "Versionized{" +
                "data=" + data +
                ", cacheNr=" + versionNr +
                '}';
    }
}
