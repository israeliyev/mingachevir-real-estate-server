package io.mingachevir.mingachevirrealestateserver.model.response;

public class ImageResponse {
    private String filePath;

    public ImageResponse(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
