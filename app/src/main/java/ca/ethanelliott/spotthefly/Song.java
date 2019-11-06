package ca.ethanelliott.spotthefly;

public class Song {
    private int id;
    private String uuid;
    private String name;
    private byte[] data;

    Song(int id, String uuid, String name, byte[] data) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
