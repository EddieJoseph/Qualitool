package ch.eddjos.qualitool.updatecache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UpdateCache<E> {

    private int cacheNr=0;
    private int size;

    private ReadWriteLock lock= new ReentrantReadWriteLock();

    private SlidingWindowStorage<CacheEntry> cache;

    public UpdateCache(int size){
        this.size=size;
        cache = new SlidingWindowStorage<>(CacheEntry.class,size);
    }

    public Versionized<E> update(int personId, int dataId, E value){
        try{
            lock.writeLock().lock();
            CacheEntry<E> entry = new CacheEntry<E>(cacheNr++,personId,dataId,value);
            cache.put(entry);
            return new Versionized(entry.getVersionNr(),entry.getValue());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Versionized<E> versionize(E data) {
        try{
            lock.readLock().lock();
            return new Versionized<>(this.cacheNr-1, data);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Versionized<E> versionize(List<E> data) {
        try{
            lock.readLock().lock();
            return new Versionized<>(this.cacheNr-1, data);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Versionized<E>  getUpdates(int cacheNr, int personId){
        try{
            lock.readLock().lock();
        if(cacheNr+size<this.cacheNr-1||cacheNr>this.cacheNr){
            return null;
        }
        CacheEntry<E>[] res = cache.get();
        HashMap<Integer,CacheEntry<E>> map = new HashMap<>();
        Arrays.stream(res).filter(e -> e.getPersonId() == personId).filter(e -> e.getVersionNr() > cacheNr).sorted(Comparator.comparingInt(CacheEntry::getVersionNr)).forEach(e->map.put(e.getDataId(),e));
        List<CacheEntry<E>> uniqueValues = new ArrayList<>(map.values());
        return Versionized.fromCacheEntry(this.cacheNr-1,uniqueValues);
        } finally {
            lock.readLock().unlock();
        }
    }

}
