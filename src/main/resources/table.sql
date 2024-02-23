-- 用户表
CREATE TABLE User (
                      id INT PRIMARY KEY AUTO_INCREMENT, -- 用户ID，主键，自增
                      Username VARCHAR(255) NOT NULL, -- 用户名，不为空
                      Password VARCHAR(255) NOT NULL, -- 密码，不为空
                      UserType INT NOT NULL DEFAULT 0 -- 用户类型，0表示普通用户，1表示管理员用户，默认为0
);

-- 歌曲表
CREATE TABLE music(
                      id INT PRIMARY KEY AUTO_INCREMENT, -- 歌曲ID，主键，自增
                      Title VARCHAR(255) NOT NULL, -- 标题，不为空
                      singer VARCHAR(255) NOT NULL, -- 作者，不为空
                      UploadDate DATE NOT NULL, -- 上传日期，不为空
                      url VARCHAR(255) NOT NULL, -- 音乐文件路径，不为空
                      UploaderID INT NOT NULL, -- 上传者ID，不为空
                      Likes INT DEFAULT 0 -- 点赞数，默认为0
);

-- 收藏表
CREATE TABLE lovemusic(
                          id INT PRIMARY KEY AUTO_INCREMENT, -- 收藏ID，主键，自增
                          user_id INT NOT NULL, -- 用户ID，不为空
                          music_id INT NOT NULL -- 歌曲ID，不为空
);

-- 评论表
CREATE TABLE Comment (
                         id INT PRIMARY KEY AUTO_INCREMENT, -- 评论ID，主键，自增
                         user_id INT NOT NULL, -- 用户ID，不为空
                         music_id INT NOT NULL, -- 歌曲ID，不为空
                         Content TEXT NOT NULL, -- 评论内容，不为空
                         CommentDate DATETIME NOT NULL -- 评论日期时间，不为空
);

-- 音乐活动轮播表
CREATE TABLE MusicEventCarousel (
                                    id INT PRIMARY KEY AUTO_INCREMENT, -- 图片ID，主键，自增
                                    even_id INT NOT NULL, -- 活动ID，不为空
                                    ImagePath VARCHAR(255) NOT NULL, -- 图片路径，不为空
                                    ImageLink VARCHAR(255) NOT NULL -- 图片链接，不为空
);

-- 活动表
CREATE TABLE MusicEvent (
                            id INT PRIMARY KEY AUTO_INCREMENT, -- 活动ID，主键，自增
                            EventName VARCHAR(255) NOT NULL, -- 活动名称，不为空
                            EventDate DATE NOT NULL, -- 活动日期，不为空
                            Location VARCHAR(255) NOT NULL, -- 活动地点，不为空
                            Description TEXT NOT NULL, -- 活动描述，不为空
                            TargetWebsiteLink VARCHAR(255) NOT NULL -- 目标网站链接，不为空
);