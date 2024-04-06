/*加载所有歌曲信息*/
function loadAllMusic(musicName){
    $.ajax({
        url: "/music/findMusic",
        type: "GET",
        data: {
            "musicName": musicName
        },
        success: function (result) {
            console.log("loadAllMusic：");
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
                    html += "<button class='btn btn-info'>查看</button>";
                    html += '<button class="btn btn-outline-danger" onclick="deleteMusic('+ music.id+ ')"><i class="fas fa-trash-alt"></i> 删除</button>'
                    html += "</div>";
                    html += "</td>";
                    html += "</tr>";
                }
            }
            $("#musicList").html(html);
        }
    });
}

/* 获取轮播图*/
function getCarousel() {
    $.ajax({
        url: '/musicevent/carousel',
        type: 'GET',
        success: function (response) {
            console.log(response);
            var html = "";
            if (response.status === 0 && response.data.length > 0) {
                for (var i = 0; i < response.data.length; i++) {
                    var item = response.data[i];
                    var activeClass = i === 0 ? " active" : "";
                    var imageUrl = 'localhost:8080' + item.imageUrl;
                    var decodedImageUrl = decodeURIComponent(item.imageUrl);
                    html += "<div class='carousel-item" + activeClass + "'>";

                    console.log(decodedImageUrl);

                    // 将链接指向新的 /musicevent/getImageFile 端点,并传递请求路径
                    html += "<a href='/musicevent/getImageFile?path=" + decodedImageUrl.split("=")[1] + "' target='_blank'>";

                    // 这里,我们为 <img> 标签添加内联 CSS 以设置宽度和高度。
                    html += "<img class='d-block w-100' src='" + decodedImageUrl + "' alt='" + item.id + "' style='width: 800px; height: 200px; object-fit: cover;'>";
                    html += "</a>";
                    html += "</div>";
                }
            }
            $('.carousel-inner').html(html);
        },
        error: function (xhr, status, error) {
            console.error('Error fetching carousel data:', error);
        }
    });
}

/*根据歌名查询歌曲信息*/
function selectByMusicName() {
    var musicName = $(".search-input").val();
    console.log("selectByMusicName：");
    console.log(musicName);
    loadAllMusic(musicName);
}

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
                loadAllMusic(); // 重新加载音乐列表
            }
        });
    }

}

// 删除选中的音乐按钮点击事件
$(function() {
    $('.action-buttons .btn-outline-danger').on('click',  function() {
        var selectedIds = [];
        console.log(selectedIds);
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
                loadAllMusic(); // 重新加载音乐列表
            } else {
                alert("删除失败：" + result.message);
            }
        }
    });
}

// 为按钮设置点击事件监听器
$(function() {
    /*歌曲管理*/
    $(".top-right-buttons .btn-outline-secondary").click(function() {
        loadAllMusic();
        getCarousel();

        // 显示查询组件、删除选中音乐按钮、轮播图
        $(".search-bar").show();
        $(".action-buttons .btn-outline-danger").show();
        $(".carousel-container").show();

        // 绑定查询按钮监听事件
        $(".search-bar .btn-primary").on('click', selectByMusicName);

        $(".top-right-buttons .btn-outline-secondary").addClass("clicked");

    });

    /*账号管理*/
    $(".top-right-buttons .btn-outline-primary").click(function() {
        loadAllUser();

        // 隐藏查询组件
        $(".search-bar").hide();

        // 隐藏删除选中音乐按钮
        $(".action-buttons .btn-outline-danger").hide();

        // 隐藏轮播图
        $(".carousel-container").hide();


        // 初始化模态框
        $('#updatePasswordModal').modal({
            // 不需要初始化模态框,使用默认设置
        });
    });

});

/*点击右上角按钮后的效果*/
$(function() {
    // 当两个个按钮中的任何一个被点击时
    $(".top-right-buttons .btn-outline-secondary, .top-right-buttons .btn-outline-primary").click(function() {
        // 首先，移除所有按钮上的.clicked类
        $(".top-right-buttons .btn-outline-secondary, .top-right-buttons .btn-outline-primary").removeClass("clicked");

        // 然后，给当前被点击的按钮添加.clicked类
        $(this).addClass("clicked");
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

/*查询所有的用户*/
function loadAllUser(){
    $.ajax({
        url: "/user/all",
        type: "GET",
        success: function (result) {
            console.log("loadAllUser：");
            console.log(result);

            // 获取所有th元素，并修改相应文本
            var ths = document.querySelectorAll('.content-table th');
            ths[1].textContent='账号';
            ths[2].textContent='密码';
            ths[3].textContent='角色类型';

            var html = "";
            if (result.status == 0){
                var userList = result.data;

                for (var i = 0; i < userList.length; i++) {
                    var user = userList[i];
                    html += "<tr>";
                    html += "<td><input type='checkbox' data-user-id=" + user.id + "></td>";
                    html += "<td>" + user.username + "</td>";
                    html += "<td>*********</td>";

                    var role="";
                    if (user.userType == 0){
                        role="普通用户";
                    }else if (user.userType == 1){
                        role="管理员";
                    }else if (user.userType == 2){
                        role="超级管理员";
                    }
                    html += "<td>" + role + "</td>";
                    html += "<td>";
                    html += "<div class='btn-group'>";
                    html += "<button class='btn btn-primary' onclick='updatePWD(" + user.id + ")'> 修改密码</button>";
                    html += "<button class='btn btn-danger' onclick='deleteUser(" + user.id + ")'> 删除账号</button>";
                    html += "</div>";
                    html += "</td>";
                    html += "</tr>";
                }

                $("#musicList").html(html);
            }
        }
    });
}

// 显示模态框并设置用户ID
function updatePWD(userId){
    // 设置修改密码模态框的用户ID
    $('#userId').val(userId);
    // 显示模态框
    $('#updatePasswordModal').modal('show');
}


// 提交修改密码请求
function submitUpdatePassword() {
    var userId = $('#userId').val();
    var newPassword = $('#newPassword').val();
    var confirmPassword = $('#confirmPassword').val();

    // 验证新密码是否为空
    if (newPassword == '') {
        alert('密码输入不能为空,请重新输入');
        return;
    }

    // 验证新密码和确认密码是否一致
    if (newPassword !== confirmPassword) {
        alert('密码输入不一致,请重新输入');
        return;
    }

    $.ajax({
        url: '/user/updatePassword',
        type: 'POST',
        data: { userId: userId, newPassword: newPassword },
        success: function(result) {
            if (result.status === 0) {
                alert('密码修改成功');
                $('#updatePasswordModal').modal('hide');
                // 刷新用户列表
                loadAllUser();
            } else {
                alert('密码修改失败: ' + result.message);
            }
        },
        error: function() {
            alert('发生错误,请重试');
        }
    });
}

// 删除用户账号
function deleteUser(id){
    if (confirm("确认删除该账号？")){
        $.ajax({
            url: "/user/delete",
            type: "POST",
            data: {
                "id": id
            },
            success: function (result) {
                console.log("deleteUser：");
                console.log(result);

                if (result.status == 0){
                    alert("删除成功");
                }else {
                    alert("删除失败：" + result.message);
                }
                console.log(sessionStorage.getItem("user"))
                // 如果删除用户是登录用户，跳转到登录页面
                if (id == JSON.parse(sessionStorage.getItem("user")).id){
                    location.href = "/login.html";
                }
                loadAllUser();
            }
    })}else {
        return;
    }
}