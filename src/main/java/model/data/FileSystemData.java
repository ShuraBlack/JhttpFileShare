package model.data;

public class FileSystemData {

    private final String name;

    private final long totalSpace;

    private final long availableSpace;


    public FileSystemData(String name, long totalSpace, long availableSpace) {
        this.name = name;
        this.totalSpace = totalSpace;
        this.availableSpace = availableSpace;
    }

    public String getName() {
        return name;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getAvailableSpace() {
        return availableSpace;
    }

    public void loadData() {
    }

}
