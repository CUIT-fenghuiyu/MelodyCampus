/*加载所有歌曲信息*/
function loadLoveMusic(musicName){
    $.ajax({
        url: "/lovemusic/findLoveMusic",
        type: "GET",
        data: {
            "musicName": musicName
        },
        success: function (result) {
            console.log("loadLoveMusic：");
            console.log(result)

            var html = "";
            if (result.status == 0) {
                var musicList = result.data;

                for (var i = 0; i < musicList.length; i++) {
                    var music = musicList[i];
                    html += "<tr>";
                    html += "<td><input type='checkbox'></td>";
                    html += "<td>" + music.title + "</td>";
                    html += "<td>" + music.singer + "</td>";
                    html += "<td><button class='btn btn-success' onclick='playMusic(\""+ music.url +".mp3" +"\",  \"" + music.title + "\", \"" + music.singer + "\")'>播放</button></td>";
                    html += "<td>";
                    html += "<div class='btn-group'>";
                    html += "<button class='btn btn-secondary' onclick='musicMarkMyFavorite("+music.id+")'><i class='fas fa-heart' data-song-id="+ music.id +"></i></button>";
                    html += "<button class='btn btn-info'>查看</button>";
                    html += "</div>";
                    html += "</td>";
                    html += "</tr>";
                }
            }
            $("#musicList").html(html);

            /*更新音乐列表后，对每首歌曲检查喜好状态*/
            updateLikeStatus();

        }
    });
}

/* 音乐收藏/取消收藏功能 */
function musicMarkMyFavorite(id) {
    $.ajax({
        url: "/lovemusic/likeMusic",
        type: "POST",
        data: {
            "musicId": id
        },
        success: function (result) {
            console.log("musicMarkMyFavorite：");
            console.log(result)
            // 检查音乐是否被收藏
            $.ajax({
                url: "/lovemusic/getLoveMusicIdList",
                type: "get",
                success: function (result) {
                    console.log(result)
                    // 判断音乐ID是否在收藏列表中
                    if (result.data.indexOf(id) != -1) {
                        $("i[data-song-id='" + id + "']").addClass("liked");
                    } else {
                        $("i[data-song-id='" + id + "']").removeClass("liked");
                    }

                    loadLoveMusic();
                }
            })

        }
    });
}

/*根据歌名查询歌曲信息*/
function selectMyLoveByMusicName() {
    var musicName = $(".search-input").val();
    console.log("selectMyLoveByMusicName：");
    console.log(musicName);
    loadLoveMusic(musicName);
}

/*
/!*更新音乐列表后，对被收藏的音乐更新UI *!/
function updateLikeStatus() {
    $.ajax({
        url: "/lovemusic/getLoveMusicIdList",
        type: "GET",
        success: function(result) {
            console.log(result);
            // 为每个歌曲Id添加喜欢样式
            var musicIdList = result.data;
            for (var i = 0; i < musicIdList.length; i++) {
                var id = musicIdList[i];
                $("i[data-song-id='" + id + "']").addClass("liked");
            }

        }
    });
}*/
