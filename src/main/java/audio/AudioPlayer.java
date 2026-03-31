package audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.Song;

import java.io.File;

/**
 * 音频播放器类
 * 负责音乐的播放和停止
 */
public class AudioPlayer {
    
    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private boolean isPaused;
    private double volume;
    private PlaybackListener listener;
    
    private static final double DEFAULT_VOLUME = 0.8;
    
    public interface PlaybackListener {
        void onPlaybackComplete();
        void onPlaybackError(String error);
        void onPlaybackStarted(Song song);
    }
    
    public AudioPlayer() {
        this.volume = DEFAULT_VOLUME;
        this.isPaused = false;
        this.mediaPlayer = null;
    }
    
    /**
     * 播放指定歌曲
     */
    public void play(Song song) throws Exception {
        if (song == null || !song.fileExists()) {
            throw new IllegalArgumentException("歌曲文件不存在或无效");
        }
        
        // 停止当前播放
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        
        currentSong = song;
        File audioFile = song.getFile();
        String mediaUrl = audioFile.toURI().toString();
        
        try {
            Media media = new Media(mediaUrl);
            mediaPlayer = new MediaPlayer(media);
            
            // 设置音量
            mediaPlayer.setVolume(volume);
            
            // 监听播放完成事件
            mediaPlayer.setOnEndOfMedia(() -> {
                isPaused = false;
                if (listener != null) {
                    listener.onPlaybackComplete();
                }
            });
            
            // 监听错误事件
            mediaPlayer.setOnError(() -> {
                if (listener != null) {
                    listener.onPlaybackError(mediaPlayer.getError().toString());
                }
            });
            
            mediaPlayer.play();
            isPaused = false;
            
            if (listener != null) {
                listener.onPlaybackStarted(song);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("无法播放文件: " + audioFile.getAbsolutePath(), e);
        }
    }
    
    /**
     * 播放指定时长的歌曲
     * @param song 要播放的歌曲
     * @param durationSeconds 播放时长（秒）
     */
    public void playLimited(Song song, int durationSeconds) throws Exception {
        play(song);
        
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            // 当播放到指定时长时停止
            long durationMs = Math.min(durationSeconds * 1000L, 
                (long) mediaPlayer.getTotalDuration().toMillis());
            
            // 使用Timeline或Thread来实现定时停止
            Thread stopThread = new Thread(() -> {
                try {
                    Thread.sleep(durationMs);
                    if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        stop();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            stopThread.setDaemon(true);
            stopThread.start();
        }
    }
    
    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }
    
    /**
     * 恢复播放
     */
    public void resume() {
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.play();
            isPaused = false;
        }
    }
    
    /**
     * 停止播放
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            isPaused = false;
            currentSong = null;
        }
    }
    
    /**
     * 设置音量（0.0 - 1.0）
     */
    public void setVolume(double volume) {
        if (volume < 0.0 || volume > 1.0) {
            throw new IllegalArgumentException("音量必须在 0.0 到 1.0 之间");
        }
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
    
    /**
     * 获取当前音量
     */
    public double getVolume() {
        return volume;
    }
    
    /**
     * 设置播放位置（秒）
     */
    public void seek(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }
    
    /**
     * 获取当前播放位置（秒）
     */
    public double getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0;
    }
    
    /**
     * 获取总播放时长（秒）
     */
    public double getTotalDuration() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0;
    }
    
    /**
     * 检查是否正在播放
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
    
    /**
     * 检查是否暂停
     */
    public boolean isPaused() {
        return isPaused;
    }
    
    /**
     * 设置播放监听器
     */
    public void setPlaybackListener(PlaybackListener listener) {
        this.listener = listener;
    }
    
    /**
     * 获取当前播放的歌曲
     */
    public Song getCurrentSong() {
        return currentSong;
    }
    
    /**
     * 释放资源
     */
    public void dispose() {
        stop();
    }
}
