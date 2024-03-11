
    /*加载所有歌曲信息*/
    function loadMusic(musicName){
    $.ajax({
        url: "/music/findMusic",
        type: "GET",
        data: {
            "musicName": musicName
        },
        success: function (result) {
            console.log("loadMusic：");
            console.log(result);

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
                    html += "<button class='btn btn-secondary' onclick='musicMark("+music.id+")'><i class='fas fa-heart' data-song-id="+ music.id +"></i></button>";
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

    /*根据歌名查询歌曲信息*/
    function selectByMusicName() {
    var musicName = $(".search-input").val();
    console.log("selectByMusicName：");
    console.log(musicName);
    loadMusic(musicName);
}

    /* 音乐收藏/取消收藏功能 */
    function musicMark(id) {
    $.ajax({
        url: "/lovemusic/likeMusic",
        type: "POST",
        data: {
            "musicId": id
        },
        success: function (result) {
            console.log("musicMark：");
            console.log(result);
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

                }
            })

        }
    });
}

    /*更新音乐列表后，对被收藏的音乐更新UI */
    function updateLikeStatus() {
    $.ajax({
        url: "/lovemusic/getLoveMusicIdList",
        type: "GET",
        success: function(result) {
            console.log("updateLikeStatus：");
            console.log(result);
            // 为每个歌曲Id添加喜欢样式
            var musicIdList = result.data;
            for (var i = 0; i < musicIdList.length; i++) {
                var id = musicIdList[i];
                $("i[data-song-id='" + id + "']").addClass("liked");
            }

        }
    });
}

    /*上传音乐功能*/
    $(function() {
    $('.submit-btn').on('click', function(e) {
        // 阻止表单默认提交事件
        e.preventDefault();

        var fileInput = $('.upload-btn')[0]; // 获取文件输入元素
        var singerInput = $('.singer-input').val(); // 获取并清除歌手输入框两侧的空格

        // 检查是否已选择文件
        if (!fileInput.files.length) {
            alert("请上传音乐文件！");
            return;
        }

        // 获取文件并检查类型
        var file = fileInput.files[0];
        if (file.type !== 'audio/mpeg') {
            alert("仅支持MP3格式的音乐文件！");
            return;
        }

        // 检查是否已填写歌手名称
        if (!singerInput) {
            alert("请填写歌手名称！");
            return;
        }

        // 进行音乐提交
        console.log("文件和歌手名称验证通过");
        // 使用 FormData 进行音乐提交
        var formData = new FormData();
        formData.append('singer', singerInput);  // 添加歌手名称
        formData.append('filename', file);       // 添加文件
        // 音乐提交逻辑...
        $.ajax({
            url: "/music/upload",
            type: "POST",
            data: formData,
            processData: false,  // 告诉jQuery不要处理发送的数据
            contentType: false,  // 告诉jQuery不要设置Content-Type请求头
            success: function (result) {
                console.log("uploadMusic：");
                console.log(result);
                if (result.status == 0) {
                    alert("上传成功！");

                    loadMusic();

                    /*清除文件和歌手名称输入框中的内容*/
                    $('.action-buttons .upload-btn').val('');
                    $('.singer-input').val('');
                } else {
                    console.log(result.msg);
                    alert("上传失败："+result.message);
                }
            }
        })
    });
});

    /*使用postMessage API在主页面和iframe之间传递消息。*/
    function playMusic(url, name, artist){
    var playerIframe = document.getElementById('musicPlayer');
    playerIframe.contentWindow.postMessage({
    action: 'play',
    url: url,
    name: name,
    artist: artist
}, window.location.origin);
    /*定位到音乐播放器*/
    window.location.href = "#musicPlayer";

    console.log("playMusic：");
    console.log(url, name, artist);
};
