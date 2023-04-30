<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>파일 목록</title>
    <style>
        td{
            vertical-align: middle;
        }
        .file-preview:hover{
            text-decoration: underline;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    <div class="container">
        <div class="d-flex justify-content-center align-items-center" style="min-height: 700px;">
            <div class="card border-0 rounded-2 shadow-lg my-4" style="width: 80%; height: 60%; min-width: 660px;">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h2>파일 목록</h2>
                    <a href="/file/upload" class="btn btn-primary" role="button">업로드</a>
                </div>
                <div class="card-body">
                    <p><strong>경로 : </strong>/</p>
                    <table class="table table-bordered text-center">
                        <thead>
                            <tr>
                                <th style="width: 10%;">번호</th>
                                <th style="width: 45%;">이름</th>
                                <th style="width: 25%;">업로드 날짜</th>
                                <th style="width: 20%;"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="fileList" value="${requestScope.pageInfo.contentList}" />
                            <c:choose>
                                <c:when test="${fileList != null and fileList.size() > 0}">
                                    <c:forEach var="file" items="${fileList}">
                                        <tr class="fileRow">
                                            <td class="fileId">${file.fileId}</td>
                                            <td class="filename">${file.originalName}</td>
                                            <td>${file.createdAt}</td>
                                            <td>
                                                <button type="button" class="btn btn-success my-1 downloadBtn">다운</button>
                                                <button type="button" class="btn btn-primary my-1 updateBtn">수정</button>
                                                <button type="button" class="btn btn-danger my-1 deleteBtn">삭제</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="4">업로드된 파일이 존재하지 않습니다.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                    <div class="d-flex justify-content-center align-items-center">
                        <c:set var="page" value="${requestScope.pageInfo}" />
                        <ul class="pagination">
                            <c:if test="${page.startPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="/file/pages/${startPage - 1}" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>
                            <c:forEach var="num" begin="${page.startPage}" end="${page.endPage}">
                                <li class="page-item">
                                    <a class="page-link <c:if test='${page.currentPage == num}'>active</c:if>" href="/file/pages/${num}">${num}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${page.endPage < page.totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="/file/pages/${endPage + 1}" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <form name="updateForm" action="/file/update" method="post">
        <input type="hidden" name="fileId" />
        <input type="hidden" name="originalName" />
    </form>
    <form name="deleteForm" action="/file/delete" method="post">
        <input type="hidden" name="fileId" />
    </form>
    <div id="previewModal" class="modal fade" data-bs-backdrop="static" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">미리보기</h5>
                    <button type="button" class="btn-close" onclick="closePreview();" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="d-flex justify-content-center align-items-center">
                        <image id="imagePreview" src="" class="d-none preview-control" style="max-width: 100%; max-height: 100%;"></image>
                        <audio id="audioPreview" src="" class="d-none preview-control" controls></audio>
                        <video id="videoPreview" src="" class="d-none preview-control" controls style="max-width: 100%; max-height: 100%;"></video>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closePreview();">닫기</button>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    const filename = document.querySelectorAll('.filename');
    const updateForm = document.updateForm;
    const deleteForm = document.deleteForm;
    const downloadBtn = document.querySelectorAll('.downloadBtn');
    const updateBtn = document.querySelectorAll('.updateBtn');
    const deleteBtn = document.querySelectorAll('.deleteBtn');
    const filePreview = [];

    const previewModal = new bootstrap.Modal(document.querySelector('#previewModal'), {});
    const previewControl = document.querySelectorAll('.preview-control');
    const imagePreview = document.querySelector('#imagePreview');
    const audioPreview = document.querySelector('#audioPreview');
    audioPreview.volume = 0.5;
    const videoPreview = document.querySelector('#videoPreview');
    videoPreview.volume = 0.5;

    const imageList = ['jpg', 'jpeg', 'png'];
    const audioList = ['mp3', 'wav'];
    const videoList = ['mp4'];
    const formatList = [...imageList, ...audioList, ...videoList];

    filename.forEach(function(td){
        const originalName = td.textContent;
        const formatType = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();

        if(formatList.includes(formatType)){
            td.classList.add('file-preview');
            filePreview.push(td);
        }
        
        if(imageList.includes(formatType)){
            td.classList.add('image-preview');
        }
        if(audioList.includes(formatType)){
            td.classList.add('audio-preview');
        }
        if(videoList.includes(formatType)){
            td.classList.add('video-preview');
        }

    });

    downloadBtn.forEach(function(btn){
        btn.onclick = function(){
            const fileId = getFileId(this);
            location.href = '/file/download/' + fileId;
        }
    });

    updateBtn.forEach(function(btn){
        btn.onclick = function(){
            const oldFilename = getFilenameWithoutFormat(this);
            let newFilename = prompt('새 파일명을 입력해주세요.', oldFilename).trim();

            if(newFilename != null && newFilename != ''){
                const fileId = getFileId(this);
                updateForm.fileId.value = fileId;
                updateForm.originalName.value = newFilename;
                updateForm.submit();
                return;
            }

            alert('파일명을 입력해주세요.');
            return;
        }
    });

    deleteBtn.forEach(function(btn){
        btn.onclick = function(){
            if(!confirm('파일을 삭제하시겠습니까?')) return;

            const fileId = getFileId(this);
            deleteForm.fileId.value = fileId;
            deleteForm.submit();
        }
    });

    filePreview.forEach(function(target){
        target.onclick = function(){
            const fileId = this.closest('.fileRow').querySelector('.fileId').textContent;
            const formatType = this.classList;
            fetch('/file/' + fileId)
                .then(res => {
                    if(!res.ok) throw new Error();
                    return res.text();
                })
                .then(filePath => {
                    hidePreviewControl();
                    if(formatType.contains('image-preview')){
                        imagePreview.src = filePath;
                        imagePreview.classList.remove('d-none');
                    }
                    if(formatType.contains('audio-preview')) {
                        audioPreview.src = filePath;
                        audioPreview.classList.remove('d-none');
                    }
                    if(formatType.contains('video-preview')) {
                        videoPreview.src = filePath;
                        videoPreview.classList.remove('d-none');
                    }
                    previewModal.show();
                })
                .catch(() => alert('잠시 후 다시 시도해주세요.'));
        }
    });

    function getFileId(target){
        return target.closest('.fileRow').querySelector('.fileId').textContent;
    }

    function getFilenameWithoutFormat(target){
        let filename = target.closest('.fileRow').querySelector('.filename').textContent;
        filename = filename.substring(0, filename.lastIndexOf('.'));
        return filename;
    }

    function hidePreviewControl(){
        previewControl.forEach(function(control){
            control.classList.add('d-none');
            control.src = '';
        })
    }

    function closePreview(){
        hidePreviewControl();
        previewModal.hide();
    }

</script>
</html>