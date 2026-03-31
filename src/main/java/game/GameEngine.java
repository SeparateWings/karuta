package game;

import audio.AudioPlayer;
import model.Card;
import model.Deck;
import model.GameState;
import model.Song;

/**
 * 游戏引擎类
 * 控制游戏流程和逻辑
 */
public class GameEngine {
    
    private GameState gameState;
    private AudioPlayer audioPlayer;
    private GameRules gameRules;
    private GameListener listener;
    private Thread playbackThread;
    
    public interface GameListener {
        void onRoundStart(Card card);
        void onMusicStart(Song song);
        void onMusicComplete();
        void onMusicInterrupted();
        void onRoundComplete(GameState.Result result);
        void onRestMusicStart();
        void onRestMusicComplete();
        void onGameOver(GameState state);
        void onError(String error);
    }
    
    public GameEngine(AudioPlayer audioPlayer, GameRules gameRules) {
        this.audioPlayer = audioPlayer;
        this.gameRules = gameRules;
        this.gameState = new GameState();
    }
    
    /**
     * 初始化游戏
     */
    public void initializeGame(Deck deck, int totalRounds) {
        gameState.initializeGame(deck, totalRounds);
    }
    
    /**
     * 开始游戏
     */
    public void startGame(Deck deck, int totalRounds) {
        try {
            initializeGame(deck, totalRounds);
            startNewRound();
        } catch (Exception e) {
            if (listener != null) {
                listener.onError("游戏初始化失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 开始新的回合
     */
    public void startNewRound() {
        if (gameState.isGameOver()) {
            endGame();
            return;
        }
        
        gameState.startNewRound();
        startInnerRound();
    }
    
    /**
     * 内部回合流程
     */
    private void startInnerRound() {
        // 选择卡牌
        gameState.selectCard();
        Card currentCard = gameState.getCurrentCard();
        
        if (currentCard == null) {
            endGame();
            return;
        }
        
        if (listener != null) {
            listener.onRoundStart(currentCard);
        }
        
        // 准备播放音乐
        Song song = gameState.getCurrentSong();
        if (song != null) {
            playMusicWithLimit(song);
        }
    }
    
    /**
     * 播放音乐（带时长限制）
     */
    private void playMusicWithLimit(Song song) {
        try {
            gameState.startMusicPlayback();
            
            int duration = gameRules.getPlaybackDuration();
            
            if (listener != null) {
                listener.onMusicStart(song);
            }
            
            // 在线程中播放，避免阻塞UI
            playbackThread = new Thread(() -> {
                try {
                    audioPlayer.playLimited(song, duration);
                    
                    // 等待音乐播放完成
                    Thread.sleep(duration * 1000L + 100);
                    
                    gameState.musicPlaybackComplete();
                    if (listener != null) {
                        listener.onMusicComplete();
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError("播放音乐失败: " + e.getMessage());
                    }
                }
            });
            playbackThread.setDaemon(true);
            playbackThread.start();
            
        } catch (Exception e) {
            if (listener != null) {
                listener.onError("无法播放音乐: " + e.getMessage());
            }
        }
    }
    
    /**
     * 停止当前音乐
     */
    public void stopCurrentMusic() {
        audioPlayer.stop();
        if (listener != null) {
            listener.onMusicInterrupted();
        }
    }
    
    /**
     * 提交对局结果
     */
    public void submitRoundResult(GameState.Result result) {
        audioPlayer.stop();
        gameState.submitResult(result);
        
        if (listener != null) {
            listener.onRoundComplete(result);
        }
        
        // 检查是否需要中场休息
        if (shouldPlayRestMusic()) {
            playRestMusic();
        } else if (gameState.isGameOver()) {
            endGame();
        }
    }
    
    /**
     * 检查是否应该播放中场休息
     */
    private boolean shouldPlayRestMusic() {
        return gameState.getCurrentRound() % gameRules.getRestIntervalRounds() == 0 
            && gameState.getCurrentRound() < gameState.getTotalRounds()
            && gameState.getCurrentDeck().hasActiveCards();
    }
    
    /**
     * 播放中场休息音乐
     */
    private void playRestMusic() {
        try {
            gameState.enterRestMusic();
            
            if (listener != null) {
                listener.onRestMusicStart();
            }
            
            Song restSong = gameRules.getRestMusic();
            if (restSong != null && restSong.fileExists()) {
                audioPlayer.play(restSong);
            }
            
        } catch (Exception e) {
            if (listener != null) {
                listener.onError("无法播放中场休息音乐: " + e.getMessage());
            }
        }
    }
    
    /**
     * 从中场休息继续
     */
    public void resumeFromRest() {
        audioPlayer.stop();
        gameState.resumeFromRest();
        
        if (listener != null) {
            listener.onRestMusicComplete();
        }
        
        startNewRound();
    }
    
    /**
     * 结束游戏
     */
    public void endGame() {
        audioPlayer.stop();
        gameState.endGame();
        
        if (listener != null) {
            listener.onGameOver(gameState);
        }
    }
    
    /**
     * 中断游戏
     */
    public void abortGame() {
        audioPlayer.stop();
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
        }
        gameState.endGame();
    }
    
    /**
     * 设置游戏监听器
     */
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }
    
    /**
     * 获取当前游戏状态
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * 获取当前卡组
     */
    public Deck getCurrentDeck() {
        return gameState.getCurrentDeck();
    }
    
    /**
     * 释放资源
     */
    public void dispose() {
        audioPlayer.dispose();
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
        }
    }
}
