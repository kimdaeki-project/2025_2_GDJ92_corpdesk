<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- theme meta -->
<meta name="theme-name" content="mono" />

<%-- sweet alert --%>
<script defer src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!-- GOOGLE FONTS -->
<link href="https://fonts.googleapis.com/css?family=Karla:400,700|Roboto" rel="stylesheet">
<link href="/plugins/material/css/materialdesignicons.min.css" rel="stylesheet" />
<link href="/plugins/simplebar/simplebar.css" rel="stylesheet" />

<!-- PLUGINS CSS STYLE -->
<link href="/plugins/nprogress/nprogress.css" rel="stylesheet" />




<link href="/plugins/DataTables/DataTables-1.10.18/css/jquery.dataTables.min.css" rel="stylesheet" />



<link href="/plugins/jvectormap/jquery-jvectormap-2.0.3.css" rel="stylesheet" />



<link href="/plugins/daterangepicker/daterangepicker.css" rel="stylesheet" />



<link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">



<link href="/plugins/toaster/toastr.min.css" rel="stylesheet" />


<!-- MONO CSS -->
<link id="main-css-href" rel="stylesheet" href="/css/style.css" />




<!-- FAVICON -->
<link href="/images/favicon.png" rel="shortcut icon" />

<!--
  HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries
-->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="/plugins/nprogress/nprogress.js"></script>
<style>
    /* 모달이 열렸을 때 사이드바가 모달 뒤로 가도록 */
    .modal-open .email-left-column,
    .modal-open .sidebar,
    .modal-open .side-nav {
        z-index: 1040 !important; /* Bootstrap 모달 backdrop보다 낮게 */
    }
    
    /* 또는 모달 자체의 z-index를 더 높게 설정 */
    .modal {
        z-index: 1060 !important;
    }
    .modal-backdrop {
        z-index: 1055 !important;
    }
</style>
