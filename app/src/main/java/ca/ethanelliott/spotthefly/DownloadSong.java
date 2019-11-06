package ca.ethanelliott.spotthefly;

public class DownloadSong {
    private String name;
    private String url;
    private boolean downloading;

    DownloadSong(String name, String url, boolean downloading) {
        this.name = name;
        this.url = url;
        this.downloading = downloading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}
