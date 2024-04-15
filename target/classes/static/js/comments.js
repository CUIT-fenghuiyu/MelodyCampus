// 获取URL中的参数值
function getURLParam(key){
    var params = location.search;
    if(params.indexOf("?")>=0){
        params = params.substring(1);
        var namevalues = params.split("=");
        if(namevalues[0]==key){
            return namevalues[1];
        }
    }else{
        return "";
    }
}

// 获取歌曲信息
function loadMusicInfo(musicId) {
    $.ajax({
        url: "http://localhost:8080/music/getMusicInfo",
        type: "GET",
        data: {
            id: musicId
        },
        success: function (result) {
            if (result.status == 0) {
                var music = result.data;

                $(".song-info h3").text(music.title);
                $(".song-info h4").text(music.singer);

                // 获取播放按钮元素
                var playButton = document.querySelector(".play-button button");

                // 添加点击事件处理程序
                playButton.onclick = function() {
                    // 按钮点击后的操作
                    playMusic(music.url+".mp3", music.title, music.singer);
                };

            }else {
                alert(result.message);
            }
        }
    })
}

// 提交评论
function submitComment() {
    var musicId = getURLParam("id");
    var content = $(".form-control").val();

    // 判断评论内容是否为空
    if (content == "") {
        alert("评论内容不能为空");
        return;
    }

    if (content.length > 100) {
        alert("评论内容不能超过100个字符");
        return;
    }

    $.ajax({
        url: "http://localhost:8080/comment/addComment",
        type: "POST",
        data: {
            musicId: musicId,
            content: content
        },
        success: function (result){
            if (result.status == 0) {
                alert("评论成功");
            }else {
                alert("评论失败");
            }
            // 清空评论框
            $(".form-control").val("");

            loadCommentList(musicId);
            console.log(result.message);
        }
    })
}


function deleteComment(commentId){
    if (!confirm("确定删除该评论吗？")) {
        return;
    }

    $.ajax({
        url: "http://localhost:8080/comment/deleteComment",
        type: "GET",
        data: {
            id: commentId
        },
        success: function (result) {
            console.log(result.message);

            if (result.status == 0) {
                alert("删除成功");
            }else {
                alert("删除失败");
            }
            loadCommentList(getURLParam("id"));
        }
    })
}

/*使用postMessage API在主页面和iframe之间传递消息。*/
function playMusic(url, name, artist){
    var playerIframe = document.getElementById('musicPlayer');
    playerIframe.contentWindow.postMessage({
        action: 'play',
        url: url,
        name: name,
        artist: artist
    }, window.location.origin);

    console.log("playMusic：");
    console.log(url, name, artist);
};
