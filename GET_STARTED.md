# 🎊 开始使用歌牌点歌系统

欢迎使用 **Karuta Jukebox 歌牌点歌系统** v1.0.0！

本项目已完整实现所有核心功能，现在就可以立即使用。

---

## ⚡ 5 分钟快速上手

### 第 0 步：验证 Java 环境
```powershell
# Windows 用户：打开 PowerShell
java -version

# 如果显示 Java 11 或更高版本，说明环境已准备好
# 否则需要下载安装 Java: https://www.java.com/zh_CN/download/
```

### 第 1 步：添加一首音乐文件（1 分钟）
```
1. 找一个 MP3 文件（如：test_song.mp3）
2. 复制到：karuta/config/music/
3. 完成！
```

### 第 2 步：添加一张卡片图片（1 分钟）
```
1. 找一个 PNG 或 JPG 图片（如：card1.png）
2. 复制到：karuta/config/images/
3. 完成！
```

### 第 3 步：编辑卡组文件（2 分钟）
```
打开 karuta/config/decks/deck1.csv

改成：
image_name,work_name,songs
card1.png,我的卡片,test_song.mp3

保存！
```

### 第 4 步：运行应用（1 分钟）
```powershell
# Windows 用户，在 karuta 目录执行：
mvn clean javafx:run

# 或者双击运行脚本（如果存在）
```

### 第 5 步：开始游戏！🎮
1. 选择 "deck1"（或您创建的卡组名）
2. 设置回合数（如 5）
3. 点击 "开始游戏" 按钮
4. 监听歌曲，点击成功或失败
5. 享受游戏！

---

## 📂 项目文件说明

### 重要文件
| 文件 | 说明 |
|------|------|
| `src/Main.java` | 启动文件 |
| `config/config.txt` | 主配置（播放时长等） |
| `config/decks/*.csv` | 卡组配置 |
| `pom.xml` | Maven 配置 |

### 文件夹结构
```
karuta/                         # 项目根文件夹
├── src/                       # 源代码目录
│   ├── Main.java             # 启动类
│   ├── model/                # 数据模型
│   ├── config/               # 配置管理
│   ├── audio/                # 音频引擎
│   ├── game/                 # 游戏逻辑
│   └── ui/                   # 用户界面
├── config/                   # 配置和媒体文件
│   ├── config.txt           # ⚙️ 游戏参数配置
│   ├── music/               # 🎵 放入音乐文件
│   ├── images/              # 🖼️ 放入卡片图片
│   └── decks/               # 📋 卡组配置文件
├── docs/                    # 文档目录
├── pom.xml                 # Maven 项目配置
└── [各种文档.md]           # 说明文档
```

---

## 🎵 配置您的第一个卡组

### 场景：4 个卡片，每个有 1-3 首歌

#### 第 1 步：准备文件
```
收集以下文件：
图片: card1.png, card2.png, card3.png, card4.png
音乐: song1.mp3, song2.mp3, song3.mp3, song4.mp3, song5.mp3
```

#### 第 2 步：放入文件夹
```
复制图片到：config/images/
复制音乐到：config/music/
```

#### 第 3 步：编辑 CSV 文件
打开文本编辑器，编辑 `config/decks/deck1.csv`：

```csv
image_name,work_name,songs
card1.png,凛月,song1.mp3|song2.mp3
card2.png,朱樱,song3.mp3
card3.png,深雪,song4.mp3|song5.mp3
card4.png,新緑,song1.mp3|song3.mp3
```

#### 第 4 步：保存并测试
1. 保存文件为 UTF-8 编码
2. 启动应用
3. 选择 deck1
4. 点击开始游戏
5. **完成！** 🎉

---

## 🎮 游戏操作指南

### 卡组选择界面
```
┌──────────────────────────────┐
│ 可用卡组:                     │
│  ☑ deck1 (示例卡组)          │
│  ☐ deck2                     │
│                              │
│ 总回合数: [ 10 ]            │
│                              │
│  [▶ 开始游戏] [✕ 退出]      │
└──────────────────────────────┘
```

**操作**:
1. 用鼠标点击选择卡组
2. 用 +/- 按钮调整回合数
3. 点击 "开始游戏" 启动

### 游戏进行界面
```
回合: 1/10 | 成功: 0 失败: 0

         [卡片图片]
        【卡片名称】
        
    🎵 歌曲播放中...
    [##################  ]

   [✓ 成功]    [✗ 失败]
```

**操作**:
- 📢 听歌曲 10-30 秒
- 👆 点击 "✓ 成功" - 卡片移出（不再出现）
- 👆 点击 "✗ 失败" - 卡片保留（可能再出现）

### 后台管理面板
- 自动打开，显示场上卡片
- 实时显示游戏进度
- 支持手动调整卡片状态

---

## ⚙️ 参数配置

### config.txt 说明

```properties
# 音乐播放时长（秒）
min_duration=10       # 最少播放 10 秒
max_duration=30       # 最多播放 30 秒
                      # 每局会在这个范围内随机

# 中场休息
rest_music=rest.mp3   # 每 5 回合播放的休息音乐
failure_mode=PASS     # PASS=轮空, SKIP=弃牌

# 文件夹路径
music_folder=config/music/
images_folder=config/images/
default_deck=config/decks/deck1.csv
```

### 推荐配置
```properties
# 用于小型集会
min_duration=10
max_duration=20
rest_music=rest.mp3

# 用于大型赛事
min_duration=15
max_duration=30
rest_music=rest.mp3

# 用于初学者
min_duration=20
max_duration=40
rest_music=rest.mp3
```

---

## 🆘 快速故障排查

| 问题 | 原因 | 解决 |
|------|------|------|
| 无法启动 | Java 未安装 | 下载安装 Java 11+ |
| 看不到卡片 | 文件缺失或路径错误 | 检查 config/images/ 名称 |
| 无声 | 音乐缺失或系统静音 | 检查音量，验证文件 |
| CSV 错误 | 格式不正确 | 使用英文逗号，UTF-8 编码 |
| 后台窗口看不到 | 窗口被隐藏 | Alt+Tab 切换窗口 |

---

## 📚 获取帮助

### 文档资源
- 📖 **快速开始**: `QUICKSTART.md`
- 📖 **用户手册**: `USER_MANUAL.md` ⭐ 推荐
- 📖 **完整文档**: `COMPLETE_GUIDE.md`
- 📖 **开发指南**: `docs/DEVELOPMENT.md`
- 📖 **CSV 格式**: `docs/CSV_FORMAT.md`

### 常见问题速查
```
Q: 如何添加更多卡组？
A: 在 config/decks/ 中创建新的 CSV 文件

Q: 如何支持更多格式的音乐？
A: 已支持 MP3/WAV/FLAC/OGG/AAC

Q: 如何修改播放时长？
A: 编辑 config/config.txt 中的时长参数

Q: 如何恢复已出局的卡？
A: 使用后台管理面板的 "清空出局" 功能

Q: 可以在 Android 上运行吗？
A: 目前不行，Android 版本在规划中
```

---

## 💡 使用建议

### 🎯 活动组织
1. **提前测试** - 在真实活动前测试一次
2. **准备备卡** - 准备多个卡组（不同难度）
3. **音量检查** - 提前测试扬声器或耳机
4. **网络可选** - 本地运行，无需网络

### 🎵 音乐准备
1. 建议使用 **MP3 格式**（兼容性最好）
2. 音乐长度 **30-60 秒**（演唱的精段）
3. 音质建议 **128-192 kbps**（平衡质量和文件大小）
4. 文件名避免特殊字符

### 🃏 卡组设计
1. **卡片数量** - 5-50 张最佳
2. **每卡歌曲** - 1-5 首（1 首简单，3-5 首难）
3. **多样化** - 不同难度和风格混搭
4. **命名规范** - 使用有意义的名字

---

## 📊 示例卡组

### 《四季主题》卡组
```csv
image_name,work_name,songs
spring.png,春樱祭典,spring_op.mp3|spring_ed.mp3
summer.png,夏季祭,summer_theme.mp3
autumn.png,秋枫诗社,autumn1.mp3|autumn2.mp3
winter.png,冬雪节,winter1.mp3|winter2.mp3
```

### 《动画角色》卡组
```csv
image_name,work_name,songs
char_a.png,主角,opening.mp3|ending.mp3
char_b.png,配角1,bgm_1.mp3|bgm_2.mp3
char_c.png,配角2,bgm_3.mp3
char_d.png,反派,boss_theme.mp3
```

---

## 🚀 接下来做什么

### 立即开始
1. ✅ 按照上面 "5 分钟快速上手" 操作
2. ✅ 运行应用并体验基本功能
3. ✅ 创建自己的卡组

### 深入了解
1. 📖 阅读 `USER_MANUAL.md` 了解所有功能
2. 📖 查看 `DEVELOPMENT.md` 学习定制开发
3. 📖 研究 `docs/CSV_FORMAT.md` 掌握高级配置

### 扩展和分享
1. 🎨 设计自己的主题卡组
2. 🎵 创建多个不同风格的卡组
3. 📱 关注 Phase 2.0（网络对战、排行榜等）

---

## ✨ 项目特色

### 🎮 游戏体验
- 实时音乐播放和统计
- 流畅的 UI 和响应
- 后台状态监控
- 防止误操作

### 🛠️ 易于定制
- 简单的 CSV 配置
- 灵活的参数调整
- 支持中文输入
- 清晰的文件结构

### 📈 可扩展架构
- 模块化设计
- 预留 Android 接口
- 支持网络扩展
- 易于功能添加

---

## 🎊 祝贺！

您现在已经拥有一个完整的、可用的歌牌点歌系统！

### 下一步行动
📋 **第 1 步**: 准备您的媒体文件（音乐 + 图片）  
📋 **第 2 步**: 编辑 CSV 卡组配置  
📋 **第 3 步**: 启动应用并测试  
📋 **第 4 步**: 享受游戏！🎵

---

**版本**: 1.0.0  
**状态**: ✅ 就绪使用  
**更新**: 2024年3月31日

---

👋 **感谢使用 Karuta Jukebox！**

有任何问题，请查看项目中的详细文档。

祝您的事件圆满成功！🎉

