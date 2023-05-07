<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>파일 업로드</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    <div class="container">
        <div class="d-flex justify-content-center align-items-center" style="min-height: 700px;">
            <div class="card border-0 rounded-2 shadow-lg my-4" style="width: 70%; height: 60%; min-width: 660px;">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h2>파일 업로드</h2>
                    <a href="/file" class="btn btn-secondary" role="button">목록으로</a>
                </div>
                <div class="card-body">
                    <div id="uploadProgress" class="progress d-none" role="progressbar" style="height: 25px;">
                        <div id="uploadProgressBar" class="progress-bar progress-bar-striped progress-bar-animated bg-success" style="width: 0%;">0%</div>
                      </div>
                    <form name="uploadForm">
                        <div class="text-center">
                            <div class="d-flex justify-content-center align-items-center">
                                <input type="file" id="uploadFile" name="uploadFile" class="form-control my-3 w-50" multiple />
                            </div>
                            <input type="hidden" name="dirId" value="${requestScope.dirId}" />
                            <button type="button" id="uploadFileBtn" class="btn btn-primary w-25">업로드</button>
                        </div>
                    </form>
                    <hr />
                    <table class="table table-bordered text-center">
                        <thead>
                            <tr>
                                <th style="width: 30%;">순번</th>
                                <th style="width: 50%;">이름</th>
                                <th style="width: 20%;"></th>
                            </tr>
                        </thead>
                        <tbody id="selectedFileList">
                            <tr>
                                <td colspan="3">파일을 선택해주세요.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    const uploadProgress = document.querySelector('#uploadProgress');
    const uploadProgressBar = document.querySelector('#uploadProgressBar');
    const uploadForm = document.uploadForm;
    const uploadFile = document.querySelector('#uploadFile');
    const selectedFileList = document.querySelector('#selectedFileList');
    const uploadFileBtn = document.querySelector('#uploadFileBtn');

    const units = ['KB', 'MB', 'GB', 'TB'];

    uploadFile.onchange = function(){
        const fileList = [...this.files];
        setFileList(fileList);
    }

    function setFileList(fileList){
        let list = '';
        fileList.forEach(function(file, num){
            list += '<tr>';
            list += '<td>' + (num + 1) + '</td>';
            list += '<td>' + file.name + '</td>';
            list += '<td><button type="button" class="btn btn-danger" onclick="deleteFileFromList(' + num + ');">삭제</button></td>'
            list += '</tr>';
        });
        selectedFileList.innerHTML = list;
    }

    function formatFileSize(bytes) {
        let u = -1;
        const r = 10 ** 2;
        
        do {
            bytes /= 1000;
            ++u;
        } while (Math.round(Math.abs(bytes) * r) / r >= 1000 && u < units.length - 1);

        return bytes.toFixed(2) + ' ' + units[u];
    }

    function getFileBytes(file){
        return new Blob([file]).size;
    }

    function deleteFileFromList(num){
        let fileList = [...uploadFile.files];
        fileList.splice(num, 1);

        const dataTransfer = new DataTransfer();
        fileList.forEach(function(file){
            dataTransfer.items.add(file);
        });
        uploadFile.files = dataTransfer.files;

        setFileList([...uploadFile.files]);
    }

    uploadFileBtn.onclick = function(){

        const fileList = [...uploadFile.files];

        if(fileList.length < 1){
            alert('업로드할 파일을 선택해주세요.');
            return;
        }

        let fileSize = 0;
        fileList.forEach(function(file){
            fileSize += getFileBytes(file);
        });

        if(fileSize > 1000000000){
            alert('파일 크기가 너무 큽니다.\n파일 크기 : [' + formatFileSize(fileSize) + ']');
            return;
        }
        
        uploadProgress.classList.remove('d-none');
        const formData = new FormData(uploadForm);
        

        let xhr = new XMLHttpRequest();
        xhr.upload.onprogress = function(){
            const percent = Math.floor((event.loaded / event.total) * 100);
            uploadProgressBar.style.width = percent + '%';
            uploadProgressBar.textContent = percent + '%';
        };
        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4){
                if(xhr.status == 200){
                    location.href = '/file?dirId=${requestScope.dirId}';
                } else {
                    alert('에러가 발생했습니다.');
                    location.reload();
                }
            }
        }
        xhr.open('POST', '/file/upload');
        xhr.send(formData);
    }

</script>
</html>