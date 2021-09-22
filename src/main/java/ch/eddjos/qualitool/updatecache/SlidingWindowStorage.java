package ch.eddjos.qualitool.updatecache;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SlidingWindowStorage<E> {

    private E [] storage;
    private int endindex=0;
    private int startindex=0;
    private int size;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SlidingWindowStorage(Class<E> clazz,int size){
        storage = (E [])Array.newInstance(clazz,size);
        this.size=size;
    }

    public void put(E value){
        try{
            lock.writeLock().lock();
            storage[endindex]=value;
            endindex =  (endindex+1) % size;
            if(endindex==startindex){
                startindex=(startindex+1)%size;
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    public E[] get(){
        try{
            lock.readLock().lock();
            if(startindex<=endindex){
                return (E[])getElements(startindex,endindex);
            }else{
                return (E[])ArrayUtils.addAll(getElements(endindex,size),getElements(0,endindex));
            }
        }finally {
            lock.readLock().unlock();
        }
    }

    private Object[] getElements(int startindex, int endindex){
        return ArrayUtils.subarray(storage,startindex,endindex);
    }

}
