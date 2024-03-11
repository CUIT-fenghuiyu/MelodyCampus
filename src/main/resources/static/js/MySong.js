/*加载用户上传的歌曲信息*/
function loadMyMusic(musicName){
    $.ajax({
        url: "/music/findMyMusic",
        type: "GET",
        data: {
            "musicName": musicName
        },
        success: function (result) {
            console.log("loadMyMusic：");
            console.log(result);

            var html = "";
            if (result.status == 0) {
                var musicList = result.data;

                for (var i = 0; i < musicList.length; i++) {
                    var music = musicList[i];
                    html += "<tr>";
                    html += "<td><input type='checkbox' data-music-id=" + music.id + "></td>";
                    html += "<td>" + music.title + "</td>";
                    html += "<td>" + music.singer + "</td>";
                    html += "<td><button class='btn btn-success' onclick='playMusic(\""+ music.url +".mp3" +"\",  \"" + music.title + "\", \"" + music.singer + "\")'>播放</button></td>";
                    html += "<td>";
                    html += "<div class='btn-group'>";
                    html += "<button class='btn btn-secondary' onclick='musicMarkMySong("+music.id+")'><i class='fas fa-heart' data-song-id="+ music.id +"></i></button>";
                    html += "<button class='btn btn-info'>查看</button>";
                    html += '<button class="btn btn-outline-danger" onclick="deleteMusic('+ music.id+ ')"><i class="fas fa-trash-alt"></i> 删除</button>'
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

/*根据歌名查询歌曲信息*/
function selectMySongByMusicName() {
    var musicName = $(".search-input").val();
    console.log("selectMySongByMusicName：");
    console.log(musicName);
    loadMyMusic(musicName);
}

/* 音乐收藏/取消收藏功能 */
function musicMarkMySong(id) {
    $.ajax({
        url: "/lovemusic/likeMusic",
        type: "POST",
        data: {
            "musicId": id
        },
        success: function (result) {
            console.log(result)
            // 检查音乐是否被收藏
            $.ajax({
                url: "/lovemusic/getLoveMusicIdList",
                type: "get",
                success: function (result) {
                    console.log("musicMarkMySong：");
                    console.log(result)
                    // 判断音乐ID是否在收藏列表中
                    if (result.data.indexOf(id) != -1) {
                        $("i[data-song-id='" + id + "']").addClass("liked");
                    } else {
                        $("i[data-song-id='" + id + "']").removeClass("liked");
                    }

                }
            })

        }
    });
}

/*/!*更新音乐列表后，对被收藏的音乐更新UI *!/
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

// 删除音乐按钮点击事件
function deleteMusic(id){
    // 确认删除
    if (confirm("确认删除？")) {
        $.ajax({
            url: "/music/delete",
            type: "POST",
            data: {
                "id": id
            },
            success: function(result) {
                console.log("deleteMusic：");
                console.log(result);
                if (result.status == 0) {
                    alert("删除成功");
                } else {
                    alert("删除失败：" + result.message);
                }
                loadMyMusic(); // 重新加载音乐列表
            }
        });
    }

}

// 删除选中的音乐按钮点击事件
$(function() {
    $('.action-buttons .btn-outline-danger').on('click',  function() {
        var selectedIds = [];
        $("input[type='checkbox']:checked").each(function() {
            selectedIds.push($(this).data('music-id'));
        });

        if (selectedIds.length > 0) {
            if (confirm("确认删除所选音乐？")) {
                deleteSelectedMusics(selectedIds);
            }
        } else {
            alert("请至少选择一项进行删除");
        }
    });
});

// 发送删除请求的函数
function deleteSelectedMusics(musicIds) {
    console.log(musicIds);
    $.ajax({
        url: "/music/deleteSel",
        type: "POST",
        data: {
            "id[]": musicIds
        },
        traditional: true,
        success: function(result) {
            console.log("deleteSelectedMusics：");
            console.log(result);
            if (result.status == 0) {
                alert("删除成功");
                loadMyMusic(); // 重新加载音乐列表
            } else {
                alert("删除失败：" + result.message);
            }
        }
    });
}
