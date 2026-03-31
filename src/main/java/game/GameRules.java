package game;

import config.ConfigManager;
import model.Song;

import java.io.File;

/**
 * 游戏规则类
 * 定义游戏的各项参数和规则
 */
public class GameRules {
    
    public enum FailureMode {
        PASS,       // 轮空
        SKIP        // 弃牌
    }
    
    private int minPlaybackDuration;      // 最小播放时长（秒）
    private int maxPlaybackDuration;      // 最大播放时长（秒）
    private int restIntervalRounds;       // 每多少回合后播放中场休息（0表示不播放）
    private Song restMusic;               // 中场休息音乐
    private FailureMode failureMode;      // 对局失败处理方式
    private boolean enableRestMusic;      // 是否启用中场休息
    
    private static final int DEFAULT_MIN_DURATION = 10;
    private static final int DEFAULT_MAX_DURATION = 30;
    private static final int DEFAULT_REST_INTERVAL = 5;  // 每5回合播放一次休息
    
    public GameRules(ConfigManager config) {
        this.minPlaybackDuration = config.getMinDuration();
        this.maxPlaybackDuration = config.getMaxDuration();
        this.restIntervalRounds = DEFAULT_REST_INTERVAL;
        this.enableRestMusic = true;
        
        // 加载中场休息音乐
        File restMusicFile = config.getRestMusicFile();
        if (restMusicFile.exists()) {
            this.restMusic = new Song(restMusicFile.getName(), restMusicFile);
        }
        
        // 设置失败模式
        String mode = config.getFailureMode();
        this.failureMode = FailureMode.valueOf(mode.toUpperCase());
    }
    
    /**
     * 获取本回合应该播放的时长（秒）
     * 随机在最小和最大时长之间
     */
    public int getPlaybackDuration() {
        if (minPlaybackDuration >= maxPlaybackDuration) {
            return minPlaybackDuration;
        }
        return minPlaybackDuration + 
            (int) (Math.random() * (maxPlaybackDuration - minPlaybackDuration + 1));
    }
    
    /**
     * 获取中场休息间隔（多少回合后播放）
     */
    public int getRestIntervalRounds() {
        return restIntervalRounds;
    }
    
    /**
     * 设置中场休息间隔
     */
    public void setRestIntervalRounds(int interval) {
        if (interval < 0) {
            throw new IllegalArgumentException("休息间隔不能为负数");
        }
        this.restIntervalRounds = interval;
    }
    
    /**
     * 获取中场休息音乐
     */
    public Song getRestMusic() {
        return restMusic;
    }
    
    /**
     * 设置中场休息音乐
     */
    public void setRestMusic(Song music) {
        this.restMusic = music;
    }
    
    /**
     * 获取失败处理模式
     */
    public FailureMode getFailureMode() {
        return failureMode;
    }
    
    /**
     * 设置失败处理模式
     */
    public void setFailureMode(FailureMode mode) {
        this.failureMode = mode;
    }
    
    /**
     * 检查是否启用中场休息
     */
    public boolean isRestMusicEnabled() {
        return enableRestMusic && restMusic != null;
    }
    
    /**
     * 启用/禁用中场休息
     */
    public void setRestMusicEnabled(boolean enabled) {
        this.enableRestMusic = enabled;
    }
    
    /**
     * 验证播放时长是否有效
     */
    public boolean isValidDuration(int seconds) {
        return seconds >= minPlaybackDuration && seconds <= maxPlaybackDuration;
    }
    
    @Override
    public String toString() {
        return String.format(
            "GameRules{duration=%d-%ds, restInterval=%d, failureMode=%s}",
            minPlaybackDuration, maxPlaybackDuration, restIntervalRounds, failureMode
        );
    }
}
