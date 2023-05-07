<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js"></script>
<div class="navbar navbar-light bg-light">
    <div class="container-fluid">
        <a href="/file" class="navbar-brand"><h2 class="m-0">파일 서버</h2></a>
        <sec:authorize access="isAuthenticated()">
            <div>
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#changePwModal">비밀번호 변경</button>
                <div class="modal fade" id="changePwModal" tabindex="-1" aria-labelledby="changePwModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form name="changePwForm" action="/changePw" method="post" class="d-inline" onsubmit="return checkChangePwForm();">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="changePwModalLabel">비밀번호 변경</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="form-group my-2">
                                        <label for="newPw" class="form-label">새 비밀번호</label>
                                        <input type="password" id="newPw" name="newPw" class="form-control" />
                                    </div>
                                    <div class="form-group my-2">
                                        <label for="newPwChk" class="form-label">새 비밀번호 확인</label>
                                        <input type="password" id="newPwChk" name="newPwChk" class="form-control" />
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">변경</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="resetChangePwForm();">취소</button>
                                    <script>
                                        const changePwForm = document.changePwForm;
                                        const newPw = document.querySelector('#newPw');
                                        const newPwChk = document.querySelector('#newPwChk');

                                        function checkChangePwForm(){

                                            const newPwVal = newPw.value.trim();
                                            const newPwChkVal = newPwChk.value.trim();

                                            if(newPwVal == ''){
                                                alert('새 비밀번호를 입력해주세요.');
                                                return false;
                                            }

                                            if(newPwChkVal == ''){
                                                alert('비밀번호 확인을 입력해주세요.');
                                                return false;
                                            }

                                            if(newPwVal != newPwChkVal){
                                                alert('비밀번호가 서로 일치하지 않습니다.');
                                                return false;
                                            }

                                            return true;
                                        }

                                        function resetChangePwForm(){
                                            changePwForm.reset();
                                        }
                                    </script>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <form name="logoutForm" action="/logout" method="post" class="d-inline">
                    <button type="submit" class="btn btn-danger">로그아웃</button>
                </form>
            </div>
        </sec:authorize>
    </div>
</div>