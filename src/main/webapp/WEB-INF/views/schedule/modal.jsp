<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<div class="modal fade" id="scheduleModal" tabindex="-1" role="dialog" aria-labelledby="scheduleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="scheduleModalLabel">일정 등록</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <form method="POST" action="/personal-schedule">
        <div class="modal-body">
          <div class="form-group">
            <label for="scheduleName">일정명</label>
            <input type="text" class="form-control" id="scheduleName" name="scheduleName" placeholder="일정명을 입력하세요">
          </div>
          <div class="form-group">
            <label for="scheduleDateTime">일시</label>
            <input type="datetime-local" class="form-control" id="scheduleDateTime" name="scheduleDateTime">
          </div>
          <div class="form-group">
            <label for="scheduleLocation">주소</label>
            <div class="d-flex justify-content-between">
              <input type="text" class="form-control" id="scheduleLocation" name="address" placeholder="주소를 입력하세요">
              <input type="button" onclick="searchAddress()" value="주소 검색" class="d_btn btn btn-light ml-2">
            </div>
          </div>
          <div class="form-group">
            <label for="scheduleContent">내용</label>
            <textarea class="form-control" id="scheduleContent" name="content" rows="3" placeholder="내용을 입력하세요"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary">등록</button>
          <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  function searchAddress() {
    new daum.Postcode({
      oncomplete: function(data) {
        document.getElementById("scheduleLocation").value = data.address;
      }
    }).open();
  }
</script>
