# 快速开始指南

## 📋 前置条件

- **Java** 11 或更高版本
- **JavaFX SDK** 11 或更高版本（已集成在 JDK 11+ 中）
- **Maven** 3.6+（可选，用于依赖管理）

## 🚀 快速启动

### 方法 1：使用 Maven（推荐）

1. **安装依赖**
   ```bash
   cd karuta
   mvn clean install
   ```

2. **运行应用**
   ```bash
   mvn javafx:run
   ```

### 方法 2：命令行编译运行

1. **进入项目目录**
   ```bash
   cd karuta
   ```

2. **编译源代码**
   ```bash
   javac -d out -encoding UTF-8 src/Main.java src/**/*.java
   ```

3. **运行应用**
   ```bash
   java -cp out:javafx-sdk/lib/* --add-modules javafx.controls,javafx.media Main
   ```

## 📁 项目设置

### 目录结构

```
karuta/
├── src/                          # Java 源代码
│   ├── Main.java
│   ├── model/                   # 数据模型
│   ├── config/                  # 配置管理
│   ├── audio/                   # 音频处理
│   ├── game/                    # 游戏逻辑
│   └── ui/                      # UI 界面
├── config/                      # 配置文件和资源
│   ├── config.txt              # 主配置文件
│   ├── decks/                  # 卡组配置 CSV
│   ├── music/                  # 🎵 放入音乐文件
│   └── images/                 # 🖼️ 放入卡片图片
├── docs/                       # 文档
├── pom.xml                    # Maven 项目配置
└── README.md                  # 项目说明
```

## 🎮 使用流程

### 1. 添加媒体文件

#### 添加音乐
将音乐文件放入 `config/music/` 文件夹：
- 支持格式：MP3, WAV, FLAC, OGG, AAC
- 文件名建议使用英文或拼音（避免特殊字符）

#### 添加图片
将卡片图片放入 `config/images/` 文件夹：
- 推荐格式：PNG 或 JPG
- 推荐分辨率：300×400 像素

### 2. 配置卡组

编辑或创建 CSV 卡组文件（`config/decks/deck1.csv`）：

```csv
image_name,work_name,songs
card_spring.png,春樱祭典,spring_1.mp3|spring_2.mp3
card_summer.png,夏季祭,summer_opening.mp3
card_autumn.png,秋风诗社,autumn_1.mp3|autumn_2.mp3|autumn_3.mp3
```

**CSV 格式说明：**
- 第一行为表头，必须精确匹配
- `image_name` - 图片文件名
- `work_name` - 作品/卡片名称
- `songs` - 对应的歌曲列表，用 `|` 分隔多首歌

### 3. 调整游戏参数

编辑 `config/config.txt`：

```properties
# 播放时长（秒）
min_duration=10      # 最少播放秒数
max_duration=30      # 最多播放秒数

# 中场休息
rest_music=rest.mp3  # 休息音乐文件名
failure_mode=PASS    # PASS(轮空) 或 SKIP(弃牌)
```

### 4. 启动应用

1. 运行 `Main` 类
2. 在卡组选择界面选择卡组
3. 设置回合数
4. 点击 **开始游戏**

## 🎮 游戏操作

### 主界面
- 📋 **选择卡组** - 从下拉列表选择
- 🔢 **设置回合数** - 调整轮数
- ▶️ **开始游戏** - 启动游戏

### 游戏进行中
- 🎵 **播放歌曲** - 自动从卡中随机选歌
- ✓ **对局成功** - 该卡从场上移除
- ✗ **对局失败** - 该卡保留在场上
- 📊 **实时统计** - 显示成功率和进度

### 后台管理面板
- 👀 **监控在场卡动** - 实时显示场上卡牌
- ➕ **添加卡牌** - 手动恢复已出局的卡
- ➖ **移除卡牌** - 手动将卡移出场外
- 🔄 **刷新状态** - 同步最新数据
- 💾 **导出状态** - 保存当前游戏状态

## 🐛 常见问题

### Q: 启动时出现找不到文件错误
**A:** 检查 `config/config.txt` 中的路径配置是否正确，确保 `config/music/` 和 `config/images/` 文件夹存在。

### Q: 看不到卡片图片
**A:** 
1. 确保图片文件在 `config/images/` 中
2. 图片文件名和 CSV 中的 `image_name` 必须完全相同（包括扩展名）
3. 检查文件名编码是否为 UTF-8

### Q: 无法播放音乐
**A:**
1. 确保音乐文件在 `config/music/` 中
2. 音乐文件格式是否被支持（MP3, WAV, FLAC, OGG, AAC）
3. 检查文件是否损坏

### Q: CVS 文件加载失败
**A:**
1. 确保 CSV 文件使用 UTF-8 编码
2. 检查逗号是否为英文逗号 `,`
3. 歌曲分隔符是否为英文竖线 `|`
4. 确保首行为 `image_name,work_name,songs`

### Q: 后台管理面板为空
**A:** 游戏启动后数据才会显示，请先选择卡组开始游戏。

## 💡 提示

- 🎵 **多首歌** - 一张卡可以对应多首歌，游戏时会随机选择
- 🔄 **中场休息** - 每 5 回合自动播放休息音乐
- 📊 **统计** - 实时显示成功率和回合数
- 🛡️ **误操作保护** - 后台管理面板可恢复意外删除的卡

## 📞 获取帮助

- 查看详细文档：`docs/DEVELOPMENT.md`
- 查看 CSV 格式说明：`docs/CSV_FORMAT.md`
- 查看架构设计：`docs/ARCHITECTURE.md`

## 🎉 祝您使用愉快！

现在您可以开始组织您的歌牌点歌活动了！

