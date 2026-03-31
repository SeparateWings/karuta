# 开发指南

## 项目架构概览

```
Model Layer (数据模型)
    ↓
Config Layer (配置管理)
    ↓
Game Layer (游戏逻辑)
    ↓
Audio Layer (音频处理)
    ↓
UI Layer (用户界面 - JavaFX)
```

## 核心类说明

### Model 包 (`model/`)
- **Song.java** - 表示一首歌曲
  - `fileName` - 文件名
  - `displayName` - 显示名称
  - `duration` - 时长（毫秒）

- **Card.java** - 表示一张卡牌
  - `imageName` - 图片名称
  - `workName` - 作品名称
  - `songs` - 该卡对应的歌曲列表
  - `isActive` - 是否在场上

- **Deck.java** - 表示一个卡组
  - `cards` - 卡组中的所有卡牌
  - `activeCards` - 当前在场上的卡牌

- **GameState.java** - 游戏状态管理
  - `RoundState` - 回合状态枚举
  - `Result` - 对局结果枚举
  - 追踪游戏进行情况

### Config 包 (`config/`)
- **ConfigManager.java** - 配置管理
  - 加载 `config.txt` 主配置
  - 加载 CSV 卡组配置
  - 验证文件完整性

### Audio 包 (`audio/`)
- **AudioPlayer.java** - 音频播放引擎
  - 基于 JavaFX MediaPlayer
  - 支持多格式（MP3, WAV, FLAC, OGG, AAC）
  - 支持限时播放（自动停止）

### Game 包 (`game/`)
- **GameEngine.java** - 游戏引擎
  - 控制游戏流程
  - 管理回合和结果
  - 触发事件回调

- **GameRules.java** - 游戏规则
  - 定义播放时长、中场休息等
  - 失败处理模式

### UI 包 (`ui/`)
- **MainWindow.java** - 主窗口和场景管理
- **DeckSelectionScreen.java** - 卡组选择界面
- **GameScreen.java** - 游戏进行界面
- **AdminPanel.java** - 后台管理面板

## 开发流程

### 1. 编译

#### 使用 Maven
```bash
cd karuta
mvn clean compile
```

#### 使用 javac
```bash
javac -cp .:lib/* src/**/*.java
```

### 2. 运行

#### 使用 Maven
```bash
mvn javafx:run
```

#### 使用 java
```bash
java -cp .:javafx-sdk/lib/* --add-modules javafx.controls,javafx.media Main
```

### 3. 构建

```bash
mvn clean package
```

## 扩展功能

### 1. 添加新的游戏规则
继承 `GameRules` 类或修改其配置参数。

### 2. 实现网络对战
在 `GameEngine` 中添加网络通信逻辑，同步游戏状态。

### 3. 添加统计和排行榜
创建 `Statistics` 类，记录游戏数据，实现数据持久化。

### 4. 自定义 UI 主题
修改 JavaFX CSS 样式表或使用主题库（如 ControlsFX）。

### 5. Android 移植
使用 libGDX 或 Android NDK 调用 C/C++ 代码实现跨平台。

## 常见问题

**Q: 如何添加新的卡组？**
A: 在 `config/decks/` 目录下创建新的 CSV 文件，按照格式编写，然后在应用中选择即可。

**Q: 如何修改播放时长？**
A: 编辑 `config/config.txt` 中的 `min_duration` 和 `max_duration` 字段。

**Q: 如何支持更多音频格式？**
A: 修改 `ConfigManager.java` 中的 `isSupportedAudioFormat()` 方法。

**Q: 后台管理面板有什么用？**
A: 用于实时监控游戏进度、手动调整卡牌状态，防止误操作或软件故障导致的不一致。

## 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

MIT License - 详见 LICENSE 文件

