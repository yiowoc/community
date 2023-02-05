/**
 * 发送评论
 */
function postComment() {
    var parentId = $("#question-comment-id").val();
    var content = $("#question-comment-content").val();
    postCommentAjax(parentId, content, 1);
}

function postSubComment(e) {
    var parentId = e.getAttribute("data-id");
    var content = $("#sub-comment-" + parentId).val();
    postCommentAjax(parentId, content, 2);
}

function postCommentAjax(parentId, content, type) {
    if(!content) {
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
            "content": content,
            "type": type
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
    });
}

/**
 * 展示二级评论
 */
function collapseComments(e) {
    e.classList.toggle("active");
    var id = e.getAttribute("data-id");
    var subComment = $("#reply-" + id);
    if(!subComment.hasClass("in")) {
        if(subComment.children.length == 2) {
            $.getJSON("/comment/" + id, function(res) {
                $.each(res.data.reverse(), function (index, commentDTO) {
                    var mediaLeftDiv = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": commentDTO.user.avatarUrl
                    }));
                    var mediaBodyDiv = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": commentDTO.user.name
                    })).append($("<div/>", {
                        "html": commentDTO.content
                    })).append($("<div/>", {
                        "class": "question-reply-menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(commentDTO.gmtCreate).format("YYYY-MM-DD")
                    })));
                    var mediaDiv = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftDiv).append(mediaBodyDiv);
                    subComment.prepend(mediaDiv);
                });
                // console.log(res);
            });
        }
    }
    subComment.toggleClass("in");
}