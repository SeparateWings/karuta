# CSV 配置文件格式说明

## 卡组配置文件格式

### 文件名
`deck_{name}.csv` - 例如：`deck1.csv`、`deck_spring.csv`

### 格式定义

```csv
image_name,work_name,songs
```

| 列名 | 数据类型 | 说明 | 示例 |
|------|---------|------|------|
| `image_name` | String | 卡牌对应的图片文件名（含扩展名） | `card1.png`、`character_A.jpg` |
| `work_name` | String | 作品名称或卡牌名称 | `作品A`、`角色B` |
| `songs` | String | 同一张卡对应的多首歌曲，使用 `\|` 分隔 | `song1.mp3\|song2.mp3\|song3.mp3` |

### 完整示例

```csv
image_name,work_name,songs
card_autumn.png,秋风诗社,autumn_song1.mp3|autumn_song2.mp3|autumn_song3.mp3
card_spring.png,春樱祭典,spring_main.mp3
card_summer.png,夏季祭,summer_opening.mp3|summer_theme.mp3
card_winter.png,冬雪节,winter1.mp3|winter2.mp3|winter3.mp3|winter4.mp3
```

### 注意事项

1. **文件编码**：UTF-8（支持中文）
2. **分隔符**：英文逗号 `,`（不要使用中文逗号）
3. **歌曲分隔**：英文竖线 `|`（不要使用中文竖线）
4. **文件路径**：仅需填写文件名，不需要完整路径
   - ✅ 正确：`song.mp3`
   - ❌ 错误：`config/music/song.mp3`
5. **文件必须存在**：
   - 所有图片必须存在于 `images/` 文件夹
   - 所有音乐文件必须存在于 `music/` 文件夹
6. **特殊字符**：如工作名称含有逗号或特殊字符，应用双引号包围
   - 例如：`"card.png","作品""A""（第1代）",song.mp3`

## 主配置文件格式 (config.txt)

```properties
# 音乐文件夹（相对于karuta文件夹）
music_folder=config/music/

# 图片文件夹
images_folder=config/images/

# 中场休息音乐文件名
rest_music=rest.mp3

# 歌曲播放时长范围（秒）
min_duration=10
max_duration=30

# 默认卡组文件
default_deck=config/decks/deck1.csv

# 游戏规则配置
# 对局失败的处理方式: PASS(轮空) 或 SKIP(弃牌)
failure_mode=PASS

# 是否启用中场休息
enable_rest_music=true

# 中场休息后是否自动继续
auto_continue_after_rest=false
```

## 文件夹组织示例

```
config/
├── music/                          # 所有音乐文件
│   ├── autumn_song1.mp3
│   ├── autumn_song2.mp3
│   ├── spring_main.mp3
│   ├── rest.mp3
│   └── ...
├── images/                         # 所有卡片图片
│   ├── card_autumn.png
│   ├── card_spring.png
│   ├── card_summer.png
│   ├── card_winter.png
│   └── ...
└── decks/                          # 卡组配置文件
    ├── deck1.csv                   # 主卡组
    ├── deck_casual.csv             # 休闲卡组
    └── deck_tournament.csv         # 竞赛卡组
```

## 数据验证规则

程序加载时会进行以下验证：

1. ✅ 检查CSV文件格式是否正确
2. ✅ 验证所有图片文件是否存在
3. ✅ 验证所有音乐文件是否存在
4. ✅ 检查是否有重复的卡牌（根据 image_name）
5. ✅ 验证音乐文件格式是否支持
6. ⚠️ 如有错误，将输出详细的错误报告

## 错误处理

- **图片不存在**：在UI中显示占位符，日志记录错误
- **音乐文件不存在**：跳过该歌曲，日志记录错误
- **格式不支持**：显示警告，尝试使用备选格式播放
- **配置文件缺失**：使用默认配置，输出警告信息

