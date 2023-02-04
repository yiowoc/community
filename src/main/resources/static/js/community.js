function postComment() {
    var parentId = $("#question-comment-id").val();
    var conetent = $("#question-comment-content").val();
    if(!conetent) {
        alert("输入内容不能为空哦~");
        return;
    }
    $.ajax({
        url: "/comment",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "parentId": parentId,
            "content": conetent,
            "type": 1
        }),
        success: function(res) {
            if(res.code == 200) {
                window.location.reload();
                // $("#question-comment-section").hide();
            } else {
                if(res.code == 2003) {
                    var confired = confirm(res.message);
                    if(confired) {
                        window.open("https://github.com/login/oauth/authorize?client_id=e674c1ab3b3235e81279&redirect_uri=http://localhost:8887/callback&scope=user");
                        localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(res.message);
                }
            }
        }
    })
}