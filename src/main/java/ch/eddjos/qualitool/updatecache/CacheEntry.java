package ch.eddjos.qualitool.updatecache;

public class CacheEntry<E> {
    public int getVersionNr() {
        return versionNr;
    }

    public int getPersonId() {
        return personId;
    }

    public int getDataId() {
        return dataId;
    }

    public E getValue() {
        return value;
    }

    private int versionNr;
    private int personId;
    private int dataId;
    private E value;

    public CacheEntry(int versionNr, int personId, int dataId, E value) {
        this.versionNr=versionNr;
        this.personId=personId;
        this.dataId=dataId;
        this.value=value;
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "versionNr=" + versionNr +
                ", personId=" + personId +
                ", dataId=" + dataId +
                ", value=" + value +
                '}';
    }
}
