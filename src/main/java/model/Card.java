package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 卡牌类
 * 代表一张卡牌，包含图片和对应的一个或多个歌曲
 */
public class Card {
    private String imageName;       // 图片文件名
    private File imageFile;         // 图片文件对象
    private String workName;        // 作品名称（显示用）
    private List<Song> songs;       // 该卡对应的所有歌曲
    private boolean isActive;       // 卡是否在场上（用于后台管理）
    
    private static final Random RANDOM = new Random();
    
    public Card(String imageName, String workName) {
        this.imageName = imageName;
        this.workName = workName;
        this.songs = new ArrayList<>();
        this.isActive = true;
    }
    
    /**
     * 添加歌曲
     */
    public void addSong(Song song) {
        if (song != null && !songs.contains(song)) {
            songs.add(song);
        }
    }
    
    /**
     * 移除歌曲
     */
    public void removeSong(Song song) {
        songs.remove(song);
    }
    
    /**
     * 获取所有歌曲
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
    
    /**
     * 随机获取一首歌曲
     * 如果该卡没有歌曲，返回null
     */
    public Song getRandomSong() {
        if (songs.isEmpty()) {
            return null;
        }
        return songs.get(RANDOM.nextInt(songs.size()));
    }
    
    /**
     * 获取指定索引的歌曲
     */
    public Song getSong(int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        }
        return null;
    }
    
    /**
     * 获取歌曲数量
     */
    public int getSongCount() {
        return songs.size();
    }
    
    /**
     * 检查图片是否存在
     */
    public boolean imageExists() {
        return imageFile != null && imageFile.exists();
    }
    
    // Getters and Setters
    public String getImageName() {
        return imageName;
    }
    
    public File getImageFile() {
        return imageFile;
    }
    
    public void setImageFile(File file) {
        this.imageFile = file;
    }
    
    public String getWorkName() {
        return workName;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return workName + " (图片: " + imageName + ", 歌曲数: " + songs.size() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Card)) {
            return false;
        }
        Card other = (Card) obj;
        return this.imageName.equals(other.imageName);
    }
    
    @Override
    public int hashCode() {
        return imageName.hashCode();
    }
}
